package nextstep.courses;

public class CanNotApplySessionStatusException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    public CanNotApplySessionStatusException(String message) {
        super(message);
    }
}
