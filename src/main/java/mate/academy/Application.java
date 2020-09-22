package mate.academy;

import mate.academy.dao.ProductDao;
import mate.academy.lib.Injector;
import mate.academy.model.Product;

public class Application {
    private static Injector injector = Injector.getInstance("mate.academy");
    private static final ProductDao productDao = (ProductDao) injector
            .getInstance(ProductDao.class);

    public static void main(String[] args) {
        Product iphoneX = new Product("iphoneX", 700);
        Product iphone11 = new Product("iphone11", 950);
        Product iphone7 = new Product("iphone7", 400);
        Product iphone8 = new Product("iphone8", 500);
        productDao.create(iphoneX);
        productDao.create(iphone11);
        productDao.create(iphone7);
        productDao.create(iphone8);
        System.out.println(productDao.getAll());

        iphone8.setPrice(600);
        productDao.update(iphone8);
        System.out.println(productDao.getAll());

        System.out.println(productDao.getById(iphone7.getId()));

        productDao.delete(iphone11.getId());
        System.out.println(productDao.getAll());
        System.out.println();
    }
}
