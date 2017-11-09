package jdbc.servlet.todo.exceptions;

public class IllegalNameFormatException extends RuntimeException {
     static final long serialVersionUID = -2552186125058542285L;

    public IllegalNameFormatException() {
    }

    public IllegalNameFormatException(String message) {
        super(message);
    }
}
