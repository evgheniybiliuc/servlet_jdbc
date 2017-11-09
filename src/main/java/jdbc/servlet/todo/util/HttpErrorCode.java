package jdbc.servlet.todo.util;

public enum HttpErrorCode {
    OK(200, "OK"), BAD_REQUEST(400, "BAD REQUEST"), UNAUTHORIZED(401, "UNATHORIZED"), ERROR(500, "SOMETHING WRONG");


    private final int code;
    private String message;

    HttpErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
