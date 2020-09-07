package mate.academy.dao;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Product;

public interface ProductDao {
    Product create(Product product);

    Optional<Product> getById(Long productId);

    Product update(Product product);

    boolean deleteById(Long productId);

    List<Product> getAllProducts();
}
