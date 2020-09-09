package mate.academy.controllers.cart;

import java.io.IOException;
import java.util.NoSuchElementException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mate.academy.lib.Injector;
import mate.academy.model.ShoppingCart;
import mate.academy.service.ProductService;
import mate.academy.service.ShoppingCartService;

public class AddToCartController extends HttpServlet {
    private static final Long USER_ID = 1L;
    private static Injector injector = Injector.getInstance("mate.academy");
    private static final ShoppingCartService shoppingCartService = (ShoppingCartService) injector
            .getInstance(ShoppingCartService.class);
    private static final ProductService productService = (ProductService) injector
            .getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        try {
            ShoppingCart shoppingCart = shoppingCartService.getByUserId(USER_ID);
            shoppingCartService.addProduct(shoppingCart, productService.getById(id));
        } catch (NoSuchElementException e) {
            ShoppingCart shoppingCart = new ShoppingCart(USER_ID);
            shoppingCartService.create(shoppingCart);
            shoppingCartService.addProduct(shoppingCart, productService.getById(id));
        }
        resp.sendRedirect(req.getContextPath() + "/products/all");
    }
}
