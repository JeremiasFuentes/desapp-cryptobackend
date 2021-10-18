package edu.api.model;

import edu.api.model.security.entity.User;
import edu.api.model.security.repository.UserRepository;
import edu.api.model.security.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository = Mockito.mock(UserRepository.class);

    @InjectMocks
    UserService userService = new UserService();

    private User user = new User();

    @BeforeEach
    void setUp(){
        userService.setUserRepository(userRepository);
    }

    @Test
    public void canGetUserByName(){

        Mockito.when(userRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(java.util.Optional.ofNullable(user));

        User response = userService.getByUserName("jeremias").get();
        Assert.assertEquals(user,response);
    }

    @Test
    public void canKnowIfUserExist(){
        Mockito.when(userRepository.existsByUserName(ArgumentMatchers.anyString())).thenReturn(true);

        Boolean response = userService.existsByUserName("jeremias");
        Assert.assertTrue(response);
    }

    @Test
    public void canKnowIfEmailIsInUse(){
        Mockito.when(userRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(true);

        Boolean response = userService.existsByEmail("jeremias@gmial.com");
        Assert.assertTrue(response);
    }
}
