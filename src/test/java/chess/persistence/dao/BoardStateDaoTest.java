package chess.persistence.dao;

import chess.domain.boardcell.PieceType;
import chess.persistence.dto.BoardStateDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardStateDaoTest {
    private static final String PU_NAME = "chess-jpa-unit";

    private static EntityManagerFactory emf;
    private static BoardStateDao boardStateDao;
    private static GameSessionDao sessionDao;

    @BeforeAll
    static void init() {
        emf = Persistence.createEntityManagerFactory(PU_NAME);
        boardStateDao = new BoardStateDao(emf);
    }

    @Test
    void insert() {
        BoardStateDto dto = BoardStateDto.of(PieceType.ROOK_BLACK.name(), "a", "8");
        BoardStateDto inserted = boardStateDao.save(dto);
        assertThat(inserted.getId()).isNotNull();
    }

//    @Test
//    void findByRoomId() {
//        GameSessionDto sess = sessionDao.save(GameSessionDto.of(GameResult.KEEP.name(), "room2"));
//        BoardStateDto state1 = BoardStateDto.of(PieceType.ROOK_WHITE.name(), "b", "2");
//        BoardStateDto state2 = BoardStateDto.of(PieceType.ROOK_BLACK.name(), "a", "8");
//        sess.setId(gameSessionDao.addSession(sess));
//        state1.setId(boardStateDao.addState(state1, sess.getId()));
//        state2.setId(boardStateDao.addState(state2, sess.getId()));
//        List<BoardStateDto> founds = boardStateDao.findBySessionId(sess.getId());
//        assertThat(founds).hasSize(2);
//        gameSessionDao.deleteById(sess.getId());
//        boardStateDao.deleteById(state1.getId());
//        boardStateDao.deleteById(state2.getId());
//    }
//
//    @Test
//    void updateCoordById() throws SQLException {
//        GameSessionDto sess = GameSessionDto.of(GameResult.KEEP.name(), "room3");
//        sess.setId(gameSessionDao.addSession(sess));
//        BoardStateDto state = BoardStateDto.of(PieceType.ROOK_WHITE.name(), "b", "2");
//        state.setId(boardStateDao.addState(state, sess.getId()));
//        state.setCoordY("4");
//        assertThat(boardStateDao.updateCoordById(state)).isEqualTo(1);
//        BoardStateDto found = boardStateDao.findById(state.getId()).get();
//        assertThat(found.getType()).isEqualTo(state.getType());
//        assertThat(found.getCoordX()).isEqualTo(state.getCoordX());
//        assertThat(found.getCoordY()).isEqualTo(state.getCoordY());
//        gameSessionDao.deleteById(sess.getId());
//        boardStateDao.deleteById(state.getId());
//    }
//
//    @Test
//    void findByRoomIdAndCoordinate() throws SQLException {
//        GameSessionDto sess = GameSessionDto.of(GameResult.KEEP.name(), "room4");
//        sess.setId(gameSessionDao.addSession(sess));
//        BoardStateDto state = BoardStateDto.of(PieceType.ROOK_WHITE.name(), "b", "4");
//        state.setId(boardStateDao.addState(state, sess.getId()));
//        Optional<BoardStateDto> found = boardStateDao.findByRoomIdAndCoordinate(sess.getId(), state.getCoordX(), state.getCoordY());
//        assertThat(found.isPresent()).isTrue();
//        assertThat(found.get().getId()).isEqualTo(state.getId());
//        gameSessionDao.deleteById(sess.getId());
//        boardStateDao.deleteById(state.getId());
//    }
}
