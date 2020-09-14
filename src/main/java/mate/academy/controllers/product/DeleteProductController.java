package mate.academy.controllers.product;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mate.academy.lib.Injector;
import mate.academy.service.ProductService;

public class DeleteProductController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final ProductService productService = (ProductService) injector
            .getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        productService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/products/all-admin");
    }
}
