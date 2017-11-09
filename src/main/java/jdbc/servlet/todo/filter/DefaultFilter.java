package jdbc.servlet.todo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "default", urlPatterns = {"/styles/*"})
public class DefaultFilter implements Filter {
    private RequestDispatcher requestDispatcher;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        requestDispatcher = filterConfig.getServletContext().getNamedDispatcher("default");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        requestDispatcher.forward(request, response);
    }

    @Override
    public void destroy() {

    }
}
