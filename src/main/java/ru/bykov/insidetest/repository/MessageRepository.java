package ru.bykov.insidetest.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bykov.insidetest.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("select m from Message m " +
            "JOIN User u on u.id = m.user.id " +
            "WHERE u.name = (:name) ")
    List<Message> findByNameLastMessageByLimitOfCount(@Param("name") String name, Pageable pageable);
}
