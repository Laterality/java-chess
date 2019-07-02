package chess.persistence.dao;

import chess.persistence.dto.BoardStateDto;

import javax.persistence.EntityManager;
import java.util.Optional;

public class BoardStateDao {

    private EntityManager em;

    private BoardStateDao(EntityManager em) {
        this.em = em;
    }

    public static BoardStateDao of(EntityManager em) {
        return new BoardStateDao(em);
    }

    public BoardStateDto save(BoardStateDto boardState) {
        em.getTransaction().begin();
        em.persist(boardState);
        em.getTransaction().commit();
        return boardState;
    }

    public Optional<BoardStateDto> findById(long id) {
        return Optional.ofNullable(em.find(BoardStateDto.class, id));
    }

    public Optional<BoardStateDto> findBySessionIdAndCoordinate(long sessionId, String coordX, String coordY) {
        BoardStateDto found = em.createQuery("SELECT b FROM board_state b WHERE session_id=:sessionId AND loc_x=:coordX AND loc_y=:coordY",
            BoardStateDto.class)
            .setParameter("sessionId", sessionId)
            .setParameter("coordX", coordX)
            .setParameter("coordY", coordY)
            .getSingleResult();

        return Optional.ofNullable(found);
    }

    public void delete(BoardStateDto boardState) {
        em.getTransaction().begin();
        em.remove(boardState);
        em.getTransaction().commit();
    }
}
