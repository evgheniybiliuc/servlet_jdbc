package jdbc.servlet.todo.exceptions;

public class NotSameLengthException extends RuntimeException {
     static final long serialVersionUID = -3042186455058047285L;

    public NotSameLengthException(String message) {
        super(message);
    }

    public NotSameLengthException() {
    }
}
