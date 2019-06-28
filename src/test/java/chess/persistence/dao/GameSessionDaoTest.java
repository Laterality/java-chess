package chess.persistence.dao;

import chess.domain.GameResult;
import chess.persistence.dto.GameSessionDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GameSessionDaoTest {
    private static final String PU_NAME = "chess-jpa-unit";

    private static GameSessionDao gameSessionDao;

    @BeforeAll
    static void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
        gameSessionDao = new GameSessionDao(emf);
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
//
//    @Test
//    void updateState() throws SQLException {
//        GameSessionDto sess = new GameSessionDto();
//        sess.setTitle("choboman");
//        sess.setState(GameResult.KEEP.name());
//        sess.setId(dao.addSession(sess));
//        sess.setState(GameResult.BLACK_WIN.name());
//        dao.updateSession(sess);
//        assertThat(dao.findById(sess.getId()).get().getState()).isEqualTo(GameResult.BLACK_WIN.name());
//        dao.deleteById(sess.getId());
//    }
}
