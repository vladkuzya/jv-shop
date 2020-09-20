package mate.academy.controllers;

import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mate.academy.lib.Injector;
import mate.academy.model.Product;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.ProductService;
import mate.academy.service.UserService;

public class InjectDataController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final UserService userService = (UserService) injector
            .getInstance(UserService.class);
    private static final ProductService productService = (ProductService) injector
            .getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User bob = new User("bob", "1");
        bob.setRoles(Set.of(Role.of("USER")));
        User alice = new User("alice", "2");
        alice.setRoles(Set.of(Role.of("USER")));
        User admin = new User("admin", "x");
        admin.setRoles(Set.of(Role.of("ADMIN")));

        userService.create(bob);
        userService.create(alice);
        userService.create(admin);
        productService.create(new Product("iphoneX", 1000));
        productService.create(new Product("iphone11", 1200));
        productService.create(new Product("iphone8", 700));

        req.getRequestDispatcher("WEB-INF/views/injectData.jsp").forward(req,resp);
    }
}
