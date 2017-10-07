package jdbc.servlet.todo.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class LayoutRender {


    private static String HEADER = "/view/layout/header.jsp";
    private static String FOOTER = "/view/layout/footer.jsp";

    private LayoutRender() {
    }

    public static void render(HttpServletRequest req, HttpServletResponse response, String viewPath) throws ServletException, IOException {
        req.getRequestDispatcher(HEADER).include(req, response);
        req.getRequestDispatcher(viewPath).include(req, response);
        req.getRequestDispatcher(FOOTER).include(req, response);
    }

    public static void setHeader(String header) {
        HEADER = header;
    }


    public static void setFooter(String footer) {
        FOOTER = footer;
    }
}
