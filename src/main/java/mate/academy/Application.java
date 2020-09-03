package mate.academy;

import mate.academy.lib.Injector;
import mate.academy.model.Product;
import mate.academy.service.ProductService;

public class Application {
    private static final long ID_GET = 2L;
    private static final long ID_DELETE = 1L;

    private static Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);
        productService.create(new Product("iphoneX", 700));
        productService.create(new Product("iphone 11", 950));
        productService.create(new Product("iphone 7", 400));
        Product iphone8 = new Product("iphone 8", 500);
        productService.create(iphone8);
        System.out.println(productService.getAll());

        iphone8.setPrice(400);
        productService.update(iphone8);
        System.out.println(productService.getAll());

        System.out.println(productService.get(ID_GET));

        productService.delete(ID_DELETE);
        System.out.println(productService.getAll());
    }
}
