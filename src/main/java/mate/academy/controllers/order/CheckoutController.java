package mate.academy.controllers.order;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mate.academy.lib.Injector;
import mate.academy.model.ShoppingCart;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;

public class CheckoutController extends HttpServlet {
    private static final Long USER_ID = 1L;
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final OrderService orderService = (OrderService) injector
            .getInstance(OrderService.class);
    private static final ShoppingCartService shoppingCartService = (ShoppingCartService) injector
            .getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ShoppingCart shoppingCart = shoppingCartService.getByUserId(USER_ID);
        orderService.completeOrder(shoppingCart);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
