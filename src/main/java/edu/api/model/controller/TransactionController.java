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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("auth/transaction")
@CrossOrigin
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
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("invalid mail or wrong information"), HttpStatus.BAD_REQUEST);
        Crypto crypto = cryptoService.getCryptFromApi(newTransaction.getCryptoName());
        Publication publication = publicationService.getByID(id).get();
        User userPublisher = userService.getByUserName(newTransaction.getUserNamePublisher()).get();
        User userClient = userService.getByUserName(newTransaction.getUserNameClient()).get();
        Transaction transaction = new Transaction(LocalDateTime.now(), newTransaction.getUserNamePublisher(), userPublisher, userClient, newTransaction.getCryptoName(),
                crypto.getPrice(), publication.getAmountOfCrypto(), publication.getPriceTotalInPesos(), newTransaction.getAmountOfCrypto(), newTransaction.getType(), publication);
        //publication.setAmountOfCrypto(publication.getAmountOfCrypto() - transaction.getAmountOfCryptoToBuy());
        //publicationService.save(publication);
        transactionService.save(transaction);
        if(newTransaction.getType() == "compra"){

            return new ResponseEntity(new Message(userPublisher.getWallet()), HttpStatus.CREATED);
        }else{

            return new ResponseEntity(new Message(userPublisher.getCvu()), HttpStatus.CREATED);
        }

    }


    @GetMapping("/{userName}/all")
    public ResponseEntity<?> getUserTransactions(@PathVariable String userName){
        User user = userService.getByUserName(userName).get();
        List<Transaction> transactions = transactionService.getAllByUser(user);
        if (transactions == null){
            return new ResponseEntity(new Message("the user not have transactions"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Transaction>>(transactions,HttpStatus.OK);
    }

    @PostMapping("/{userName}/confirmReception/{id}")
    public ResponseEntity<?> confirmReceptionTransaction(@PathVariable String userName,@PathVariable int id){
        if(!transactionService.existsById(id)){
            return new ResponseEntity(new Message("Transactions dont exist"), HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = transactionService.getByID(id).get();
        if(transaction.isClosed()){
            return new ResponseEntity(new Message("Transactions closed"), HttpStatus.BAD_REQUEST);
        }
        transaction.setConfirm(true);
        if (transaction.isConfirm() && transaction.isTransfer()) {
            this.calculateClose(transaction);
            transaction.setClosed(true);

        }
        transactionService.save(transaction);
        return new ResponseEntity<List<Transaction>>(HttpStatus.OK);
    }

    private void calculateClose(Transaction transaction) {
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
    }

    @PostMapping("/{userName}/confirmTransaction/{id}")
    public ResponseEntity<?> confirmTransaction(@PathVariable String userName,@PathVariable int id){
        if(!transactionService.existsById(id)){
            return new ResponseEntity(new Message("Transactions dont exist"), HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = transactionService.getByID(id).get();
        if(transaction.isClosed()){
            return new ResponseEntity(new Message("Transaction closed"), HttpStatus.BAD_REQUEST);
        }
        transaction.setTransfer(true);
        if (transaction.isConfirm() && transaction.isTransfer()) {
            transaction.setClosed(true);
            this.calculateClose(transaction);
        }
        transactionService.save(transaction);
        return new ResponseEntity<List<Transaction>>(HttpStatus.OK);
    }

    @PostMapping("/{userName}/cancelTransaction/{id}")
    public ResponseEntity<?> cancelTransaction(@PathVariable String userName,@PathVariable int id){

        if(!transactionService.existsById(id)){
            return new ResponseEntity(new Message("Transaction dont exist"), HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = transactionService.getByID(id).get();
        if(transaction.isClosed()){
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
        return new ResponseEntity<List<Transaction>>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransactions(){
        List<Transaction> transactions = transactionService.getAll();
        if (transactions == null){
            return new ResponseEntity(new Message("Transactions empty"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Transaction>>(transactions,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable int id){
        if(!transactionService.existsById(id)){
            return new ResponseEntity(new Message("Transaction dont exist"), HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = transactionService.getByID(id).get();

        return new ResponseEntity<Transaction>(transaction,HttpStatus.OK);
    }

}
