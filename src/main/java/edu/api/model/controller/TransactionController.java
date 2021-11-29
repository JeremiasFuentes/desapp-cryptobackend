package edu.api.model.controller;

import edu.api.model.dto.Crypto;
import edu.api.model.dto.Message;
import edu.api.model.dto.NewTransaction;
import edu.api.model.entity.Publication;
import edu.api.model.entity.Transaction;
import edu.api.model.entity.User;
import edu.api.model.service.UserService;
import edu.api.model.service.CryptoService;
import edu.api.model.service.PublicationService;
import edu.api.model.service.TransactionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("auth/transaction")
@CrossOrigin
@Log4j2
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    PublicationService publicationService;

    @Autowired
    UserService userService;

    @Autowired
    CryptoService cryptoService;

    @PostMapping("/addT/{id}")
    public ResponseEntity<?> newTransaction(@Valid @RequestBody NewTransaction newTransaction, BindingResult bindingResult,@PathVariable int id){
        long start = System.currentTimeMillis();
        if(bindingResult.hasErrors()) {
            log.error("invalid mail or wrong information");
            return new ResponseEntity(new Message("invalid mail or wrong information"), HttpStatus.BAD_REQUEST);
        }
        Crypto crypto = cryptoService.getCryptFromApi(newTransaction.getCryptoName());
        Publication publication = publicationService.getByID(id).get();
        if(publication.getAmountOfCrypto() < newTransaction.getAmountOfCrypto()){
            log.error("amount higher than the transaction");
            return new ResponseEntity(new Message("amount higher than the transaction"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.getByUserName(newTransaction.getUserNamePublisher()).get();
        User user2 = userService.getByUserName(newTransaction.getUserNameClient()).get();
        Transaction transaction = new Transaction(LocalDateTime.now(), user,user2,newTransaction.getCryptoName(),
                crypto.getPrice(),publication.getAmountOfCrypto() , publication.getPriceTotalInPesos(), newTransaction.getAmountOfCrypto(), newTransaction.getType(), publication);
        publication.setAmountOfCrypto(publication.getAmountOfCrypto() - transaction.getAmountOfCryptoToBuy());
        publicationService.save(publication);
        transactionService.save(transaction);
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("Transiction created in " +elapsedTimeMillis+ " ms");
        if(newTransaction.getType() == "compra"){

            return new ResponseEntity(new Message(user.getWallet()), HttpStatus.CREATED);
        }else{

            return new ResponseEntity(new Message(user.getCvu()), HttpStatus.CREATED);
        }

    }


    @GetMapping("/{userName}/all")
    public ResponseEntity<?> getUserTransactions(@PathVariable String userName){
        long start = System.currentTimeMillis();
        User user = userService.getByUserName(userName).get();
        List<Transaction> transactions = transactionService.getAllByUserPublisher(user);
        List<Transaction> transactions2 = transactionService.getAllByUserClient(user);
        List<Transaction> newList = new ArrayList<Transaction>(transactions);
        newList.addAll(transactions2);
        if (newList == null){
            log.info("the user not have transactions");
            return new ResponseEntity(new Message("the user not have transactions"), HttpStatus.BAD_REQUEST);
        }
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("Transictions returns in " +elapsedTimeMillis+ " ms");
        return new ResponseEntity<List<Transaction>>(newList,HttpStatus.OK);
    }

    @PostMapping("/{userName}/confirmReception/{id}")
    public ResponseEntity<?> confirmReceptionTransaction(@PathVariable String userName,@PathVariable int id){
        long start = System.currentTimeMillis();
        if(!transactionService.existsById(id)){
            log.error("Transaction dont exist");
            return new ResponseEntity(new Message("Transactions dont exist"), HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = transactionService.getByID(id).get();
        if(transaction.isClosed()){
            log.error("Transaction Closed");
            return new ResponseEntity(new Message("Transaction closed"), HttpStatus.BAD_REQUEST);
        }
        transaction.setConfirm(true);
        if (transaction.isConfirm() && transaction.isTransfer()) {
            this.calculateClose(transaction);
            transaction.setClosed(true);

        }
        transactionService.save(transaction);
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("Confirm reception of Transaction in " +elapsedTimeMillis + " ms");
        return new ResponseEntity<List<Transaction>>(HttpStatus.OK);
    }

    private void calculateClose(Transaction transaction) {
        long start = System.currentTimeMillis();
        User user = transaction.getUserPublisher();
        User user2 = transaction.getUserClient();
        if(TimeUnit.MILLISECONDS.toMinutes(LocalDateTime.now().compareTo(transaction.getDate())) <= 30){
            user.setPoints(user.getPoints() + 10);
            user2.setPoints(user2.getPoints() + 10);
        }else{
            user.setPoints(user.getPoints() + 5);
            user2.setPoints(user2.getPoints() + 5);
        }
        user.setCantTrxFinished(user.getCantTrxFinished()+1);
        user2.setCantTrxFinished(user2.getCantTrxFinished()+1);
        user.recalculateReputation();
        user2.recalculateReputation();
        userService.save(user);
        userService.save(user2);
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("Transaction Closed in "+elapsedTimeMillis+ " ms");
    }

    @PostMapping("/{userName}/confirmTransaction/{id}")
    public ResponseEntity<?> confirmTransaction(@PathVariable String userName,@PathVariable int id){
        long start = System.currentTimeMillis();
        if(!transactionService.existsById(id)){
            log.error("Transaction dont exist");
            return new ResponseEntity(new Message("Transaction dont exist"), HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = transactionService.getByID(id).get();
        if(transaction.isClosed()){
            log.error("Transaction Closed");
            return new ResponseEntity(new Message("Transaction closed"), HttpStatus.BAD_REQUEST);
        }
        transaction.setTransfer(true);
        if (transaction.isConfirm() && transaction.isTransfer()) {
            transaction.setClosed(true);
            this.calculateClose(transaction);
        }
        transactionService.save(transaction);
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("Confirm Transaction in " + elapsedTimeMillis+" ms");
        return new ResponseEntity<List<Transaction>>(HttpStatus.OK);
    }

    @PostMapping("/{userName}/cancelTransaction/{id}")
    public ResponseEntity<?> cancelTransaction(@PathVariable String userName,@PathVariable int id){
        long start = System.currentTimeMillis();
        if(!transactionService.existsById(id)){
            log.error("Transaction dont exist");
            return new ResponseEntity(new Message("Transaction dont exist"), HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = transactionService.getByID(id).get();
        if(transaction.isClosed()){
            log.error("Transaction Closed");
            return new ResponseEntity(new Message("Transaction closed"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.getByUserName(userName).get();
        if(user.getPoints() <= 20){
            user.setPoints(0);
        }else{
            user.setPoints(user.getPoints()- 20);
        }
        user.recalculateReputation();
        userService.save(user);
        transactionService.delete(transaction);
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("Transaction Canceled in "+elapsedTimeMillis+" ms");
        return new ResponseEntity<List<Transaction>>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransactions(){
        long start = System.currentTimeMillis();
        List<Transaction> transactions = transactionService.getAll();
        if (transactions == null){
            log.info("Transactions empty");
            return new ResponseEntity(new Message("Transactions empty"), HttpStatus.BAD_REQUEST);
        }
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("Transactions return in "+elapsedTimeMillis+" ms");
        return new ResponseEntity<List<Transaction>>(transactions,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable int id){
        long start = System.currentTimeMillis();
        if(!transactionService.existsById(id)){
            log.error("Transaction dont exist");
            return new ResponseEntity(new Message("Transaction dont exist"), HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = transactionService.getByID(id).get();
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("Transaction returned in "+elapsedTimeMillis+" ms" );
        return new ResponseEntity<Transaction>(transaction,HttpStatus.OK);
    }

}
