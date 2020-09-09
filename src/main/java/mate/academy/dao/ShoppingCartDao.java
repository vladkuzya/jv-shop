package mate.academy.dao;

import java.util.Optional;
import mate.academy.model.ShoppingCart;

public interface ShoppingCartDao {
    ShoppingCart create(ShoppingCart shoppingCart);

    Optional<ShoppingCart> getById(Long cartId);

    Optional<ShoppingCart> getByUserId(Long userId);

    ShoppingCart update(ShoppingCart shoppingCart);

    boolean deleteById(Long cartId);
}
