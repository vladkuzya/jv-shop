package mate.academy.controllers;

import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mate.academy.lib.Injector;
import mate.academy.model.Role;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class RegistrationController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final UserService userService = (UserService) injector
            .getInstance(UserService.class);
    private static final ShoppingCartService shoppingCartService = (ShoppingCartService) injector
            .getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/users/registration.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("psw");
        String passwordRepeat = req.getParameter("psw-repeat");

        if (password.equals(passwordRepeat)) {
            User user = new User(login, password);
            user.setRoles(Set.of(Role.of("USER")));
            userService.create(user);
            shoppingCartService.create(new ShoppingCart(user.getId()));
            resp.sendRedirect(req.getContextPath() + "/");
        } else {
            req.setAttribute("message", "Your password and repeat password aren't the same");
            req.setAttribute("login", login);
            req.getRequestDispatcher("/WEB-INF/views/users/registration.jsp").forward(req,resp);
        }
    }
}
