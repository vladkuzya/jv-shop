package mate.academy.controllers.cart;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mate.academy.controllers.LoginController;
import mate.academy.lib.Injector;
import mate.academy.service.ProductService;
import mate.academy.service.ShoppingCartService;

public class DeleteProductFromCartController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final ShoppingCartService shoppingCartService = (ShoppingCartService) injector
            .getInstance(ShoppingCartService.class);
    private static final ProductService productService = (ProductService) injector
            .getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long productId = Long.parseLong(req.getParameter("id"));
        Long userId = (Long) req.getSession().getAttribute(LoginController.USER_ID);
        shoppingCartService.deleteProduct(shoppingCartService.getByUserId(userId),
                productService.getById(productId));
        resp.sendRedirect(req.getContextPath() + "/shopping-carts/show");
    }
}
