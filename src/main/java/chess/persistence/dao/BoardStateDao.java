package chess.persistence.dao;

import chess.persistence.dto.BoardStateDto;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        try {
            BoardStateDto found = em.createQuery("SELECT b FROM board_state b WHERE session_id=:sessionId AND loc_x=:coordX AND loc_y=:coordY",
                BoardStateDto.class)
                .setParameter("sessionId", sessionId)
                .setParameter("coordX", coordX)
                .setParameter("coordY", coordY)
                .getSingleResult();
            return Optional.of(found);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void delete(BoardStateDto boardState) {
        em.getTransaction().begin();
        em.remove(boardState);
        em.getTransaction().commit();
    }
}
