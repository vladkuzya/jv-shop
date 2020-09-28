package mate.academy.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.OrderDao;
import mate.academy.exceptions.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.Product;
import mate.academy.util.ConnectionUtil;

@Dao
public class OrderDaoJdbcImpl implements OrderDao {
    @Override
    public Order create(Order order) {
        String query = "INSERT INTO orders (user_id) VALUES (?)";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, order.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                order.setId(resultSet.getLong(1));
            }
            statement.close();
            addProductsToDB(connection, order);
            return order;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't create order with id "
                    + order.getId(), ex);
        }
    }

    @Override
    public Optional<Order> getById(Long id) {
        Order order = new Order(id);
        List<Product> products = getProductsFromOrder(id);
        String query = "SELECT * FROM orders "
                + "WHERE order_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                order = getOrderFromResultSet(resultSet);
                order.setProducts(products);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't get order by id" + id, ex);
        }
        return Optional.ofNullable(order);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders "
                + "WHERE user_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't get order by user id" + userId, ex);
        }
        for (Order order : orders) {
            order.setProducts(getProductsFromOrder(order.getId()));
        }
        return orders;
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't get all orders", ex);
        }
        for (Order order : orders) {
            order.setProducts(getProductsFromOrder(order.getId()));
        }
        return orders;
    }

    @Override
    public Order update(Order order) {
        String query = "UPDATE orders SET user_id = ? "
                + "WHERE order_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            deleteProductsFromDB(connection, order.getId());
            addProductsToDB(connection, order);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, order.getUserId());
            statement.setLong(2, order.getId());
            statement.executeUpdate();
            return order;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update order", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE orders SET deleted = TRUE WHERE order_id = ?";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't delete order with id" + id, ex);
        }
    }

    private List<Product> getProductsFromOrder(long orderId) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products p "
                + "INNER JOIN orders_products op ON p.product_id = op.product_id "
                + "WHERE order_id = ?";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long productId = resultSet.getLong("product_id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                products.add(new Product(productId, name, price));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get product from order with id " + orderId, e);
        }
        return products;
    }

    private void addProductsToDB(Connection connection,
                                 Order order) {
        String query = "INSERT INTO orders_products VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, order.getId());
            for (Product product : order.getProducts()) {
                statement.setLong(2, product.getId());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't add products to order with id"
                    + order.getId(), ex);
        }
    }

    private void deleteProductsFromDB(Connection connection,
                                      Long id) throws SQLException {
        String query = "DELETE FROM orders_products WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException {
        long orderId = resultSet.getLong("order_id");
        long userId = resultSet.getLong("user_id");
        Order order = new Order(userId);
        order.setId(orderId);
        return order;
    }
}
