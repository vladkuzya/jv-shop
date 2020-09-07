package mate.academy;

import mate.academy.lib.Injector;
import mate.academy.model.Product;
import mate.academy.service.ProductService;

public class Application {
    private static Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);
        Product iphoneX = new Product("iphoneX", 700);
        Product iphone11 = new Product("iphone1", 950);
        Product iphone7 = new Product("iphone7", 400);
        Product iphone8 = new Product("iphone8", 500);
        productService.create(iphoneX);
        productService.create(iphone11);
        productService.create(iphone7);
        productService.create(iphone8);
        System.out.println(productService.getAll());

        iphone8.setPrice(400);
        productService.update(iphone8);
        System.out.println(productService.getAll());

        System.out.println(productService.get(iphone7.getId()));

        productService.delete(iphone11.getId());
        System.out.println(productService.getAll());
    }
}
