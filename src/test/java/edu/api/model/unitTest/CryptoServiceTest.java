package edu.api.model.unitTest;

import edu.api.model.dto.Crypto;
import edu.api.model.dto.DollarPrice;
import edu.api.model.service.CryptoService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CryptoServiceTest {

    @Mock
    RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    @InjectMocks
    CryptoService cryptoService = new CryptoService();


    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);

    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Test
    public void getAliceResponseFromApi(){
        Crypto crypto = new Crypto("ALICEUSDT", 17.00f);
        ResponseEntity<Crypto> cryptoResponse = new ResponseEntity<>(crypto, HttpStatus.OK);

        DollarPrice dp = new DollarPrice("", 1.00f, 1.00f);
        List<DollarPrice> ld = new ArrayList<>();
        ld.add(dp);
        ResponseEntity<List<DollarPrice>> dollarResponse = new ResponseEntity<>(ld,HttpStatus.OK);

        Mockito
                .when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.eq(Crypto.class)))
                .thenReturn(cryptoResponse);

        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<DollarPrice>>>any()))
                .thenReturn(dollarResponse);

        Crypto response = cryptoService.getCryptFromApi("ALICEUSDT");
        Assert.assertEquals(response.getSymbol(), crypto.getSymbol());
    }

    @Test
    public void getBadResponseFromApi(){
        ResponseEntity<Crypto> cryptoResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        Mockito
                .when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.eq(Crypto.class)))
                .thenReturn(cryptoResponse);


        Crypto response = cryptoService.getCryptFromApi("ALICEUSDTEDD");
        Assert.assertEquals(response, null);
    }

    @Test
    public void getAllCryptsFromApiTest(){
        DollarPrice dp = new DollarPrice("", 1.00f, 1.00f);
        List<DollarPrice> ld = new ArrayList<>();
        ld.add(dp);
        ResponseEntity<List<DollarPrice>> dollarResponse = new ResponseEntity<>(ld,HttpStatus.OK);

        Crypto crypto = new Crypto("ALICEUSDT", 17.00f);
        List<Crypto> lc = new ArrayList<>();
        lc.add(crypto);
        ResponseEntity<List<Crypto>> cResponse = new ResponseEntity<>(lc,HttpStatus.OK);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","BEARER eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NjQ0OTI5NjMsInR5cGUiOiJleHRlcm5hbCIsInVzZXIiOiJqZXJlbWlhc2Z1ZW50ZXM3N0BnbWFpbC5jb20ifQ.gHHz2GFOfB8BRJBq-vWCK4Fh4LdDfozeG-Pg9bfc4YwQr-sR1Nc-Phy3m3BBjTK1DKAVEdRAlz_pGk-UGUvHwg");
        HttpEntity<String> entity = new HttpEntity(headers);

        Mockito.when(restTemplate.exchange("https://api1.binance.com/api/v3/ticker/price", HttpMethod.GET, null, new ParameterizedTypeReference<List<Crypto>>() {}))
                .thenReturn(cResponse);

        Mockito.when( restTemplate.exchange("https://api.estadisticasbcra.com/usd_of", HttpMethod.GET,entity, new ParameterizedTypeReference<List<DollarPrice>>() {}))
                .thenReturn(dollarResponse);

        List<Crypto> result = cryptoService.getAllCryptsFromApi();
        Assert.assertEquals(result.size(), 1);
    }

}
