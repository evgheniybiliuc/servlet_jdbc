package jdbc.servlet.todo.servlet;

import jdbc.servlet.todo.pojo.Note;
import jdbc.servlet.todo.pojo.User;
import jdbc.servlet.todo.service.NoteService;
import jdbc.servlet.todo.util.HttpErrorCode;
import jdbc.servlet.todo.util.LayoutRender;
import jdbc.servlet.todo.util.StringValidator;
import jdbc.servlet.todo.util.error.HttpResponder;
import jdbc.servlet.todo.util.user.UserSessionRetriever;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/home")
@MultipartConfig
public class HomeServlet extends HttpServlet {

    private static final String ACTION_TAG = "_a";
    private static final String ID_TAG = "_d";


    private static final String ACTION_TYPE_DONE = "done";
    private static final String ACTION_TYPE_CREATE = "create";
    private static final String ACTION_TYPE_DELETE = "delete";

    private static final String TITLE_PARAMETER_NAME = "title";


    private UserSessionRetriever userSessionRetriever = new UserSessionRetriever();
    private NoteService noteService = new NoteService();
    private HttpResponder httpResponder = new HttpResponder();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Note> notes = new ArrayList<>();

        Optional<User> user = userSessionRetriever.retrieveFrom(req.getSession());
        user.ifPresent(u -> notes.addAll(noteService.getNotesFor(u)));

        req.setAttribute("notes", notes);
        LayoutRender.render(req, resp, "/view/home.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> userOptional = userSessionRetriever.retrieveFrom(req.getSession());
        String actionType = req.getParameter(ACTION_TAG);

        if (!userOptional.isPresent() || StringValidator.isNullOrEmpty(actionType)) {
            httpResponder.sendResponse(resp, HttpErrorCode.BAD_REQUEST);
            return;
        }

        User user = userOptional.get();

        if (actionType.equals(ACTION_TYPE_CREATE)) {
            String title = req.getParameter(TITLE_PARAMETER_NAME);

            if (StringValidator.isNullOrEmpty(title)) {
                httpResponder.sendResponse(resp, HttpErrorCode.BAD_REQUEST);
                return;
            }

            int id = noteService.create(title, user);

            if (id == -1) {
                httpResponder.sendResponse(resp, HttpErrorCode.ERROR);
                return;
            }
            resp.getWriter().write("{\"id\":" + id + "}");

        } else {
            int id = Integer.valueOf(req.getParameter(ID_TAG));
            Optional<Note> note = noteService.getNoteById(id);

            if (note.isPresent() && note.get().getUser().equals(user)) {
                modifyNote(note.get(), actionType);
            }

        }

    }


    private void modifyNote(Note note, String actionType) {
        switch (actionType) {
            case ACTION_TYPE_DONE:
                Note.Status status = note.getStatus() != Note.Status.DONE ? Note.Status.DONE : Note.Status.ACTIVE;
                note.setStatus(status);
                noteService.update(note);
                break;
            case ACTION_TYPE_DELETE:
                noteService.delete(note);
                break;
        }


    }
}
