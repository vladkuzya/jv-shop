package mate.academy.dao;

import mate.academy.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Product create(Product product);

    Optional<Product> getByID(Long productId);

    Product update(Product product);

    boolean deleteById(long productId);

    List<Product> getAllProducts();
}
