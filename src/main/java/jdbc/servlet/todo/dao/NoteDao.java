package jdbc.servlet.todo.dao;

import jdbc.servlet.todo.pojo.Note;
import jdbc.servlet.todo.pojo.User;

import java.util.List;

public interface NoteDao extends GenericDao<Note> {
    List<Note> getAllFor(User user);
}
