package jdbc.servlet.todo.filter;

import jdbc.servlet.todo.util.user.UserSessionAuthorizer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class AuthorizationFilter extends GenericFilter {


    private static final String HOME = "/";

    private static final String ASSETS_DIRECTORY = "/styles";

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        UserSessionAuthorizer userSessionAuthorizer = new UserSessionAuthorizer(req.getSession(true));
        String loginUrl = req.getContextPath() + "/auth";

        boolean isLoginUrl = req.getRequestURI().equals(loginUrl);
        boolean isHome = req.getRequestURI().equals(HOME);
        boolean isAssets = req.getRequestURI().contains(ASSETS_DIRECTORY);

        if (userSessionAuthorizer.isUserAuthorized() || isLoginUrl || isHome || isAssets) {
            chain.doFilter(req, res);
        } else {
            res.sendRedirect(HOME);
        }
    }


}