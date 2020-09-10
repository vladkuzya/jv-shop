package mate.academy.controllers.order;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mate.academy.lib.Injector;
import mate.academy.model.Order;
import mate.academy.service.OrderService;

public class GetAllOrdersForAdminController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final OrderService orderService = (OrderService) injector
            .getInstance(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Order> ordersList = orderService.getAll();
        req.setAttribute("orders", ordersList);
        req.getRequestDispatcher("/WEB-INF/views/orders/all_admin.jsp").forward(req, resp);
    }
}
