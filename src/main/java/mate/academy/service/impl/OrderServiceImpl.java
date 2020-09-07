package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.db.Storage;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = orderDao.create(shoppingCart);
        order.setProducts(new ArrayList<>(shoppingCart.getProducts()));
        shoppingCart.getProducts().removeAll(shoppingCart.getProducts());
        return order;
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderDao.getUserOrders(userId);
    }

    @Override
    public Order getById(Long orderId) {
        return orderDao.getById(orderId).get();
    }

    @Override
    public List<Order> getAll() {
        return Storage.orders;
    }

    @Override
    public boolean delete(Long orderId) {
        return orderDao.deleteById(orderId);
    }
}
