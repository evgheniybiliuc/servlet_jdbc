package jdbc.servlet.todo.dao;

import jdbc.servlet.todo.exceptions.NotSameLengthException;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    Optional<T> getById(int id);

    /**
     * @param params - Table rows.
     * @param values - Table values.
     * @return Optional value based
     * @throws NotSameLengthException if length of params and values is different.
     */
    Optional<T> getByParams(String[] params, Object[] values) throws NotSameLengthException;

    List<T> getAll();

    int create(T obj);

    boolean delete(T obj);

    boolean update(T obj);

    void setConnection(Connection connection);

    Connection getConnection();

}
