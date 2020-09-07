package mate.academy.dao;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;

public interface OrderDao {
    Order create(ShoppingCart shoppingCart);

    Optional<Order> getById(Long orderId);

    List<Order> getUserOrders(Long userId);

    Order update(Order order);

    boolean deleteById(Long orderId);
}
