package chess.persistence.dao;

import chess.persistence.dto.GameSessionDto;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class GameSessionDao {

    private EntityManager em;

    private GameSessionDao(EntityManager em) {
        this.em = em;
    }

    public static GameSessionDao of(EntityManager em) {
        return new GameSessionDao(em);
    }

    public GameSessionDto save(GameSessionDto session) {
        em.getTransaction().begin();
        em.persist(session);
        em.getTransaction().commit();
        return session;
    }

    public Optional<GameSessionDto> findById(long id) {
        return Optional.ofNullable(em.find(GameSessionDto.class, id));
    }

    public Optional<GameSessionDto> findByTitle(String title) {
        GameSessionDto result = em.createQuery("SELECT s FROM game_session s WHERE title=:title", GameSessionDto.class)
            .setParameter("title", title)
            .getSingleResult();
        return Optional.ofNullable(result);
    }

    public List<GameSessionDto> findLatestSessions(int limit) {
        return em.createQuery("SELECT s FROM game_session s ORDER BY reg_date DESC", GameSessionDto.class)
            .setMaxResults(limit)
            .getResultList();
    }
}
