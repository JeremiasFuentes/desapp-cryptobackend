package edu.api.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.api.model.controller.CryptoController;
import edu.api.model.dto.Crypto;
import edu.api.model.dto.Message;
import edu.api.model.enums.RolName;
import edu.api.model.security.controller.AuthController;
import edu.api.model.security.dto.JwtDto;
import edu.api.model.security.dto.LoginUser;
import edu.api.model.security.dto.RegisterUser;
import edu.api.model.security.entity.Rol;
import edu.api.model.security.jwt.JwtProvider;
import edu.api.model.security.repository.UserRepository;
import edu.api.model.security.service.RolService;
import edu.api.model.security.service.UserService;
import edu.api.model.service.CryptoService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import java.util.Collection;

public class AuthControllerTest {

    @Mock
    private UserService userService = Mockito.mock(UserService.class);

    @Mock
    private RolService rolService = Mockito.mock(RolService.class);

    @Mock
    private JwtProvider jwtProvider = Mockito.mock(JwtProvider.class);

    @Mock
    private PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    @Mock
    private AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);

    @InjectMocks
    private AuthController authController = new AuthController();

    private LoginUser lUser;
    private RegisterUser user;
    private BindingResult br;
    private Rol rol;
    private Authentication authentication;
    private UserDetails userDetails;
    private Collection authorities;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        user = Mockito.mock(RegisterUser.class);
        br = Mockito.mock(BindingResult.class);
        rol = Mockito.mock(Rol.class);
        lUser = Mockito.mock(LoginUser.class);
        authentication = Mockito.mock(Authentication.class);
        userDetails = Mockito.mock(UserDetails.class);
        authorities = Mockito.mock(Collection.class);
    }


    @Test
    void registerUser() throws JsonProcessingException {


        Mockito
                .when(userService.existsByEmail(ArgumentMatchers.anyString()))
                .thenReturn(false);

        Mockito
                .when(userService.existsByUserName(ArgumentMatchers.anyString()))
                .thenReturn(false);

        Mockito.when(br.hasErrors()).thenReturn(false);

        Mockito.when(rolService.getByRolName(ArgumentMatchers.any(RolName.class))).thenReturn(java.util.Optional.ofNullable(rol));

        Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("pass");


        Mockito.when(user.getUserName()).thenReturn("admin");
        Mockito.when(user.getCvu()).thenReturn("12312312");
        Mockito.when(user.getDirection()).thenReturn("");
        Mockito.when(user.getEmail()).thenReturn("admin@a.a");
        Mockito.when(user.getLastName()).thenReturn("Fuentes");
        Mockito.when(user.getName()).thenReturn("Jeremias");
        Mockito.when(user.getWallet()).thenReturn("123123124343124414434");
        Mockito.when(user.getPassword()).thenReturn("asdfgh77");

        ResponseEntity<?> response = authController.nuevo(user,br);
        Message msg = (Message) response.getBody();
        Assert.assertEquals(msg.getMessage(), "user created");
        Assert.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    void registerUserMailInUse() throws JsonProcessingException {


        Mockito
                .when(userService.existsByEmail(ArgumentMatchers.anyString()))
                .thenReturn(true);

        Mockito
                .when(userService.existsByUserName(ArgumentMatchers.anyString()))
                .thenReturn(false);

        Mockito.when(br.hasErrors()).thenReturn(false);

        Mockito.when(rolService.getByRolName(ArgumentMatchers.any(RolName.class))).thenReturn(java.util.Optional.ofNullable(rol));

        Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("pass");


        Mockito.when(user.getUserName()).thenReturn("admin");
        Mockito.when(user.getCvu()).thenReturn("12312312");
        Mockito.when(user.getDirection()).thenReturn("");
        Mockito.when(user.getEmail()).thenReturn("admin@a.a");
        Mockito.when(user.getLastName()).thenReturn("Fuentes");
        Mockito.when(user.getName()).thenReturn("Jeremias");
        Mockito.when(user.getWallet()).thenReturn("123123124343124414434");
        Mockito.when(user.getPassword()).thenReturn("asdfgh77");

        ResponseEntity<?> response = authController.nuevo(user,br);
        Message msg = (Message) response.getBody();
        Assert.assertEquals(msg.getMessage(), "mail in use");
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void registerUserNameInUse() throws JsonProcessingException {


        Mockito
                .when(userService.existsByEmail(ArgumentMatchers.anyString()))
                .thenReturn(false);

        Mockito
                .when(userService.existsByUserName(ArgumentMatchers.anyString()))
                .thenReturn(true);

        Mockito.when(br.hasErrors()).thenReturn(false);

        Mockito.when(rolService.getByRolName(ArgumentMatchers.any(RolName.class))).thenReturn(java.util.Optional.ofNullable(rol));

        Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("pass");


        Mockito.when(user.getUserName()).thenReturn("admin");
        Mockito.when(user.getCvu()).thenReturn("12312312");
        Mockito.when(user.getDirection()).thenReturn("");
        Mockito.when(user.getEmail()).thenReturn("admin@a.a");
        Mockito.when(user.getLastName()).thenReturn("Fuentes");
        Mockito.when(user.getName()).thenReturn("Jeremias");
        Mockito.when(user.getWallet()).thenReturn("123123124343124414434");
        Mockito.when(user.getPassword()).thenReturn("asdfgh77");

        ResponseEntity<?> response = authController.nuevo(user,br);
        Message msg = (Message) response.getBody();
        Assert.assertEquals(msg.getMessage(), "user name in use");
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void loginTest() throws JsonProcessingException {


        Mockito.when(br.hasErrors()).thenReturn(false);

        Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        Mockito.when(jwtProvider.generateToken(ArgumentMatchers.any(Authentication.class))).thenReturn("hola");

        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        Mockito.when(userDetails.getUsername()).thenReturn("jere");

        Mockito.when(userDetails.getAuthorities()).thenReturn(authorities);



        ResponseEntity<JwtDto> response = authController.login(lUser,br);
        JwtDto jwtDto = response.getBody();
        Assert.assertEquals(jwtDto.getToken(), "hola");
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
