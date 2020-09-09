package mate.academy.dao;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Order;

public interface OrderDao {
    Order create(Order order);

    Optional<Order> getById(Long orderId);

    List<Order> getUserOrders(Long userId);

    Order update(Order order);

    boolean deleteById(Long orderId);
}
