package mate.academy.controllers.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mate.academy.lib.Injector;
import mate.academy.service.UserService;

public class DeleteUserController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final UserService userService = (UserService) injector
            .getInstance(UserService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        userService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/users/all");
    }
}
