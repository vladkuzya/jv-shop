package mate.academy.controllers.order;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mate.academy.lib.Injector;
import mate.academy.service.OrderService;

public class DeleteOrderController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final OrderService orderService = (OrderService) injector
            .getInstance(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        orderService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/admin/orders/");
    }
}
