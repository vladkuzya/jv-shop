package mate.academy.dao.impl;

import mate.academy.dao.ProductDao;
import mate.academy.db.Storage;
import mate.academy.lib.Dao;
import mate.academy.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Dao
public class ProductDaoImpl implements ProductDao {

    @Override
    public Product create(Product product) {
        Storage.addProduct(product);
        return product;
    }

    @Override
    public Optional<Product> getByID(Long productId) {
        return Storage.products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst();
    }

    @Override
    public Product update(Product product) {
        IntStream.range(0, Storage.products.size())
                .filter(i -> Storage.products.get(i).getId().equals(product.getId()))
                .forEach(i -> Storage.products.set(i, product));
        return product;
    }

    @Override
    public boolean deleteById(long productId) {
        return Storage.products.remove(getByID(productId).get());
    }

    @Override
    public List<Product> getAllProducts() {
        return Storage.products;
    }
}
