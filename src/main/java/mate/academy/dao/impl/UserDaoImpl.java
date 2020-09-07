package mate.academy.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import mate.academy.dao.UserDao;
import mate.academy.db.Storage;
import mate.academy.lib.Dao;
import mate.academy.model.User;

@Dao
public class UserDaoImpl implements UserDao {
    @Override
    public User create(User user) {
        Storage.addUser(user);
        return user;
    }

    @Override
    public Optional<User> getById(Long userId) {
        return Storage.users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }

    @Override
    public List<User> getAll() {
        return Storage.users;
    }

    @Override
    public User update(User user) {
        IntStream.range(0, Storage.users.size())
                .filter(i -> Storage.users.get(i).getId().equals(user.getId()))
                .forEach(i -> Storage.users.set(i, user));
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return Storage.users.removeIf(user -> user.getId().equals(id));
    }
}
