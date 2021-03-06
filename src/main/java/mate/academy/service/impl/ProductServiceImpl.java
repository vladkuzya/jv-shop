package mate.academy.service.impl;

import java.util.List;
import mate.academy.dao.ProductDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Product;
import mate.academy.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    @Inject
    private ProductDao productDao;

    @Override
    public Product create(Product product) {
        return productDao.create(product);
    }

    @Override
    public Product getById(Long id) {
        return productDao.getById(id).get();
    }

    @Override
    public List<Product> getAll() {
        return productDao.getAll();
    }

    @Override
    public Product update(Product product) {
        return productDao.update(product);
    }

    @Override
    public boolean delete(Long id) {
        return productDao.delete(id);
    }
}
