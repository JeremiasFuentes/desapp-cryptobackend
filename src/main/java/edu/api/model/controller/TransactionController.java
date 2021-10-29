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
        Publication publication = new Publication(LocalDateTime.now(),newPublication.getUserName(), newPublication.getCryptoName(), newPublication.getAmountOfCrypto(),
                crypto.getPrice() , newPublication.getPriceTotalInPesos(), newPublication.getType(), 0.0f);
        publicationService.save(publication);
        return new ResponseEntity(new Message("publication created"), HttpStatus.CREATED);
    }

    @PostMapping("/addT/{id}")
    public ResponseEntity<?> newTransaction(@Valid @RequestBody NewTransaction newTransaction, BindingResult bindingResult,@PathVariable int id){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("invalid mail or wrong information"), HttpStatus.BAD_REQUEST);
        Crypto crypto = cryptoService.getCryptFromApi(newTransaction.getCryptoName());
        Publication publication = publicationService.getByID(id).get();
        Transaction transaction = new Transaction(LocalDateTime.now(), newTransaction.getUserNameSeller(),newTransaction.getUserNameBuyer(),newTransaction.getCryptoName(),
                crypto.getPrice(),publication.getAmountOfCrypto() ,newTransaction.getPriceTotalInPesos(),newTransaction.getAmountOfCrypto(), newTransaction.getType(), publication);

        transactionService.save(transaction);
        publication.setAmountOfCrypto(publication.getAmountOfCrypto() - transaction.getAmountOfCryptoToBuy());
        publicationService.save(publication);

        User user = userService.getByUserName(newTransaction.getUserNameSeller()).get();
        User user2 = userService.getByUserName(newTransaction.getUserNameBuyer()).get();
        /*
        Transaction transactionWithID = transactionService.getByID(transaction.getId()).get();
        List<Transaction> list;
        List<Transaction> list2;
        list = user.getTransactions();
        list.add(transactionWithID);
        user.setTransactions(list);
        list2 = user2.getTransactions();
        list2.add(transactionWithID);
        user.setTransactions(list2);
        userService.save(user);
        userService.save(user2);
        */
        if(newTransaction.getType() == "compra"){

            return new ResponseEntity(new Message(user.getWallet()), HttpStatus.CREATED);
        }else{

            return new ResponseEntity(new Message(user.getCvu()), HttpStatus.CREATED);
        }

    }

    @GetMapping("/publications/{userName}")
    public ResponseEntity<?> getUsers(@PathVariable String userName){
        List<Publication> publications = publicationService.getByUserNameSeller(userName);
        if (publications == null){
            return new ResponseEntity(new Message("the user not have publications"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Publication>>(publications,HttpStatus.OK);
    }



}
