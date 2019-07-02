package chess.service;

import chess.domain.BoardStateFactory;
import chess.domain.GameResult;
import chess.domain.RegularBoardStateFactory;
import chess.persistence.PersistenceUnitFactory;
import chess.persistence.dao.BoardStateDao;
import chess.persistence.dao.GameSessionDao;
import chess.persistence.dto.BoardStateDto;
import chess.persistence.dto.GameSessionDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

public class SessionService {
    private EntityManagerFactory emf;

    public SessionService() {
        this.emf = PersistenceUnitFactory.getEntityManagerFactory();
    }

    public GameSessionDto createSession(String title) {
        EntityManager em = emf.createEntityManager();
        GameSessionDto sess = GameSessionDto.of(GameResult.KEEP.name(), title);
        GameSessionDao sessionDao = GameSessionDao.of(em);
        sessionDao.save(sess);
        createBoardState(new RegularBoardStateFactory(), sess.getId());
        em.close();
        return sess;
    }

    private void createBoardState(BoardStateFactory boardStateFactory, long sessionId) {
        EntityManager em = emf.createEntityManager();
        GameSessionDao sessionDao = GameSessionDao.of(em);
        BoardStateDao boardStateDao = BoardStateDao.of(em);
        GameSessionDto sess = sessionDao.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다. ID: " + sessionId));
        boardStateFactory.create().entryStream()
            .map(entry -> BoardStateDto.of(entry.getValue().getType().name(),
                entry.getKey().getXSymbol(),
                entry.getKey().getYSymbol(), sess)
            )
            .forEach(boardStateDao::save);
        em.close();
    }

    public Optional<GameSessionDto> findSessionById(long id) {
        EntityManager em = emf.createEntityManager();
        GameSessionDao sessionDao = GameSessionDao.of(em);
        Optional<GameSessionDto> found = sessionDao.findById(id);
        em.close();
        return found;
    }

    public List<GameSessionDto> findLatestSessions(int limit) {
        EntityManager em = emf.createEntityManager();
        GameSessionDao sessionDao = GameSessionDao.of(em);
        List<GameSessionDto> sessions = sessionDao.findLatestSessions(limit);
        em.close();
        return sessions;
    }

    public void updateSessionState(long id, GameResult stateTo) {
        EntityManager em = emf.createEntityManager();
        GameSessionDao sessionDao = GameSessionDao.of(em);
        GameSessionDto session = sessionDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("게임 세션을 찾을 수 없습니다: " + id));
        session.setState(stateTo.name());
        sessionDao.save(session);
        em.close();
    }
}
