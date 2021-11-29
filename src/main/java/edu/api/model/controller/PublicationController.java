package edu.api.model.controller;

import edu.api.model.dto.Crypto;
import edu.api.model.dto.Message;
import edu.api.model.dto.NewPublication;
import edu.api.model.dto.NewTransaction;
import edu.api.model.entity.Publication;
import edu.api.model.entity.Transaction;
import edu.api.model.entity.User;
import edu.api.model.service.CryptoService;
import edu.api.model.service.PublicationService;
import edu.api.model.service.TransactionService;
import edu.api.model.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("auth/publication")
@CrossOrigin
@Log4j2
public class PublicationController {

    @Autowired
    PublicationService publicationService;

    @Autowired
    UserService userService;

    @Autowired
    CryptoService cryptoService;

    @PostMapping("/addP")
    public ResponseEntity<?> newPublication(@Valid @RequestBody NewPublication newPublication, BindingResult bindingResult){
        long start = System.currentTimeMillis();
        if(bindingResult.hasErrors()) {
            log.error("wrong information");
            return new ResponseEntity(new Message("wrong information"), HttpStatus.BAD_REQUEST);
        }
        Crypto crypto = cryptoService.getCryptFromApi(newPublication.getCryptoName());
        User user = userService.getByUserName(newPublication.getUserName()).get();
        Publication publication = new Publication(LocalDateTime.now(),user,newPublication.getUserName(), newPublication.getCryptoName(), newPublication.getAmountOfCrypto(),
                crypto.getPrice() , newPublication.getPriceTotalInPesos(), newPublication.getType(), user.getReputation());

        publicationService.save(publication);
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("User created in " + elapsedTimeMillis + " ms");
        return new ResponseEntity(new Message("publication created"), HttpStatus.CREATED);
    }


    @GetMapping("/user/{userName}")
    public ResponseEntity<?> getUserPublications(@PathVariable String userName){
        long start = System.currentTimeMillis();
        User user = userService.getByUserName(userName).get();
        List<Publication> publications = publicationService.getAllByUser(user);
        if (publications == null){
            log.error("the user not have publications");
            return new ResponseEntity(new Message("the user not have publications"), HttpStatus.BAD_REQUEST);
        }
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("return publication of user in " +elapsedTimeMillis+ " ms");
        return new ResponseEntity<List<Publication>>(publications,HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllPublications(){
        long start = System.currentTimeMillis();
        List<Publication> publications = publicationService.getAll();
        if (publications == null){
            log.info("Publications empty");
            return new ResponseEntity(new Message("Publications empty"), HttpStatus.BAD_REQUEST);
        }
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("Publications return in " +elapsedTimeMillis+ " ms");
        return new ResponseEntity<List<Publication>>(publications,HttpStatus.OK);
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getPublication(@PathVariable int id){
        long start = System.currentTimeMillis();
        if(!publicationService.existsById(id)){
            log.error("Publicaiton dont exist");
            return new ResponseEntity(new Message("Publication dont exist"), HttpStatus.BAD_REQUEST);
        }
        Publication publication = publicationService.getByID(id).get();
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("Publication return in " +elapsedTimeMillis+ " ms");
        return new ResponseEntity<Publication>(publication,HttpStatus.OK);
    }

    @PostMapping("/{userName}/cancelPublication/{id}")
    public ResponseEntity<?> cancelTransaction(@PathVariable String userName,@PathVariable int id){
        long start = System.currentTimeMillis();
        if(!publicationService.existsById(id)){
            log.info("Transaction dont exist");
            return new ResponseEntity(new Message("Transaction dont exist"), HttpStatus.BAD_REQUEST);
        }
        Publication publication = publicationService.getByID(id).get();
        String publisher = publication.getUser().getUserName();
        if(!Objects.equals(publisher, userName)){
            log.error("This publication is not yours to delete");
            return new ResponseEntity(new Message("This publication is not yours to delete"), HttpStatus.BAD_REQUEST);
        }
        publicationService.delete(publication);
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("Transaction delete in " +elapsedTimeMillis+ " ms");
        return new ResponseEntity<List<Transaction>>(HttpStatus.OK);
    }

}
