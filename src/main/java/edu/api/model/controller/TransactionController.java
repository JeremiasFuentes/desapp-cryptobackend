package edu.api.model.controller;

import edu.api.model.dto.Crypto;
import edu.api.model.dto.Message;
import edu.api.model.dto.NewPublication;
import edu.api.model.dto.NewTransaction;
import edu.api.model.entity.Publication;
import edu.api.model.entity.Transaction;
import edu.api.model.security.entity.User;
import edu.api.model.security.service.UserService;
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
import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/addP")
    public ResponseEntity<?> newPublication(@Valid @RequestBody NewPublication newPublication, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("invalid mail or wrong information"), HttpStatus.BAD_REQUEST);
        Crypto crypto = cryptoService.getCryptFromApi(newPublication.getCryptoName());
        User user = userService.getByUserName(newPublication.getUserName()).get();
        Publication publication = new Publication(LocalDateTime.now(),user, newPublication.getCryptoName(), newPublication.getAmountOfCrypto(),
                crypto.getPrice() , newPublication.getPriceTotalInPesos(), newPublication.getType(), 0.0f);

        /*
        List<Publication> publications = user.getPublications();
        publications.add(publication);
        user.setPublications(publications);
        userService.save(user);
        */
        publicationService.save(publication);
        return new ResponseEntity(new Message("publication created"), HttpStatus.CREATED);
    }

    @PostMapping("/addT/{id}")
    public ResponseEntity<?> newTransaction(@Valid @RequestBody NewTransaction newTransaction, BindingResult bindingResult,@PathVariable int id){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("invalid mail or wrong information"), HttpStatus.BAD_REQUEST);
        Crypto crypto = cryptoService.getCryptFromApi(newTransaction.getCryptoName());
        Publication publication = publicationService.getByID(id).get();
        User user = userService.getByUserName(newTransaction.getUserNameSeller()).get();
        User user2 = userService.getByUserName(newTransaction.getUserNameBuyer()).get();
        Transaction transaction = new Transaction(LocalDateTime.now(), newTransaction.getUserNameSeller(),user2,newTransaction.getCryptoName(),
                crypto.getPrice(),publication.getAmountOfCrypto() , publication.getPriceTotalInPesos(), newTransaction.getAmountOfCrypto(), newTransaction.getType(), publication);
        publication.setAmountOfCrypto(publication.getAmountOfCrypto() - transaction.getAmountOfCryptoToBuy());
        publicationService.save(publication);
        transactionService.save(transaction);
        if(newTransaction.getType() == "compra"){

            return new ResponseEntity(new Message(user.getWallet()), HttpStatus.CREATED);
        }else{

            return new ResponseEntity(new Message(user.getCvu()), HttpStatus.CREATED);
        }

    }

    @GetMapping("/publications/{userName}")
    public ResponseEntity<?> getUserPublications(@PathVariable String userName){
        User user = userService.getByUserName(userName).get();
        List<Publication> publications = publicationService.getAllByUser(user);
        if (publications == null){
            return new ResponseEntity(new Message("the user not have publications"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Publication>>(publications,HttpStatus.OK);
    }

    @GetMapping("/{userName}")
    public ResponseEntity<?> getUserTransactions(@PathVariable String userName){
        User user = userService.getByUserName(userName).get();
        List<Transaction> transactions = transactionService.getAllByUser(user);
        if (transactions == null){
            return new ResponseEntity(new Message("the user not have transactions"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Transaction>>(transactions,HttpStatus.OK);
    }

    @PostMapping("/confirmReception/{id}")
    public ResponseEntity<?> confirmReceptionTransaction(@PathVariable int id){
        if(!transactionService.existsById(id)){
            return new ResponseEntity(new Message("Transactions dont exist"), HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = transactionService.getByID(id).get();
        if(transaction.isClosed()){
            return new ResponseEntity(new Message("Transactions closed"), HttpStatus.BAD_REQUEST);
        }
        /*
        User userOfPublication = userService.getByUserName(transaction.getUserNameSeller()).get();
        User userOfTransaction = transaction.getUser();
        if (transaction.getType() == "compra") {

        }
        */
        transaction.setConfirm(true);
        if (transaction.isConfirm() && transaction.isTransfer()) {
            transaction.setClosed(true);
        }
        transactionService.save(transaction);
        return new ResponseEntity<List<Transaction>>(HttpStatus.OK);
    }

    @PostMapping("/confirmTransaction/{id}")
    public ResponseEntity<?> confirmTransaction(@PathVariable int id){
        if(!transactionService.existsById(id)){
            return new ResponseEntity(new Message("Transactions dont exist"), HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = transactionService.getByID(id).get();
        if(transaction.isClosed()){
            return new ResponseEntity(new Message("Transactions closed"), HttpStatus.BAD_REQUEST);
        }
        /*
        User userOfPublication = userService.getByUserName(transaction.getUserNameSeller()).get();
        User userOfTransaction = transaction.getUser();
        if (transaction.getType() == "compra") {

        }
        */
        transaction.setTransfer(true);
        if (transaction.isConfirm() && transaction.isTransfer()) {
            transaction.setClosed(true);
        }
        transactionService.save(transaction);
        return new ResponseEntity<List<Transaction>>(HttpStatus.OK);
    }

    @PostMapping("/cancelTransaction/{id}")
    public ResponseEntity<?> cancelTransaction(@PathVariable int id){

        if(!transactionService.existsById(id)){
            return new ResponseEntity(new Message("Transaction dont exist"), HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = transactionService.getByID(id).get();
        if(transaction.isClosed()){
            return new ResponseEntity(new Message("Transactions closed"), HttpStatus.BAD_REQUEST);
        }
        /*
        User userOfPublication = userService.getByUserName(transaction.getUserNameSeller()).get();
        User userOfTransaction = transaction.getUser();
        if (transaction.getType() == "compra") {

        }
        */

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

    @GetMapping("/publications")
    public ResponseEntity<?> getAllPublications(){
        List<Publication> publications = publicationService.getAll();
        if (publications == null){
            return new ResponseEntity(new Message("Transactions empty"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Publication>>(publications,HttpStatus.OK);
    }

}
