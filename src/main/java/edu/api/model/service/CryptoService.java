package edu.api.model.service;

import edu.api.model.dto.Crypto;
import edu.api.model.dto.DollarPrice;
import edu.api.model.enums.CryptoName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CryptoService {

    @Autowired
    RestTemplate restTemplate = new RestTemplate();


    public List<Crypto> getAllCryptsFromApi(){
        try {
            ResponseEntity<List<Crypto>> all = restTemplate.exchange("https://api1.binance.com/api/v3/ticker/price", HttpMethod.GET, null, new ParameterizedTypeReference<List<Crypto>>() {});
            if (all.getStatusCode() == HttpStatus.BAD_REQUEST){
                return null;
            }
            List<Crypto> result = all.getBody();
            List<Crypto> finish = result.stream().filter(x -> Arrays.stream(CryptoName.values()).anyMatch(c -> c.toString().equals(x.getSymbol()))).collect(Collectors.toList());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            finish.forEach(x -> x.setLastUpdate(dtf.format(LocalDateTime.now())));
            DollarPrice dolar = this.getDollarPriceNow();
            finish.forEach(x -> x.setPrice(x.getPrice() * dolar.getV()));
            return finish;
        }catch (HttpClientErrorException.BadRequest e){
            return null;
        }
    }

    public Crypto getCryptFromApi(String name){
            ResponseEntity<Crypto> active = restTemplate.getForEntity("https://api1.binance.com/api/v3/ticker/price?symbol=" + name, Crypto.class);
            if (active.getStatusCode() == HttpStatus.BAD_REQUEST){
                return null;
            }
            Crypto body = active.getBody();
            body.setPrice(body.getPrice() * this.getDollarPriceNow().getV());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            body.setLastUpdate(dtf.format(LocalDateTime.now()));
            return body;

    }

    public DollarPrice getDollarPriceNow(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","BEARER eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NjQ0OTI5NjMsInR5cGUiOiJleHRlcm5hbCIsInVzZXIiOiJqZXJlbWlhc2Z1ZW50ZXM3N0BnbWFpbC5jb20ifQ.gHHz2GFOfB8BRJBq-vWCK4Fh4LdDfozeG-Pg9bfc4YwQr-sR1Nc-Phy3m3BBjTK1DKAVEdRAlz_pGk-UGUvHwg");
        HttpEntity<String> entity = new HttpEntity(headers);
        ResponseEntity<List<DollarPrice>> dollarPrices = restTemplate.exchange("https://api.estadisticasbcra.com/usd_of", HttpMethod.GET,entity, new ParameterizedTypeReference<List<DollarPrice>>() {});
        List<DollarPrice> response = dollarPrices.getBody();
        DollarPrice dolar = response.get(response.size()-1);
        return dolar;
    }

}
