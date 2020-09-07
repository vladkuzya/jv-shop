package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.db.Storage;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;

    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order(shoppingCart.getUserId());
        order.setProducts(new ArrayList<>(shoppingCart.getProducts()));
        shoppingCartService.clear(shoppingCart);
        return orderDao.create(order);
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
