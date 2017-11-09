package jdbc.servlet.todo.util.error;

import jdbc.servlet.todo.util.HttpErrorCode;

import javax.servlet.http.HttpServletResponse;

public interface HttpServletErrorResponder {
    void sendResponse(HttpServletResponse response, HttpErrorCode errCode, String message);

}
