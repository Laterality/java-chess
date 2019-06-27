package chess;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUnitTest {
    @Test
    void createEntityManager() {
        EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("com.woowacourse.chess.jpa");

        EntityManager em = sessionFactory.createEntityManager();
    }
}
