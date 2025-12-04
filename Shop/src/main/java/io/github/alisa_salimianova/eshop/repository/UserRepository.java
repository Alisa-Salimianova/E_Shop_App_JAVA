package io.github.alisa_salimianova.eshop.repository;

import io.github.alisa_salimianova.eshop.model.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class UserRepository {
    private final List<User> users;
    private final AtomicInteger idCounter;

    public UserRepository() {
        this.users = new ArrayList<>();
        this.idCounter = new AtomicInteger(1);
        initializeSampleUsers();
    }

    private void initializeSampleUsers() {
        save(new User(idCounter.getAndIncrement(), "Алиса Салимьянова", "alisa@example.com"));
        save(new User(idCounter.getAndIncrement(), "Иван Иванов", "ivan@example.com"));
        save(new User(idCounter.getAndIncrement(), "Мария Петрова", "maria@example.com"));
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public Optional<User> findById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    public void save(User user) {
        users.add(user);
    }
}