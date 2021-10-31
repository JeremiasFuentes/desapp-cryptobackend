package edu.api.model.repository;

import edu.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    Optional<User> findById(int id);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    List<User> findAll();

}
