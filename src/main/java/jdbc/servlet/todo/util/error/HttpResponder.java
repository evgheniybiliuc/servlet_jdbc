package jdbc.servlet.todo.util.error;

import jdbc.servlet.todo.util.HttpErrorCode;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpResponder implements HttpServletErrorResponder {

    @Override
    public void sendResponse(HttpServletResponse response, HttpErrorCode errCode, String message) {
        response.setContentType("text/html");
        try {
            response.sendError(errCode.getCode(), message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendResponse(HttpServletResponse response, HttpErrorCode errCode) {
        response.setContentType("text/html");
        try {
            response.sendError(errCode.getCode(), errCode.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
