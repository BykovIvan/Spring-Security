package ru.bykov.insidetest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bykov.insidetest.model.User;

@Repository
public interface UserRepository extends JpaRepository<Long, User> {
}
