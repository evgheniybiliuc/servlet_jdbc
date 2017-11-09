package jdbc.servlet.todo.dao.impl;

import jdbc.servlet.todo.dao.UserDao;
import jdbc.servlet.todo.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final String ALL = "id,username,email,password,create_time";
    private static final String SELECT_USER_WHERE_ID = "SELECT " + ALL + " FROM user WHERE id = ?";
    private static final String SELECT_USER_WHERE_NAME_AND_PASSWORD = "SELECT " + ALL + " FROM user WHERE username = ? AND password = ?";
    private static final String CREATE_USER = "INSERT INTO user (username, email, password) VALUES (?,?,?)";
    private static final String SELECT_USER_WHERE_NAME = "SELECT " + ALL + " FROM user WHERE username = ?";
    private Connection connection;

    @Override
    public Optional<User> getById(int id) {
        Optional<User> result = Optional.empty();
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_USER_WHERE_ID)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setUsername(resultSet.getString("username"));
            user.setEmail(resultSet.getString("email"));
            user.setCreatedAt(new Date(resultSet.getLong("create_time")));
            result = Optional.of(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }

    @Override
    public Optional<User> getByParams(String[] params, Object[] values) {
        return null;
    }


    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public int create(User obj) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(CREATE_USER);) {
            preparedStatement.setString(1, obj.getUsername());
            preparedStatement.setString(2, obj.getEmail());
            preparedStatement.setString(3, obj.getPassword());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean delete(User obj) {
        return false;
    }

    @Override
    public boolean update(User obj) {
        return false;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public Optional<User> getByUsernameAndPassword(String name, String password) {
        Optional<User> result = Optional.empty();
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_USER_WHERE_NAME_AND_PASSWORD)) {

            statement.setString(1, name);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = Optional.of(createUserFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }


        return result;
    }

    @Override
    public Optional<User> getByUserName(String username) {
        Optional<User> user = Optional.empty();
        try( PreparedStatement statement = getConnection().prepareStatement(SELECT_USER_WHERE_NAME)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = Optional.of(createUserFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setCreatedAt(resultSet.getDate("create_time"));
        return user;
    }
}
