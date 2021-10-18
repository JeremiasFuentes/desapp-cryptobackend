package edu.api.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.api.model.controller.CryptoController;
import edu.api.model.dto.Crypto;
import edu.api.model.dto.Message;
import edu.api.model.service.CryptoService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CryptoControllerTest {

    @Mock
    private CryptoService cryptoService = new CryptoService();

    @InjectMocks
    private CryptoController cryptoController = new CryptoController();

    private Crypto crypto;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        crypto = new Crypto("ALICEUSDT", 15.00f);
    }

    @Test
    void getAliceCrypto() throws JsonProcessingException {


        Mockito
                .when(cryptoService.getCryptFromApi("ALICEUSDT"))
          .thenReturn(crypto);

        ResponseEntity<Crypto> response = (ResponseEntity<Crypto>) cryptoController.getCrypto("ALICEUSDT");
        Assert.assertEquals(response.getBody(), crypto);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getNotExistCrypto() throws JsonProcessingException {

        Mockito
                .when(cryptoService.getCryptFromApi("wasd"))
                .thenReturn(null);

        ResponseEntity<?> response = (ResponseEntity<Crypto>) cryptoController.getCrypto("ALICEUSDT");
        Message msg = (Message) response.getBody();
        Assert.assertEquals(msg.getMessage(), "URL not found");
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void getAllCrypts() throws JsonProcessingException {
        List<Crypto> crypts = new ArrayList<Crypto>();
        crypts.add(crypto);
        Mockito
                .when(cryptoService.getAllCryptsFromApi())
                .thenReturn(crypts);

        ResponseEntity<?> response = cryptoController.getCrypts();
        Assert.assertEquals(response.getBody(), crypts);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void cantGetAllCrypts() throws JsonProcessingException {

        Mockito
                .when(cryptoService.getAllCryptsFromApi())
                .thenReturn(null);

        ResponseEntity<?> response = cryptoController.getCrypts();
        Message msg = (Message) response.getBody();
        Assert.assertEquals(msg.getMessage(), "URL not found");
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
