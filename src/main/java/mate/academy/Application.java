package mate.academy;

import mate.academy.lib.Injector;
import mate.academy.model.Product;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import mate.academy.service.ProductService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Application {
    private static Injector injector = Injector.getInstance("mate.academy");
    private static final ProductService productService = (ProductService) injector
            .getInstance(ProductService.class);
    private static final ShoppingCartService shoppingCartService = (ShoppingCartService) injector
            .getInstance(ShoppingCartService.class);
    private static final UserService userService = (UserService) injector
            .getInstance(UserService.class);
    private static final OrderService orderService = (OrderService) injector
            .getInstance(OrderService.class);

    public static void main(String[] args) {
        Product iphoneX = new Product("iphoneX", 700);
        Product iphone11 = new Product("iphone1", 950);
        Product iphone7 = new Product("iphone7", 400);
        Product iphone8 = new Product("iphone8", 500);
        productService.create(iphoneX);
        productService.create(iphone11);
        productService.create(iphone7);
        productService.create(iphone8);
        System.out.println(productService.getAll());

        iphone8.setPrice(600);
        productService.update(iphone8);
        System.out.println(productService.getAll());

        System.out.println(productService.getById(iphone7.getId()));

        productService.delete(iphone11.getId());
        System.out.println(productService.getAll());
        System.out.println();

        User vlad = new User("Vlad", "kuzya");
        User roman = new User("Roman", "rome");
        User sasha = new User("Sasha", "sanek");

        userService.create(vlad);
        userService.create(roman);
        userService.create(sasha);
        System.out.println(userService.getAll());

        vlad.setPassword("0110");
        userService.update(vlad);
        System.out.println(userService.getById(vlad.getId()));

        userService.delete(sasha.getId());
        System.out.println(userService.getAll());
        System.out.println();

        ShoppingCart shoppingCartVlad = new ShoppingCart(vlad.getId());
        ShoppingCart shoppingCartRoman = new ShoppingCart(roman.getId());
        shoppingCartService.create(shoppingCartVlad);
        shoppingCartService.create(shoppingCartRoman);
        shoppingCartService.addProduct(shoppingCartVlad, iphone7);
        shoppingCartService.addProduct(shoppingCartVlad, iphoneX);
        shoppingCartService.addProduct(shoppingCartRoman, iphone8);
        System.out.println(shoppingCartService.getById(vlad.getId()));
        System.out.println(shoppingCartService.getById(roman.getId()));

        shoppingCartService.deleteProduct(shoppingCartVlad, iphone7);
        System.out.println(shoppingCartService.getById(vlad.getId()));

        shoppingCartService.clear(shoppingCartRoman);
        System.out.println(shoppingCartService.getByUserId(roman.getId()));

        shoppingCartService.delete(roman.getId());
        System.out.println();

        orderService.completeOrder(shoppingCartService.getById(vlad.getId()));
        System.out.println(orderService.getUserOrders(vlad.getId()));

        System.out.println(orderService.getAll());
    }
}
