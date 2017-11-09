package jdbc.servlet.todo.servlet;

import jdbc.servlet.todo.util.LayoutRender;
import jdbc.servlet.todo.util.user.UserSessionAuthorizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "home", value = "/")
public class IndexServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserSessionAuthorizer userSessionAuthorizer = new UserSessionAuthorizer(req.getSession());

        if (userSessionAuthorizer.isUserAuthorized())
            resp.sendRedirect("/home");

        LayoutRender.render(req, resp, "/view/index.jsp");
    }
}
