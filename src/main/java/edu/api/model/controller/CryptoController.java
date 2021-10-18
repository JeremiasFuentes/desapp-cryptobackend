package edu.api.model.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import edu.api.model.dto.Crypto;
import edu.api.model.dto.DollarPrice;
import edu.api.model.dto.Message;
import edu.api.model.enums.CryptoName;
import edu.api.model.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("auth/api")
@CrossOrigin
public class CryptoController {

    @Autowired
    CryptoService cryptoService = new CryptoService();

    @GetMapping("/crypts")
    public ResponseEntity<List<Crypto>> getCrypts() throws JsonProcessingException {

       List<Crypto> all = cryptoService.getAllCryptsFromApi();
       if (all == null){
           return new ResponseEntity(new Message("URL not found"),HttpStatus.BAD_REQUEST);
       }
       return new ResponseEntity<List<Crypto>>(all,HttpStatus.OK);
    }

    @GetMapping(value = "/crypts/{name}")
    public ResponseEntity<?> getCrypto(@PathVariable("name") String name) throws JsonProcessingException {
        Crypto active = cryptoService.getCryptFromApi(name);
        if(active == null){
            return new ResponseEntity(new Message("URL not found"),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Crypto>(active, HttpStatus.OK);
    }
}
