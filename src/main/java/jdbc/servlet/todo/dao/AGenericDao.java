package jdbc.servlet.todo.dao;

import jdbc.servlet.todo.exceptions.IllegalNameFormatException;
import jdbc.servlet.todo.exceptions.NotSameLengthException;
import jdbc.servlet.todo.exceptions.UndefinedValueException;

import java.sql.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AGenericDao {
    private static final String DAO_SUFFIX = "Dao";

    private final String TABLE_NAME;

    public AGenericDao(String tableName) {
        TABLE_NAME = tableName;
    }

    public AGenericDao() {
        TABLE_NAME = retrieveTableNameFromClassName().toLowerCase();
    }

    /**
     * @return Table name based on current class name;
     * @throws IllegalNameFormatException if class name don`t contain Dao suffix or class name is Dao.
     */
    private String retrieveTableNameFromClassName() {
        String className = getClass().getSimpleName();
        if (!className.contains(DAO_SUFFIX) || className.equals(DAO_SUFFIX))
            throw new IllegalNameFormatException("Illegal format of the current class name :" + className + ".Name must contain Dao suffix");
        String[] splittedClassName = className.split(DAO_SUFFIX);

        return splittedClassName[0];
    }

    protected Optional<ResultSet> getById(int id) {
        return getByParams(new String[]{"id"}, new Integer[]{id});
    }

    /**
     * @param params - Table rows.
     * @param values - Table values.
     * @return Optional value based
     * @throws NotSameLengthException if length of params and values is different.
     * @throws IllegalStateException  if length is zero or less.
     */
    protected Optional<ResultSet> getByParams(String[] params, Object[] values) throws NotSameLengthException, IllegalStateException {
        Optional<ResultSet> result = Optional.empty();

        validateArrayData(params, values);

        String preparedParameters = Arrays.stream(params)
                .collect(Collectors.joining("=? AND ", "", "=?"));

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + preparedParameters;

        try (PreparedStatement preparedStatement = getPreparedStatementForQueryWithValues(query, values)) {
            result = Optional.ofNullable(preparedStatement.executeQuery());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void validateArrayData(String[] params, Object[] values) {
        if (params.length == 0)
            throw new IllegalStateException("params length must be greater than 0 current is " + params.length);

        if (params.length != values.length)
            throw new NotSameLengthException("different params and values length {params:" + params.length + ", values:" + values.length + "}");

        if (containsNull(values))
            throw new NullPointerException("values objects contains null");
        if (containsNull(params))
            throw new NullPointerException("values objects contains null");
    }

    private boolean containsNull(Object[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) return true;
        }
        return false;
    }

    private PreparedStatement getPreparedStatementForQueryWithValues(String query, Object[] values) throws SQLException {

        PreparedStatement preparedStatement = getConnection().prepareStatement(query);

        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof String) {
                preparedStatement.setString(i, (String) values[i]);
            } else if (values[i] instanceof Integer) {
                preparedStatement.setInt(i, (Integer) values[i]);
            } else if (values[i] instanceof Date) {
                preparedStatement.setDate(i, (Date) values[i]);
            } else {
                throw new UndefinedValueException();
            }

        }


        return preparedStatement;
    }

    protected Optional<ResultSet> getAll() {
        Optional<ResultSet> result = Optional.empty();

        try (Statement statement = getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            result = Optional.ofNullable(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    protected boolean insert(String[] params, Object[] values) {
        validateArrayData(params, values);
        String concatenatedParams = Arrays.stream(params).collect(Collectors.joining(",", "(", ")"));

        String concatenatedWildCards = Stream.iterate(0, i -> i + 1)
                .limit(params.length)
                .map(m -> "?")
                .collect(Collectors.joining(",", "(", ")"));

        String query = "INSERT INTO " + TABLE_NAME + concatenatedParams + concatenatedWildCards;
        System.out.println("insert// " + query);

        try (PreparedStatement preparedStatement = getPreparedStatementForQueryWithValues(query, values)) {

            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean delete(String[] params, Object[] values) {
        validateArrayData(params, values);
        String preparedParameters = Arrays.stream(params)
                .collect(Collectors.joining("=? AND ", "", "=?"));
        String query = "DELETE FROM " + TABLE_NAME + "WHERE " + preparedParameters;

        try (PreparedStatement preparedStatement = getPreparedStatementForQueryWithValues(query, values)) {
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean update(String[] params, Object[] values, String condition) {
        validateArrayData(params, values);
        String preparedParameters = Arrays.stream(params)
                .collect(Collectors.joining("=? AND ", "", "=?"));
        String query = "UPDATE " + TABLE_NAME + " SET " + preparedParameters;
        try {
            PreparedStatement statement = getPreparedStatementForQueryWithValues(query, values);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public abstract Connection getConnection();
}