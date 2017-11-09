package jdbc.servlet.todo.util.user;

import jdbc.servlet.todo.pojo.User;
import jdbc.servlet.todo.util.session.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class UserSessionAuthorizer {

    private final UserEncryptor userEncryptor = new UserEncryptor();
    private final HttpSession session;


    public UserSessionAuthorizer(HttpSession session) {
        this.session = session;
    }

    public void authorize(User user) {
        String encHashString = userEncryptor.encrypt(user);

        session.setAttribute(SessionAttributes.LOGIN.getValue(), user.getUsername());
        session.setAttribute(SessionAttributes.USER_ENC_KEY.getValue(), encHashString);
    }

    public boolean isUserAuthorized() {
        UserSessionRetriever userSessionRetriever = new UserSessionRetriever();
        Optional<User> user = userSessionRetriever.retrieveFrom(session);

        String userEncryptedKey = (String) session.getAttribute(SessionAttributes.USER_ENC_KEY.getValue());


        return user.filter(u -> userEncryptor.encrypt(u).equals(userEncryptedKey)).isPresent();

    }
}
