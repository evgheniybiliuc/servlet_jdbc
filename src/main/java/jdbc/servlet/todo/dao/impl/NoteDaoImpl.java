package jdbc.servlet.todo.dao.impl;

import jdbc.servlet.todo.dao.NoteDao;
import jdbc.servlet.todo.exceptions.NotSameLengthException;
import jdbc.servlet.todo.pojo.Note;
import jdbc.servlet.todo.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NoteDaoImpl implements NoteDao {

    private Connection connection;

    private static final String ALL = "id,user_id,title,description,status,created_at";
    private static final String UPDATE_NOTE = "UPDATE note SET title = ?,description = ?,status = ? WHERE id = ?";
    private static final String GET_NOTE_BY_ID = "SELECT " + ALL + " FROM note WHERE id = ?";
    private static final String DELETE_NOTE = "DELETE FROM note WHERE id = ?";
    private static final String CREATE_NOTE = "INSERT INTO note (user_id, title, description, status) VALUES (?,?,?,?)";
    private static final String GET_NOTES_FOR_USER = "SELECT " + ALL + " FROM note WHERE  user_id = ?";

    @Override
    public Optional<Note> getById(int id) {
        Optional<Note> result = Optional.empty();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(GET_NOTE_BY_ID)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = Optional.of(generateNoteFrom(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public Optional<Note> getByParams(String[] params, Object[] values) throws NotSameLengthException {
        return null;
    }

    @Override
    public List<Note> getAll() {
        return null;
    }

    @Override
    public int create(Note obj) {
        int id = -1;
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(CREATE_NOTE)) {
            preparedStatement.setInt(1, obj.getUserId());
            preparedStatement.setString(2, obj.getTitle());
            preparedStatement.setString(3, obj.getDescription());
            preparedStatement.setString(4, obj.getStatus().toString());
            if (preparedStatement.executeUpdate() == 1) {
                ResultSet idSet = preparedStatement.getGeneratedKeys();
                if (idSet.next()) {
                    id = idSet.getInt(1);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public boolean delete(Note obj) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(DELETE_NOTE)) {

            preparedStatement.setInt(1, obj.getId());
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Note obj) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(UPDATE_NOTE)) {
            preparedStatement.setString(1, obj.getTitle());
            preparedStatement.setString(2, obj.getDescription());
            preparedStatement.setString(3, obj.getStatus().toString());
            preparedStatement.setInt(4, obj.getId());
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public List<Note> getAllFor(User user) {
        List<Note> list = new ArrayList<>();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(GET_NOTES_FOR_USER)) {
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(generateNoteFrom(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    private Note generateNoteFrom(ResultSet resultSet) throws SQLException {
        Note note = new Note();
        note.setId(resultSet.getInt("id"));
        note.setUserId(resultSet.getInt("user_id"));
        note.setTitle(resultSet.getString("title"));
        note.setDescription(resultSet.getString("description"));
        note.setStatus(Enum.valueOf(Note.Status.class, resultSet.getString("status")));
        note.setCreatedAt(resultSet.getDate("created_at"));
        return note;
    }
}
