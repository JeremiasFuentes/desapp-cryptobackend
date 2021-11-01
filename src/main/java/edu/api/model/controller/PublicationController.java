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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("auth/publication")
@CrossOrigin
public class PublicationController {

    @Autowired
    PublicationService publicationService;

    @Autowired
    UserService userService;

    @Autowired
    CryptoService cryptoService;

    @PostMapping("/addP")
    public ResponseEntity<?> newPublication(@Valid @RequestBody NewPublication newPublication, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("wrong information"), HttpStatus.BAD_REQUEST);
        Crypto crypto = cryptoService.getCryptFromApi(newPublication.getCryptoName());
        User user = userService.getByUserName(newPublication.getUserName()).get();
        Publication publication = new Publication(LocalDateTime.now(),user,newPublication.getUserName(), newPublication.getCryptoName(), newPublication.getAmountOfCrypto(),
                crypto.getPrice() , newPublication.getPriceTotalInPesos(), newPublication.getType(), user.getReputation());

        publicationService.save(publication);
        return new ResponseEntity(new Message("publication created"), HttpStatus.CREATED);
    }


    @GetMapping("/user/{userName}")
    public ResponseEntity<?> getUserPublications(@PathVariable String userName){
        User user = userService.getByUserName(userName).get();
        List<Publication> publications = publicationService.getAllByUser(user);
        if (publications == null){
            return new ResponseEntity(new Message("the user not have publications"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Publication>>(publications,HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllPublications(){
        List<Publication> publications = publicationService.getAll();
        if (publications == null){
            return new ResponseEntity(new Message("Publications empty"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Publication>>(publications,HttpStatus.OK);
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getPublication(@PathVariable int id){
        if(!publicationService.existsById(id)){
            return new ResponseEntity(new Message("Publication dont exist"), HttpStatus.BAD_REQUEST);
        }
        Publication publication = publicationService.getByID(id).get();

        return new ResponseEntity<Publication>(publication,HttpStatus.OK);
    }

}
