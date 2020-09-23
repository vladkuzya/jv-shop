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
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO orders (user_id) VALUES (?);",
                            Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, order.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                order.setId(resultSet.getLong(1));
            }
            addProductsToDB(connection, order);
            return order;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't create shopping cart with id "
                    + order.getId(), ex);
        }
    }

    @Override
    public Optional<Order> getById(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM orders o"
                            + "INNER JOIN orders_products op ON op.order_id = o.order_id"
                            + "INNER JOIN products p ON p.product_id = op.product_id"
                            + "WHERE o.order_id = ? AND o.deleted = FALSE");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getOrderFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't getting shopping cart by user id" + id, ex);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM orders "
                            + "WHERE user_id = ? AND deleted = FALSE");
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't getting order by user id" + userId, ex);
        }
        return orders;
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM orders WHERE deleted = FALSE");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
            return orders;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't getting all orders", ex);
        }
    }

    @Override
    public Order update(Order order) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            deleteProductsFromDB(connection, order.getId());
            addProductsToDB(connection, order);
            PreparedStatement statement = connection.prepareStatement("UPDATE orders"
                    + "SET user_id = ?\n"
                    + "WHERE user_id = ? AND deleted = FALSE");
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
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE orders SET deleted = TRUE WHERE order_id = ?");
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't delete order with id" + id, ex);
        }
    }

    private static List<Product> getProductsFromOrder(long orderId) {
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM products p\n"
                    + "INNER JOIN orders_products op ON p.product_id = op.product_id\n"
                    + "WHERE order_id = ?;");
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
                                 Order order) throws SQLException {
        PreparedStatement statement = connection
                .prepareStatement("INSERT INTO orders_products VALUES (?, ?)");
        statement.setLong(1, order.getId());
        for (Product product : order.getProducts()) {
            statement.setLong(2, product.getId());
            statement.executeUpdate();
        }
    }

    private void deleteProductsFromDB(Connection connection,
                                      Long id) throws SQLException {
        PreparedStatement statement = connection
                .prepareStatement("DELETE FROM orders_products WHERE order_id = ?");
        statement.setLong(1, id);
        statement.executeUpdate();
    }

    private static Order getOrderFromResultSet(ResultSet resultSet) throws SQLException {
        long orderId = resultSet.getLong("order_id");
        long userId = resultSet.getLong("user_id");
        Order order = new Order(userId);
        order.setId(orderId);
        order.setProducts(getProductsFromOrder(orderId));
        return order;
    }
}
