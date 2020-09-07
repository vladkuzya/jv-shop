package mate.academy.service.impl;

import mate.academy.dao.ProductDao;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Product;
import mate.academy.model.ShoppingCart;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Inject
    private ProductDao productDao;

    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        return shoppingCartDao.create(shoppingCart);
    }

    @Override
    public ShoppingCart addProduct(ShoppingCart shoppingCart, Product product) {
        shoppingCart.getProducts().add(productDao.getById(product.getId()).get());
        return shoppingCart;
    }

    @Override
    public boolean deleteProduct(ShoppingCart shoppingCart, Product product) {
        return shoppingCart.getProducts().remove(product);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getProducts().clear();
    }

    @Override
    public ShoppingCart getByUserId(Long userId) {
        return shoppingCartDao.getById(userId).get();
    }

    @Override
    public boolean delete(Long cartId) {
        return shoppingCartDao.deleteById(cartId);
    }
}
