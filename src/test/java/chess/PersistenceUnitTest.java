package chess;

import chess.domain.boardcell.PieceType;
import chess.persistence.dto.BoardStateDto;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PersistenceUnitTest {
    private static final String PERSISTENCE_UNIT_NAME = "chess-jpa-unit";
    @Test
    void createEntityManager() {
        EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        EntityManager em = sessionFactory.createEntityManager();
    }

    @Test
    void select() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(BoardStateDto.of(PieceType.ROOK_BLACK.name(),"a", "1"));
        em.getTransaction().commit();
        List<BoardStateDto> result = em.createQuery("select s from board_state s", BoardStateDto.class).getResultList();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo(PieceType.ROOK_BLACK.name());
    }
}
