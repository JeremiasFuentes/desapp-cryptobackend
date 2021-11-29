package edu.api.model.controller;

import edu.api.model.dto.Message;
import edu.api.model.dto.JwtDto;
import edu.api.model.dto.LoginUser;
import edu.api.model.dto.RegisterUser;
import edu.api.model.entity.Rol;
import edu.api.model.entity.User;
import edu.api.model.enums.RolName;
import edu.api.model.jwt.JwtProvider;
import edu.api.model.service.RolService;
import edu.api.model.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("")
@CrossOrigin
@Log4j2
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUser registerUser, BindingResult bindingResult){
        long start = System.currentTimeMillis();
        if(bindingResult.hasErrors()) {
            log.error("invalid mail or wrong information");
            return new ResponseEntity(new Message("invalid mail or wrong information"), HttpStatus.BAD_REQUEST);
        }
        if(userService.existsByUserName(registerUser.getLastName())) {
            log.error("user name in use");
            return new ResponseEntity(new Message("user name in use"), HttpStatus.BAD_REQUEST);
        }
        if(userService.existsByEmail(registerUser.getEmail())) {
            log.error("mail in use");
            return new ResponseEntity(new Message("mail in use"), HttpStatus.BAD_REQUEST);
        }
        User user =
                new User(registerUser.getName(), registerUser.getLastName(),registerUser.getUserName(), registerUser.getEmail(),passwordEncoder.encode(registerUser.getPassword()),registerUser.getDirection(),registerUser.getCvu(),registerUser.getWallet());
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolName.ROLE_USER).get());
        if(registerUser.getRols().contains("admin")) {
            roles.add(rolService.getByRolName(RolName.ROLE_ADMIN).get());
        }
        user.setRols(roles);
        userService.save(user);
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("user created in " + elapsedTimeMillis + " ms");
        return new ResponseEntity(new Message("user created"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUser loginUser, BindingResult bindingResult){
        long start = System.currentTimeMillis();
        if(bindingResult.hasErrors()) {
            log.error("invalid information");
            return new ResponseEntity(new Message("invalid information"), HttpStatus.BAD_REQUEST);
        }
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("Login success in "+ elapsedTimeMillis + " ms");
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }

    @GetMapping("/auth/users")
    public ResponseEntity<?> getUsers(){
        long start = System.currentTimeMillis();
        List<User> all = userService.getAll();
        if (all == null){
            log.error("Users not found");
            return new ResponseEntity(new Message("Users not found"), HttpStatus.BAD_REQUEST);
        }
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.info("Get users success in " + elapsedTimeMillis + " ms");
        return new ResponseEntity<List<User>>(all,HttpStatus.OK);
    }

    @GetMapping("/auth/users/{id}")
    public ResponseEntity<?> getUsers(@PathVariable int id){
        long start = System.currentTimeMillis();
        User user = userService.getById(id).get();
        if (user == null){
            log.error("User not found");
            return new ResponseEntity(new Message("User not found"), HttpStatus.BAD_REQUEST);
        }
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        log.error("User found in " + elapsedTimeMillis + " ms");
        return new ResponseEntity<User>(user,HttpStatus.OK);
    }
}
