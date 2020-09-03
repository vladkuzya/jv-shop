package mate.academy;

import mate.academy.lib.Injector;
import mate.academy.model.Product;
import mate.academy.service.ProductService;

public class Application {
    private static final long ID_UPDATE = 3L;
    private static final long ID_GET = 2L;
    private static final long ID_DELETE = 1L;

    private static Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);
        productService.create(new Product("iphoneX", 20000));
        productService.create(new Product("iphone 11", 25000));
        productService.create(new Product("iphone 7", 10000));
        Product iphone8 = new Product("iphone 8", 10000);
        iphone8.setId(ID_UPDATE);

        productService.update(iphone8);
        System.out.println(productService.get(ID_GET));

        productService.delete(ID_DELETE);
        System.out.println(productService.getAll());

    }
}
