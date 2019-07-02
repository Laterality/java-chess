package chess.service;

import chess.domain.*;
import chess.domain.boardcell.ChessPiece;
import chess.domain.boardcell.ChessPieceFactory;
import chess.domain.boardcell.PieceType;
import chess.persistence.DataSourceFactory;
import chess.persistence.PersistenceUnitFactory;
import chess.persistence.dao.BoardStateDao;
import chess.persistence.dao.GameSessionDao;
import chess.persistence.dto.BoardStateDto;
import chess.persistence.dto.GameSessionDto;
import chess.service.dto.CoordinatePairDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameService {
    private EntityManagerFactory emf;

    public GameService() {
        DataSource ds = DataSourceFactory.getInstance().createDataSource();
        emf = PersistenceUnitFactory.getEntityManagerFactory();
    }

    public List<BoardStateDto> findBoardStatesBySessionId(long sessionId) {
        EntityManager em = emf.createEntityManager();
        GameSessionDao gameSessionDao = GameSessionDao.of(em);
        GameSessionDto session = gameSessionDao.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다. ID: " + sessionId));
        em.close();
        return session.getPieces();
    }

    public GameResult movePiece(CoordinatePair from, CoordinatePair to, long sessionId) {
        EntityManager em = emf.createEntityManager();
        GameSessionDao sessionDao = GameSessionDao.of(em);
        ChessGame game = new ChessGame(() -> findBoardStatesMapBySessionId(sessionDao, sessionId));
        game.move(from, to);

        BoardStateDao boardStateDao = BoardStateDao.of(em);
        boardStateDao.findBySessionIdAndCoordinate(sessionId, to.getXSymbol(), to.getYSymbol())
            .ifPresent(boardStateDao::delete);
        boardStateDao.findBySessionIdAndCoordinate(sessionId, from.getXSymbol(), from.getYSymbol())
            .ifPresent(dto -> {
                dto.setCoordX(to.getXSymbol());
                dto.setCoordY(to.getYSymbol());
                boardStateDao.save(dto);
            });
        GameResult result = GameResult.judge(game.getBoardState().values());
        GameSessionDto sess = sessionDao.findById(sessionId)
            .orElseThrow(() -> new IllegalStateException("결과를 반영할 세션을 찾을 수 없습니다."));
        sess.setState(result.name());
        sessionDao.save(sess);
        em.close();
        return result;
    }

    private LivingPieceGroup findBoardStatesMapBySessionId(GameSessionDao gameSessionDao, long sessionId) {
        Map<CoordinatePair, ChessPiece> board = new HashMap<>();
        EntityManager em = emf.createEntityManager();
        gameSessionDao.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 세션 ID입니다: " + sessionId))
            .getPieces()
            .forEach(dto ->
                board.put(CoordinatePair.of(dto.getCoordX() + dto.getCoordY()).get(),
                    ChessPieceFactory.create(PieceType.valueOf(dto.getType()))));
        em.close();
        return LivingPieceGroup.of(board);
    }

    public List<CoordinatePairDto> findMovableCoordinates(long sessionId, CoordinatePair from) {
        EntityManager em = emf.createEntityManager();
        GameSessionDao gameSessionDao = GameSessionDao.of(em);
        ChessGame game = new ChessGame(() -> findBoardStatesMapBySessionId(gameSessionDao, sessionId));
        em.close();
        return game.getMovableCoordinates(from).stream()
            .map(coord -> {
                CoordinatePairDto dto = new CoordinatePairDto();
                dto.setX(coord.getXSymbol());
                dto.setY(coord.getYSymbol());
                return dto;
            })
            .collect(Collectors.toList());
    }

    public ScoreCounter calculateScore(long sessionId) {
        EntityManager em = emf.createEntityManager();
        GameSessionDao gameSessionDao = GameSessionDao.of(em);
        ChessGame game = new ChessGame(() -> findBoardStatesMapBySessionId(gameSessionDao, sessionId));
        em.close();
        return new ScoreCounter(game.getBoardState());
    }

    public GameResult judgeResult(long sessionId) {
        EntityManager em = emf.createEntityManager();
        GameSessionDao gameSessionDao = GameSessionDao.of(em);
        ChessGame game = new ChessGame(() -> findBoardStatesMapBySessionId(gameSessionDao, sessionId));
        em.close();
        return GameResult.judgeScore(game.getBoardState());
    }
}
