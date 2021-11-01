package edu.api.model.util;

import edu.api.model.entity.Publication;
import edu.api.model.entity.User;
import edu.api.model.enums.CryptoName;
import edu.api.model.service.CryptoService;
import edu.api.model.service.PublicationService;
import edu.api.model.service.TransactionService;
import edu.api.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/*
@Component
public class FakeInfo {

    @Autowired
    UserService userService = new UserService();

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PublicationService publicationService = new PublicationService();

    @Autowired
    CryptoService cryptoService;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        User user = new User("jeremias","fuentes","prueba1","prueba1@gmail.com", passwordEncoder.encode( "prueba1"),"direccion","1234567890987654321234","12345678");
        User user2 = new User("jeremias","fuentes","prueba2","prueba2@gmail.com",passwordEncoder.encode( "prueba2"),"direccion","1234567890987654321234","12655679");
        User user3 = new User("jeremias","fuentes","prueba3","prueba3@gmail.com",passwordEncoder.encode( "prueba3"),"direccion","1234567890987654321234","12325671");
        userService.save(user);
        userService.save(user2);
        userService.save(user3);


        Publication publication = new Publication(LocalDateTime.now(),user,"prueba1", CryptoName.BTCUSDT.name(),10.0f,cryptoService.getCryptFromApi(CryptoName.BTCUSDT.name()).getPrice(),
                (10.0f * cryptoService.getCryptFromApi(CryptoName.BTCUSDT.name()).getPrice()),"compra",0.0f);
        Publication publication2 = new Publication(LocalDateTime.now(),user,"prueba1", CryptoName.ALICEUSDT.name(),20.0f,cryptoService.getCryptFromApi(CryptoName.ALICEUSDT.name()).getPrice(),
                (20.0f * cryptoService.getCryptFromApi(CryptoName.BTCUSDT.name()).getPrice()),"compra",100.0f);
        Publication publication3 = new Publication(LocalDateTime.now(),user2,"prueba2", CryptoName.ATOMUSDT.name(),200.0f,cryptoService.getCryptFromApi(CryptoName.ATOMUSDT.name()).getPrice(),
                (200.0f * cryptoService.getCryptFromApi(CryptoName.BTCUSDT.name()).getPrice()),"compra",50.0f);

        publicationService.save(publication);
        publicationService.save(publication2);
        publicationService.save(publication3);

    }


}

*/