package edu.api.model.repository;

import edu.api.model.entity.Publication;
import edu.api.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Integer> {
    List<Publication> findAll();
    Optional<Publication> findById(int id);
    List<Publication> findByUserNameSeller(String userName);
}