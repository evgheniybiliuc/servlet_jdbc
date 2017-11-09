package jdbc.servlet.todo.servlet;

import jdbc.servlet.todo.pojo.User;
import jdbc.servlet.todo.service.UserService;
import jdbc.servlet.todo.util.HttpErrorCode;
import jdbc.servlet.todo.util.StringValidator;
import jdbc.servlet.todo.util.error.HttpResponder;
import jdbc.servlet.todo.util.error.HttpServletErrorResponder;
import jdbc.servlet.todo.util.user.UserEncryptor;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
@MultipartConfig
public class RegistrationServlet extends HttpServlet {

    private HttpServletErrorResponder responder = new HttpResponder();
    private UserService userService = new UserService();
    private UserEncryptor encryptor = new UserEncryptor();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("username");
        String userPassword = req.getParameter("password");
        String userEmail = req.getParameter("email");

        System.out.println("userName " + userName);
        System.out.println("userPassword " + userPassword);
        System.out.println("userEmail " + userEmail);
        StringBuilder stringBuilder = new StringBuilder();

        if (StringValidator.isAtLeastOneEmptyOrNull(userName, userPassword, userEmail)) {
            responder.sendResponse(resp, HttpErrorCode.BAD_REQUEST, "MISSING REQUIRED PARAMETERS");
            return;//Todo://find out how to avoid return in void method(You don`t deserve cookie for this).
        }
        boolean isEmailValid = StringValidator.isValid(userEmail, "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,63}$");
        boolean isValidUserName = StringValidator.isValid(userName);

        if (!isEmailValid) stringBuilder.append("Invalid Email<br/>"); // for logging
        if (isValidUserName) stringBuilder.append("Invalid Username<br/>"); // for logging

        System.out.println("String builder " + stringBuilder.toString());


        if (isValidUserName && isEmailValid) {
            userPassword = encryptor.encrypt(userPassword);

            User user = new User();
            user.setUsername(userName);
            user.setEmail(userEmail);
            user.setPassword(userPassword);

            if (userService.saveUser(user) != -1) {
                stringBuilder.append("User has been saved");
                responder.sendResponse(resp, HttpErrorCode.OK, stringBuilder.toString());
            } else {
                stringBuilder.append("Opsss...something went wrong.");
                responder.sendResponse(resp, HttpErrorCode.ERROR, stringBuilder.toString());
            }
        } else {
            stringBuilder.append("Invalid userName or password");
            responder.sendResponse(resp, HttpErrorCode.BAD_REQUEST, stringBuilder.toString());
        }

    }
}
