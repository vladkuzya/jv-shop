package mate.academy.controllers.user;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mate.academy.lib.Injector;
import mate.academy.model.User;
import mate.academy.service.UserService;

public class GetAllUsersController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final UserService userService = (UserService) injector
            .getInstance(UserService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<User> allUsers = userService.getAll();
        req.setAttribute("users", allUsers);
        req.getRequestDispatcher("/WEB-INF/views/users/all.jsp").forward(req,resp);
    }
}
