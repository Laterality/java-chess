package chess.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUnitFactory {
    private static final String PU_NAME = "chess-jpa-unit";
    private static EntityManagerFactory emf;

    static {
        emf = Persistence.createEntityManagerFactory(PU_NAME);
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
