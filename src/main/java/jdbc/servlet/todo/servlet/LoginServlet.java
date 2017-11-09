package jdbc.servlet.todo.servlet;

import jdbc.servlet.todo.pojo.User;
import jdbc.servlet.todo.service.UserService;
import jdbc.servlet.todo.util.HttpErrorCode;
import jdbc.servlet.todo.util.LayoutRender;
import jdbc.servlet.todo.util.StringValidator;
import jdbc.servlet.todo.util.user.UserSessionAuthorizer;
import jdbc.servlet.todo.util.encryptor.BasicStringEncryptor;
import jdbc.servlet.todo.util.error.HttpResponder;
import jdbc.servlet.todo.util.error.HttpServletErrorResponder;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


@WebServlet("/auth")
@MultipartConfig
public class LoginServlet extends HttpServlet {

    private static final String MISSING_REQUIRED_PARAMETERS = "MISSING REQUIRED PARAMETERS";
    private static final String USER_DONT_EXIST = "USER DON`T EXISTS";
    private static final String DONE = "DONE";

    private UserService userService = new UserService();
    private HttpServletErrorResponder errorResponder = new HttpResponder();
    private BasicStringEncryptor basicStringEncryptor = new BasicStringEncryptor();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LayoutRender.render(req, resp, "/view/auth.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserSessionAuthorizer userSessionAuhorizer = new UserSessionAuthorizer(req.getSession());

        String userName = req.getParameter("user");
        String password = req.getParameter("password");

        if (StringValidator.isAtLeastOneEmptyOrNull(userName, basicStringEncryptor.encrypt(password))) {
            errorResponder.sendResponse(resp, HttpErrorCode.BAD_REQUEST, MISSING_REQUIRED_PARAMETERS);
            return;
        }
        Optional<User> user = userService.getUserByUsernameAndPassword(userName, basicStringEncryptor.encrypt(password));
        if (user.isPresent()) {

            userSessionAuhorizer.authorize(user.get());
            errorResponder.sendResponse(resp, HttpErrorCode.OK, DONE);
        } else {
            errorResponder.sendResponse(resp, HttpErrorCode.UNAUTHORIZED, USER_DONT_EXIST);
        }
    }

}
