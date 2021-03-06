package mate.academy.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exceptions.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Product;
import mate.academy.model.ShoppingCart;
import mate.academy.util.ConnectionUtil;

@Dao
public class ShoppingCartDaoJdbcImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        String query = "INSERT INTO shopping_carts (user_id) VALUES (?)";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, shoppingCart.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                shoppingCart.setId(resultSet.getLong(1));
            }
            addProductsToDB(connection, shoppingCart);
            return shoppingCart;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't create shopping cart with id "
                    + shoppingCart.getId(), ex);
        }
    }

    @Override
    public Optional<ShoppingCart> getById(Long id) {
        ShoppingCart shoppingCart = null;
        List<Product> products = getProductsFromShoppingCart(id);
        String query = "SELECT * FROM shopping_carts WHERE cart_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                shoppingCart = getCartFromResultSet(resultSet);
                shoppingCart.setProducts(products);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't get shopping cart by user id" + id, ex);
        }
        return Optional.ofNullable(shoppingCart);
    }

    @Override
    public Optional<ShoppingCart> getByUserId(Long userId) {
        ShoppingCart shoppingCart = null;
        String query = "SELECT * FROM shopping_carts WHERE user_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                shoppingCart = getCartFromResultSet(resultSet);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't get cart by user id" + userId, ex);
        }
        if (shoppingCart != null) {
            shoppingCart.setProducts(getProductsFromShoppingCart(shoppingCart.getId()));
        }
        return Optional.ofNullable(shoppingCart);
    }

    @Override
    public List<ShoppingCart> getAll() {
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        String query = "SELECT * FROM shopping_carts sc "
                + "INNER JOIN shopping_carts_products scp ON sc.cart_id = scp.cart_id "
                + "INNER JOIN products p ON p.product_id = scp.product_id "
                + "WHERE sc.deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                shoppingCarts.add(getCartFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't get all shopping carts", ex);
        }
        for (ShoppingCart cart : shoppingCarts) {
            cart.setProducts(getProductsFromShoppingCart(cart.getId()));
        }
        return shoppingCarts;
    }

    @Override
    public ShoppingCart update(ShoppingCart shoppingCart) {
        String query = "UPDATE shopping_carts SET user_id = ? WHERE cart_id = ? "
                + "AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            deleteProductsFromDB(connection, shoppingCart.getId());
            addProductsToDB(connection, shoppingCart);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, shoppingCart.getUserId());
            statement.setLong(2, shoppingCart.getId());
            statement.executeUpdate();
            return shoppingCart;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update shopping cart", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE shopping_carts SET user_id = ? "
                + "WHERE cart_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't delete cart with id" + id, ex);
        }
    }

    private void addProductsToDB(Connection connection,
                                 ShoppingCart shoppingCart) {
        String query = "INSERT INTO shopping_carts_products "
                + "(cart_id, product_id) VALUES (?, ?)";
        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {
            statement.setLong(1, shoppingCart.getId());
            for (Product product : shoppingCart.getProducts()) {
                statement.setLong(2, product.getId());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't add products to cart with id"
                    + shoppingCart.getId(), ex);
        }
    }

    private void deleteProductsFromDB(Connection connection,
                                      Long id) throws SQLException {
        String query = "DELETE FROM shopping_carts_products "
                + "WHERE cart_id = ?";
        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't delete products from cart with id"
                    + id, ex);
        }
    }

    private ShoppingCart getCartFromResultSet(ResultSet resultSet) throws SQLException {
        long cartId = resultSet.getLong("cart_id");
        long userId = resultSet.getLong("user_id");
        ShoppingCart shoppingCart = new ShoppingCart(userId);
        shoppingCart.setId(cartId);
        return shoppingCart;
    }

    private List<Product> getProductsFromShoppingCart(long cartId) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products p "
                + "INNER JOIN shopping_carts_products scp ON p.product_id = scp.product_id "
                + "WHERE cart_id = ?";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, cartId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long productId = resultSet.getLong("product_id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                products.add(new Product(productId, name, price));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get product from cart with id " + cartId, e);
        }
        return products;
    }
}

