package edu.api.model;

import edu.api.model.entity.User;
import edu.api.model.repository.UserRepository;
import edu.api.model.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

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
