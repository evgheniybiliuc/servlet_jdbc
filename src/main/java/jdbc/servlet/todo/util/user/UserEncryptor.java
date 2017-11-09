package jdbc.servlet.todo.util.user;

import jdbc.servlet.todo.pojo.User;
import jdbc.servlet.todo.util.encryptor.BasicStringEncryptor;

public class UserEncryptor extends BasicStringEncryptor {

    public String encrypt(User user) {
        String password = user.getPassword();
        String email = user.getEmail();
        return encrypt(password + email);
    }
}
