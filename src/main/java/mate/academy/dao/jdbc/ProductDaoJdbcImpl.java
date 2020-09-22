package mate.academy.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.ProductDao;
import mate.academy.exceptions.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Product;
import mate.academy.util.ConnectionUtil;

@Dao
public class ProductDaoJdbcImpl implements ProductDao {
    @Override
    public Product create(Product item) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO products (name, price) VALUES (?,?)",
                            Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, item.getName());
            statement.setDouble(2, item.getPrice());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                item.setId(resultSet.getLong(1));
            }
            return item;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't create product " + item.getName(), ex);
        }
    }

    @Override
    public Optional<Product> getById(Long id) {
        Product product = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM products WHERE product_id = ?"
                            + " AND deleted = FALSE");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                product = getProductFromResultSet(resultSet);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't getting product by id" + id, ex);
        }
        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM products WHERE deleted = FALSE");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(getProductFromResultSet(resultSet));
            }
            return products;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't getting all products", ex);
        }
    }

    @Override
    public Product update(Product product) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE products SET name = ?, price = ?"
                            + "WHERE product_id = ? AND deleted = FALSE");
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setLong(3, product.getId());
            statement.executeUpdate();
            return product;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't create product" + product.getName(), ex);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE products SET deleted = TRUE WHERE product_id = ?");
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't delete product with id" + id, ex);
        }
    }

    private static Product getProductFromResultSet(ResultSet resultSet) throws SQLException {
        long productId = resultSet.getLong("product_id");
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        Product product = new Product(name, price);
        product.setId(productId);
        return product;
    }
}
