package jdbc.servlet.todo.dao;

import jdbc.servlet.todo.pojo.User;

import java.util.HashMap;
import java.util.Optional;

public interface UserDao extends GenericDao<User> {
    Optional<User> getByUsernameAndPassword(String name, String password);
    Optional<User> getByUserName(String username);
}
