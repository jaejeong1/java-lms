package nextstep.courses.domain.session;

import nextstep.courses.domain.participant.ParticipantManager;
import nextstep.courses.domain.participant.SessionParticipants;
import nextstep.courses.domain.participant.SessionUserEnrolment;
import nextstep.courses.exception.EndSessionException;
import nextstep.courses.exception.MaxParticipantsException;
import nextstep.courses.exception.MissMatchPriceException;
import nextstep.courses.type.SessionStatus;
import nextstep.courses.type.ParticipantSelectionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EnrolmentTest {

    @DisplayName("강의 유료 결제 한다.")
    @Test
    void 강의유료결제한다() {
        // given
        int money = 10000;
        ParticipantManager participantManager = new ParticipantManager(10);
        Price price = new Price(money);
        Enrolment enrolment = new Enrolment(participantManager, price, SessionStatus.READY);
        // when
        enrolment.addParticipant(money, new SessionUserEnrolment(1L, 1L, ParticipantSelectionStatus.WAITING));
        // then
        assertThat(enrolment.nowParticipants()).isEqualTo(1);
    }

    @DisplayName("강의 참여가 최대를 넘기면 예외가 발생한다.")
    @Test
    void 강의_참여가_최대를_넘기면_예외가_발생한다() {
        // given
        int money = 10000;
        List<SessionUserEnrolment> users = new ArrayList<>() {{
            add(new SessionUserEnrolment(1L, 1L, ParticipantSelectionStatus.WAITING));
        }};
        ParticipantManager participantManager = new ParticipantManager(1, new SessionParticipants(users));
        Price price = new Price(money);
        Enrolment enrolment = new Enrolment(participantManager, price, SessionStatus.READY);
        // when
        // then
        assertThatThrownBy(() -> enrolment.addParticipant(money, new SessionUserEnrolment(2L, 1L, ParticipantSelectionStatus.WAITING)))
                .isInstanceOf(MaxParticipantsException.class);
    }

    @DisplayName("결제할 금액이 강의가격과 다르면 예외가 발생한다.")
    @Test
    void 결제할_금액이_강의가격과_다르면_예외가_발생한다() {
        // given
        int money = 10000;
        ParticipantManager participantManager = new ParticipantManager(10);
        Price price = new Price(money);
        Enrolment enrolment = new Enrolment(participantManager, price, SessionStatus.READY);
        // when
        // then
        assertThatThrownBy(() -> enrolment.addParticipant(1000, new SessionUserEnrolment(2L, 1L, ParticipantSelectionStatus.WAITING)))
                .isInstanceOf(MissMatchPriceException.class);
    }

    @DisplayName("강의가 종료 상태이면 예외가 발생한다.")
    @Test
    void 강의가_종료_상태이면_예외가_발생한다() {
        // given
        int money = 10000;
        ParticipantManager participantManager = new ParticipantManager(10);
        Price price = new Price(money);
        Enrolment enrolment = new Enrolment(participantManager, price, SessionStatus.FINISH);
        // when
        // then
        assertThatThrownBy(() -> enrolment.addParticipant(money, new SessionUserEnrolment(2L, 1L, ParticipantSelectionStatus.WAITING)))
                .isInstanceOf(EndSessionException.class);
    }
}