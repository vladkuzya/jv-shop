package mate.academy.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mate.academy.lib.Injector;
import mate.academy.service.UserService;

public class AuthenticationFilter implements Filter {
    private static final String USER_ID = "user_id";
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final UserService userService = (UserService) injector
            .getInstance(UserService.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String url = req.getServletPath();
        if (url.equals("/users/login") || url.equals("/users/registration")) {
            chain.doFilter(req, resp);
            return;
        }

        Long userId = (Long) req.getSession().getAttribute(USER_ID);
        if (userId == null || userService.getById(userId) == null) {
            resp.sendRedirect(req.getContextPath() + "/users/login");
            return;
        }

        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}
