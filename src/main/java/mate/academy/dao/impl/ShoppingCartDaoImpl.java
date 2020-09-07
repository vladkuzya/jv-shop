package mate.academy.dao.impl;

import java.util.Optional;
import java.util.stream.IntStream;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.db.Storage;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        Storage.addShoppingCarts(shoppingCart);
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getById(Long cartId) {
        return Storage.shoppingCarts.stream()
                .filter(shoppingCart -> shoppingCart.getId().equals(cartId))
                .findFirst();
    }

    @Override
    public ShoppingCart update(ShoppingCart shoppingCart) {
        IntStream.range(0, Storage.shoppingCarts.size())
                .filter(i -> Storage.shoppingCarts.get(i).getId().equals(shoppingCart.getId()))
                .forEach(i -> Storage.shoppingCarts.set(i, shoppingCart));
        return shoppingCart;
    }

    @Override
    public boolean deleteById(Long cartId) {
        return Storage.shoppingCarts.removeIf(shoppingCart -> shoppingCart.getId().equals(cartId));
    }
}
