package chess.service;

import chess.domain.*;
import chess.persistence.DataSourceFactory;
import chess.persistence.dao.BoardStateDao;
import chess.persistence.dto.BoardStateDto;
import chess.service.dto.CoordinatePairDto;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameServiceImpl implements GameService {

    private BoardStateDao boardStateDao;

    public GameServiceImpl() {
        DataSource ds = new DataSourceFactory().createDataSource();
        boardStateDao = new BoardStateDao(ds);
    }

    @Override
    public List<BoardStateDto> findBoardStatesByRoomId(long sessionId) {
        try {
            return boardStateDao.findBySessionId(sessionId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }

    @Override
    public GameResult movePiece(CoordinatePair from, CoordinatePair to, long sessionId) {
        try {
            ChessGame game = new ChessGame(() -> findBoardStatesMapByRoomId(sessionId));
            game.move(from, to);

            deleteTargetStateIfPresent(to, sessionId);
            updateSrcState(from, to, sessionId);
            return GameResult.judge(game.getBoardState().values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }

    private GameBoardState findBoardStatesMapByRoomId(long roomId) {
        try {
            AbstractChessPieceFactory factory = new ChessPieceFactory();
            Map<CoordinatePair, ChessPiece> board = new HashMap<>();
            CoordinatePair.forEachCoordinate(coord -> board.put(coord, factory.create(PieceType.NONE)));

            boardStateDao.findBySessionId(roomId)
                .forEach(dto ->
                    board.put(CoordinatePair.from(dto.getCoordX() + dto.getCoordY()).get(),
                        factory.create(PieceType.valueOf(dto.getType()))));
            return GameBoardState.of(board);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }

    private void deleteTargetStateIfPresent(CoordinatePair to, long roomId) throws SQLException {
        boardStateDao.findByRoomIdAndCoordinate(roomId, to.getX().getSymbol(), to.getY().getSymbol())
            .ifPresent(dto -> tryDeleteBoardStateById(dto.getId()));
    }

    private void tryDeleteBoardStateById(long id) {
        try {
            boardStateDao.deleteById(id);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }

    private void updateSrcState(CoordinatePair from, CoordinatePair to, long roomId) throws SQLException {
        boardStateDao.findByRoomIdAndCoordinate(roomId, from.getX().getSymbol(), from.getY().getSymbol())
            .ifPresent(dto -> {
                dto.setCoordX(to.getX().getSymbol());
                dto.setCoordY(to.getY().getSymbol());
                tryUpdateBoardState(dto);
            });
    }

    private void tryUpdateBoardState(BoardStateDto dto) {
        try {
            boardStateDao.updateCoordById(dto);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }

    @Override
    public List<CoordinatePairDto> findMovableCoordinates(long sessionId, CoordinatePair from) {
        GameBoardState state = findBoardStatesMapByRoomId(sessionId);
        return state.at(from).getMovableCoordinates(coord -> state.at(coord).getType().getTeam(), from).stream()
            .map(coord -> {
                CoordinatePairDto dto = new CoordinatePairDto();
                dto.setX(coord.getX().getSymbol());
                dto.setY(coord.getY().getSymbol());
                return dto;
            })
            .collect(Collectors.toList());
    }
}
