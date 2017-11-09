package jdbc.servlet.todo.service;

import jdbc.servlet.todo.dao.NoteDao;
import jdbc.servlet.todo.dao.impl.NoteDaoImpl;
import jdbc.servlet.todo.pojo.Note;
import jdbc.servlet.todo.pojo.User;
import jdbc.servlet.todo.util.transaction.TransactionManagerImpl;

import java.util.List;
import java.util.Optional;

public class NoteService {
    private NoteDao noteDao = new NoteDaoImpl();
    private UserService userService = new UserService();


    public List<Note> getNotesFor(User user) {
        return TransactionManagerImpl.INSTANCE.doInTransaction(connection -> {
            noteDao.setConnection(connection);
            return initUser(noteDao.getAllFor(user));

        });
    }

    public Optional<Note> getNoteById(int id) {
        return TransactionManagerImpl.INSTANCE.doInTransaction(connection -> {
            noteDao.setConnection(connection);
            Optional<Note> note = noteDao.getById(id);
            note.ifPresent(n -> {
                User user = userService.getUserById(n.getUserId()).get();
                n.setUser(user);
            });
            return note;
        });
    }

    public boolean update(Note note) {
        return TransactionManagerImpl.INSTANCE.doInTransaction(connection -> {
            noteDao.setConnection(connection);
            return noteDao.update(note);
        });
    }

    public int create(String title, User user) {
        return TransactionManagerImpl.INSTANCE.doInTransaction(connection -> {
            noteDao.setConnection(connection);
            Note note = new Note();
            note.setTitle(title);
            note.setUserId(user.getId());
            note.setStatus(Note.Status.ACTIVE);


            return noteDao.create(note);
        });
    }

    public boolean delete(Note note) {
        return TransactionManagerImpl.INSTANCE.doInTransaction(connection -> {
            noteDao.setConnection(connection);
            return noteDao.delete(note);
        });
    }

    private List<Note> initUser(List<Note> notes) {
        if (notes.size() > 0) {
            User user = userService.getUserById(notes.get(0).getUserId()).get();
            for (Note note : notes) {
                note.setUser(user);
            }
        }

        return notes;
    }


}
