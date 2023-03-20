package org.booking.services.impl;

import lombok.AllArgsConstructor;
import org.booking.model.User;
import org.booking.repository.UserRepository;
import org.booking.services.UserService;
import org.booking.utils.ListPaginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository repository;
    @Override
    public Optional getUserById(long userId) {
        return repository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return getAllUsers().stream()
                .filter(e -> email.equalsIgnoreCase(e.getEmail()))
                .findFirst();
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        Predicate<User> userPredicate = e -> name.equalsIgnoreCase(e.getName());
        return getFilteredUsers(userPredicate, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        repository.save(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        return (User) repository.save(user);
    }

    @Override
    public void deleteUser(long userId) {
        repository.deleteById(userId);
    }

    private List<User> getAllUsers() {
        return (List<User>) repository.findAll();
    }

    private List<User> getFilteredUsers(Predicate<User> predicate, int pageSize, int pageNum) {
        List<User> sourceList = getAllUsers().stream()
                .filter(predicate)
                .collect(Collectors.toList());
        return (List<User>) ListPaginator.getPageList(sourceList, pageSize, pageNum);
    }

}
