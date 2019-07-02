package chess.persistence.dao;

import chess.domain.GameResult;
import chess.domain.boardcell.PieceType;
import chess.persistence.dto.BoardStateDto;
import chess.persistence.dto.GameSessionDto;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardStateDaoTest {
    private static final String PU_NAME = "chess-jpa-unit";

    private static EntityManagerFactory emf;

    private EntityManager em;
    private BoardStateDao boardStateDao;
    private GameSessionDao gameSessionDao;

    @BeforeAll
    static void init() {
        emf = Persistence.createEntityManagerFactory(PU_NAME);
    }

    @AfterAll
    static void cleanup() {
        emf.close();
        emf = null;
    }

    @BeforeEach
    void createEm() {
        this.em = emf.createEntityManager();
        this.gameSessionDao = GameSessionDao.of(em);
        this.boardStateDao = BoardStateDao.of(em);
    }

    @AfterEach
    void closeEm() {
        this.em.close();
        this.em = null;
        this.gameSessionDao = null;
        this.boardStateDao = null;
    }

    @Test
    void insert() {
        BoardStateDto dto = BoardStateDto.of(PieceType.ROOK_BLACK.name(), "a", "8");
        BoardStateDto inserted = boardStateDao.save(dto);
        assertThat(inserted.getId()).isNotNull();
    }

    @Test
    void findById() {
        BoardStateDto dto = boardStateDao.save(BoardStateDto.of(PieceType.ROOK_BLACK.name(), "a", "8"));
        BoardStateDto found = boardStateDao.findById(dto.getId()).get();
        assertThat(found).isEqualTo(dto);
    }

    @Test
    void findBySessionId() {
        GameSessionDto sess = gameSessionDao.save(GameSessionDto.of(GameResult.KEEP.name(), "room2"));
        BoardStateDto state1 = boardStateDao.save(BoardStateDto.of(null, PieceType.ROOK_WHITE.name(), "b", "2", sess));
        BoardStateDto state2 = boardStateDao.save(BoardStateDto.of(null, PieceType.ROOK_BLACK.name(), "a", "8", sess));
        em.clear();
        GameSessionDto founds = gameSessionDao.findById(sess.getId()).get();
        assertThat(founds.getPieces())
            .hasSize(2)
            .containsExactlyInAnyOrder(state1, state2);
    }
//
    @Test
    void updateCoordById() {
        GameSessionDto sess = GameSessionDto.of(GameResult.KEEP.name(), "room3");
        gameSessionDao.save(sess);
        BoardStateDto state = BoardStateDto.of(null, PieceType.ROOK_WHITE.name(), "b", "2", sess);
        boardStateDao.save(state);
        em.clear();
        state = boardStateDao.findById(state.getId()).get();
        state.setCoordY("4");
        boardStateDao.save(state);
        em.clear();
        BoardStateDto found = boardStateDao.findById(state.getId()).get();
        assertThat(found.getType()).isEqualTo(state.getType());
        assertThat(found.getCoordX()).isEqualTo(state.getCoordX());
        assertThat(found.getCoordY()).isEqualTo(state.getCoordY());
    }

    @Test
    void findByRoomIdAndCoordinate() {
        GameSessionDto sess = GameSessionDto.of(GameResult.KEEP.name(), "room4");
        gameSessionDao.save(sess);
        BoardStateDto state = BoardStateDto.of(null, PieceType.ROOK_WHITE.name(), "b", "4", sess);
        boardStateDao.save(state);
        em.clear();
        Optional<BoardStateDto> found = boardStateDao.findBySessionIdAndCoordinate(sess.getId(), state.getCoordX(), state.getCoordY());
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getId()).isEqualTo(state.getId());
    }

    @Test
    void delete() {
        BoardStateDto inserted = boardStateDao.save(BoardStateDto.of(PieceType.ROOK_BLACK.name(), "a", "8"));
        em.clear();
        BoardStateDto found = boardStateDao.findById(inserted.getId()).get();
        boardStateDao.delete(found);
        em.clear();
        assertThat(boardStateDao.findById(inserted.getId()).isPresent()).isFalse();
    }
}
