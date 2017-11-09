package jdbc.servlet.todo.service;

import jdbc.servlet.todo.dao.UserDao;
import jdbc.servlet.todo.dao.impl.UserDaoImpl;
import jdbc.servlet.todo.pojo.User;
import jdbc.servlet.todo.util.transaction.TransactionManagerImpl;

import java.util.Optional;

public class UserService {

    private UserDao userDao = new UserDaoImpl();

    public Optional<User> getUserByUsernameAndPassword(final String username, final String password) {

        return TransactionManagerImpl.INSTANCE.doInTransaction(connection -> {
            userDao.setConnection(connection);
            return userDao.getByUsernameAndPassword(username, password);

        });
    }

    public Optional<User> getUserByUserName(final String username) {
        return TransactionManagerImpl.INSTANCE.doInTransaction(connection -> {
            userDao.setConnection(connection);
            return userDao.getByUserName(username);
        });
    }

    public Optional<User> getUserById(int id) {
        return TransactionManagerImpl.INSTANCE.doInTransaction(connection -> {
            userDao.setConnection(connection);
            return userDao.getById(id);
        });
    }

    public int saveUser(User user) {
        return TransactionManagerImpl.INSTANCE.doInTransaction(connection -> {
            userDao.setConnection(connection);
            return userDao.create(user);
        });
    }


}
