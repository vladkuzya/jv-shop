package mate.academy.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.exceptions.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.util.ConnectionUtil;

@Dao
public class UserDaoJdbcImpl implements UserDao {
    @Override
    public User create(User user) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO users (login, password) VALUES (?,?)",
                            Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                user.setId(resultSet.getLong(1));
            }
            statement.close();
            return addUserRoles(connection, user);
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't create user " + user.getLogin(), ex);
        }
    }

    @Override
    public Optional<User> getById(Long id) {
        User user = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM users WHERE user_id = ?"
                            + " AND deleted = FALSE");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = getUserFromResultSet(resultSet);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't get user by id" + id, ex);
        }
        user.setRoles(getRolesOfUser(user.getId()));
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        User user = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM users WHERE login = ?"
                            + " AND deleted = FALSE");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = getUserFromResultSet(resultSet);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't get user by login" + login, ex);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM users WHERE deleted = FALSE");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet));
            }
            return users;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't get all users", ex);
        }
    }

    @Override
    public User update(User user) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("UPDATE users SET login = ?, password = ? "
                            + "WHERE user_id = ? AND deleted = FALSE");
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setLong(3, user.getId());
            statement.executeUpdate();
            deleteUserRoles(connection, user.getId());
            addUserRoles(connection, user);
            return user;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't update user" + user.getLogin(), ex);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("UPDATE users "
                            + "SET deleted = TRUE WHERE user_id = ?");
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't delete user with id" + id, ex);
        }
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        long userId = resultSet.getLong("user_id");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        User user = new User(login, password);
        user.setId(userId);
        return user;
    }

    private User addUserRoles(Connection connection, User user) {
        try (PreparedStatement statement =
                     connection.prepareStatement("INSERT INTO users_roles (user_id, role_id) "
                             + "VALUES (?,?)")) {
            statement.setLong(1, user.getId());
            for (Role role : getRolesOfUser(user.getId())) {
                statement.setLong(2, getRoleIdByName(role.getRoleName()));
                statement.executeUpdate();
            }
            user.setRoles(getRolesOfUser(user.getId()));
            return user;
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't add role to user with id"
                    + user.getId(), ex);
        }
    }

    private Set<Role> getRolesOfUser(Long userId) {
        String query = "SELECT role_id, role_name FROM users_roles JOIN roles "
                + "USING (role_id) WHERE user_id = ?";
        Set<Role> roles = new HashSet<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Long roleId = rs.getLong("role_id");
                String roleName = rs.getString("role_name");
                Role role = Role.of(roleName);
                role.setId(roleId);
                roles.add(role);
            }
            return roles;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get user with ID = " + userId, e);
        }
    }

    private void deleteUserRoles(Connection connection,
                                 Long id) throws SQLException {
        try (PreparedStatement statement =
                     connection.prepareStatement("DELETE FROM users_roles WHERE user_id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataProcessingException("Couldn't delete role from user with id"
                    + id, ex);
        }
    }

    private Long getRoleIdByName(Role.RoleName roleName) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM roles "
                            + "WHERE role_name = ?");
            statement.setString(1, roleName.name());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getLong("role_id");
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't get roleId with name = "
                    + roleName.name(), ex);
        }
        return 1L;
    }
}
