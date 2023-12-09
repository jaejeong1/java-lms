package nextstep.session.service;

import nextstep.session.domain.Enrollment;
import nextstep.session.domain.Session;
import nextstep.session.domain.SessionRepository;
import nextstep.session.ui.CreateSessionRequest;
import nextstep.users.domain.NsUser;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Long createSession(NsUser loginUser, CreateSessionRequest dto) {
        return sessionRepository.save(dto.toSession(loginUser));
    }

    public void enrollSession(NsUser loginUser, Long sessionId) {
        Session session = sessionRepository.findById(sessionId);
        session.enroll(loginUser);
        new Enrollment(loginUser, session);
    }
}
