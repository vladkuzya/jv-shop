package mate.academy.controllers.cart;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mate.academy.lib.Injector;
import mate.academy.model.Product;
import mate.academy.model.ShoppingCart;
import mate.academy.service.ProductService;
import mate.academy.service.ShoppingCartService;

public class ShoppingCartController extends HttpServlet {
    private static final Long USER_ID = 1L;
    private static Injector injector = Injector.getInstance("mate.academy");
    private static final ShoppingCartService shoppingCartService = (ShoppingCartService) injector
            .getInstance(ShoppingCartService.class);
    private static final ProductService productService = (ProductService) injector
            .getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ShoppingCart shoppingCart;
        List<Product> productList;
        try {
            shoppingCart = shoppingCartService.getByUserId(USER_ID);
            productList = shoppingCart.getProducts();
        } catch (NoSuchElementException e) {
            shoppingCart = new ShoppingCart(USER_ID);
            productList = shoppingCart.getProducts();
        }
        req.setAttribute("products", productList);
        req.getRequestDispatcher("/WEB-INF/views/shopping-carts/show.jsp").forward(req,resp);
    }
}
