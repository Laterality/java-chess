package chess.persistence.dao;

import chess.domain.GameResult;
import chess.persistence.dto.GameSessionDto;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GameSessionDaoTest {
    private static final String PU_NAME = "chess-jpa-unit";

    private static EntityManagerFactory emf;

    private GameSessionDao gameSessionDao;
    private EntityManager em;

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
    }

    @AfterEach
    void closeEm() {
        this.em.close();
        this.em = null;
        this.gameSessionDao = null;
    }

    @Test
    void insert() {
        GameSessionDto sess = gameSessionDao.save(GameSessionDto.of(GameResult.KEEP.name(), "some sess"));
        assertThat(sess.getTitle()).isEqualTo("some sess");
    }

    @Test
    void findById() {
        GameSessionDto sess = gameSessionDao.save(GameSessionDto.of(GameResult.KEEP.name(), "some sess"));
        assertThat(gameSessionDao.findById(sess.getId()).isPresent()).isTrue();
    }

    @Test
    void findByTitle() {
        GameSessionDto sess = gameSessionDao.save(GameSessionDto.of(GameResult.KEEP.name(), "some other sess"));
        assertThat(gameSessionDao.findByTitle("some other sess").isPresent()).isTrue();
    }

    @Test
    void latestSessions() {
        GameSessionDto s1 = gameSessionDao.save(GameSessionDto.of(GameResult.BLACK_WIN.name(), "first session"));
        GameSessionDto s2 = gameSessionDao.save(GameSessionDto.of(GameResult.WHITE_WIN.name(), "second session"));
        GameSessionDto s3 = gameSessionDao.save(GameSessionDto.of(GameResult.KEEP.name(), "third session"));
        List<GameSessionDto> results = gameSessionDao.findLatestSessions(2);
        assertThat(results).hasSize(2).containsExactly(s3, s2);
    }

    @Test
    void updateState() {
        GameSessionDto sess = gameSessionDao.save(GameSessionDto.of(GameResult.KEEP.name(), "first session"));
        sess.setState(GameResult.BLACK_WIN.name());
        gameSessionDao.save(sess);
        assertThat(gameSessionDao.findById(sess.getId()).get().getState()).isEqualTo(GameResult.BLACK_WIN.name());
    }
}
