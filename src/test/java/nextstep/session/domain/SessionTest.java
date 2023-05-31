package nextstep.session.domain;

import nextstep.session.NotRecruitException;
import nextstep.session.StudentNumberExceededException;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SessionTest {

    @ParameterizedTest
    @EnumSource(value = SessionStatus.class, names = {"END", "READY"})
    void 강의_수강신청은_모집중일_때만_가능하다(SessionStatus status) {

        // given
        Session session = new Session(1L, 1L, status);

        // when
        assertThatThrownBy(
                () -> session.signUp(NsUserTest.JAVAJIGI))
                .isInstanceOf(NotRecruitException.class);
    }

    @Test
    void 강의는_강의_최대_수강_인원을_초과할_수_없다() {

        // given
        Session session = new Session(1L, 0L, SessionStatus.RECRUITING);

        // when
        assertThatThrownBy(
                () -> session.signUp(NsUserTest.JAVAJIGI))
                .isInstanceOf(StudentNumberExceededException.class);
    }

    @Test
    void 수강신청_성공() throws StudentNumberExceededException, NotRecruitException {

        // given
        Session session = new Session(1L, 2L, SessionStatus.RECRUITING);

        // when
        session.signUp(NsUserTest.JAVAJIGI);

        // then
        assertThat(session.getStudents()).contains(NsUserTest.JAVAJIGI);
    }
}