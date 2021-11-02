package edu.api.model.service;

import edu.api.model.dto.Crypto;
import edu.api.model.dto.DollarPrice;
import edu.api.model.enums.CryptoName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "cryptosCache")
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
            finish.forEach(x -> x.setPrice(x.getPrice() * dolar.getSell()));
            return finish;
        }catch (HttpClientErrorException.BadRequest e){
            return null;
        }
    }

    @Cacheable(value = "crypto")
    public Crypto getCryptFromApi(String name){
            ResponseEntity<Crypto> active = restTemplate.getForEntity("https://api1.binance.com/api/v3/ticker/price?symbol=" + name, Crypto.class);
            if (active.getStatusCode() == HttpStatus.BAD_REQUEST){
                return null;
            }
            Crypto body = active.getBody();
            body.setPrice(body.getPrice() * this.getDollarPriceNow().getSell());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            body.setLastUpdate(dtf.format(LocalDateTime.now()));
            return body;

    }

    @Cacheable(value = "dollar")
    public DollarPrice getDollarPriceNow(){
        ResponseEntity<DollarPrice> dollarPrice = restTemplate.getForEntity("https://api-dolar-argentina.herokuapp.com/api/dolaroficial",DollarPrice.class);
        DollarPrice dolar = dollarPrice.getBody();
        return dolar;
    }

}
