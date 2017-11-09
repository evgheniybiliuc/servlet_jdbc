package jdbc.servlet.todo.util.user;

import jdbc.servlet.todo.pojo.User;
import jdbc.servlet.todo.service.UserService;
import jdbc.servlet.todo.util.StringValidator;
import jdbc.servlet.todo.util.session.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class UserSessionRetriever implements Retriever<User, HttpSession> {

    private UserService userService = new UserService();

    @Override
    public Optional<User> retrieveFrom(HttpSession session) {

        String login = (String) session.getAttribute(SessionAttributes.LOGIN.getValue());


        if (StringValidator.isNullOrEmpty(login)) return Optional.empty();

        return userService.getUserByUserName(login);
    }
}
