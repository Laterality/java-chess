package chess.persistence.dao;

import chess.persistence.dto.GameSessionDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameSessionDao {

    private DataSource dataSource;
    private EntityManagerFactory emf;

    public GameSessionDao(DataSource ds) {
        this.dataSource = ds;
    }

    public GameSessionDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public long addSession(GameSessionDto sess) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement query = conn.prepareStatement(GameSessionDaoSql.INSERT, Statement.RETURN_GENERATED_KEYS)) {
            query.setString(1, sess.getTitle());
            query.setString(2, sess.getState());
            return getGeneratedKey(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GameSessionDto save(GameSessionDto session) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(session);
        em.getTransaction().commit();
        em.close();
        return session;
    }

    private long getGeneratedKey(PreparedStatement query) throws SQLException {
        query.executeUpdate();
        try (ResultSet rs = query.getGeneratedKeys()) {
            rs.next();
            return rs.getLong(1);
        }
    }

    public Optional<GameSessionDto> findById(long id) {
        EntityManager em = emf.createEntityManager();
        GameSessionDto sess = em.find(GameSessionDto.class, id);
        em.close();
        return Optional.ofNullable(sess);
    }

    private Optional<GameSessionDto> handleSingleResult(PreparedStatement query) throws SQLException {
        try (ResultSet rs = query.executeQuery()) {
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional.of(mapResult(rs));
        }
    }

    private GameSessionDto mapResult(ResultSet rs) throws SQLException {
        return GameSessionDto.of(
            rs.getLong("id"),
            rs.getString("state"),
            rs.getString("title")
        );
    }

    public Optional<GameSessionDto> findByTitle(String title) {
        EntityManager em = emf.createEntityManager();
        GameSessionDto found = em.createQuery("select s from game_session s where title=:title", GameSessionDto.class)
            .setParameter("title", title)
            .getSingleResult();
        em.close();
        return Optional.ofNullable(found);
    }

    public List<GameSessionDto> findLatestSessions(int limit) {
        EntityManager em = emf.createEntityManager();
        List<GameSessionDto> results = em.createQuery("select s from game_session s order by reg_date desc", GameSessionDto.class)
            .setMaxResults(limit)
            .getResultList();
        em.close();
        return results;
    }

    private List<GameSessionDto> handleMultipleResults(PreparedStatement query) throws SQLException {
        try (ResultSet rs = query.executeQuery()) {
            List<GameSessionDto> founds = new ArrayList<>();
            while (rs.next()) {
                founds.add(mapResult(rs));
            }
            return founds;
        }
    }

    public int deleteById(long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement query = conn.prepareStatement(GameSessionDaoSql.DELETE_BY_ID)) {
            query.setLong(1, id);
            return query.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int updateSession(GameSessionDto sess) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement query = conn.prepareStatement(GameSessionDaoSql.UPDATE)) {
            query.setString(1, sess.getTitle());
            query.setString(2, sess.getState());
            query.setLong(3, sess.getId());
            return query.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static class GameSessionDaoSql {
        private static final String INSERT = "INSERT INTO game_session(title, state) VALUES(?, ?)";
        private static final String SELECT_BY_ID = "SELECT id, title, state FROM game_session WHERE id=?";
        private static final String SELECT_BY_TITLE = "SELECT id, title, state FROM game_session WHERE title=?";
        private static final String SELECT_LATEST_N = "SELECT id, title, state FROM game_session ORDER BY id DESC LIMIT ?";
        private static final String UPDATE = "UPDATE game_session SET title=?, state=? WHERE id=?";
        private static final String DELETE_BY_ID = "DELETE FROM game_session WHERE id=?";
    }
}
