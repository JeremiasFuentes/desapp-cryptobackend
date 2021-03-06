package edu.api.model.service;

import edu.api.model.entity.Transaction;
import edu.api.model.repository.TransactionRepository;
import edu.api.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public void delete(Transaction transaction){
        transactionRepository.delete(transaction);
    }

    public void save(Transaction transaction){
        transactionRepository.save(transaction);
    }

    public List<Transaction> getAll(){
        return transactionRepository.findAll();
    }


    public Optional<Transaction> getByID (int id){return transactionRepository.findById(id);}

    public List<Transaction> getAllByUserPublisher(User user){
        return transactionRepository.findAllByUserPublisher(user);
    }

    public List<Transaction> getAllByUserClient(User user){
        return transactionRepository.findAllByUserClient(user);
    }

    public boolean existsById(int id){
        return transactionRepository.existsById(id);
    }
}
