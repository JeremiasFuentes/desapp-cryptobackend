package edu.api.model.repository;

import edu.api.model.entity.Transaction;
import edu.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAll();
    Optional<Transaction> findById(int id);
    List<Transaction> findAllByUserPublisher(User user);
    List<Transaction> findAllByUserClient(User user);
    boolean existsById(int id);
}