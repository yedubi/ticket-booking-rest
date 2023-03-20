package org.booking.services;

import org.booking.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long userId);

    Optional<User> getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(long userId);


}
