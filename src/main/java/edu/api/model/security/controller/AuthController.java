package edu.api.model.security.controller;

import edu.api.model.dto.Message;
import edu.api.model.security.dto.JwtDto;
import edu.api.model.security.dto.LoginUser;
import edu.api.model.security.dto.RegisterUser;
import edu.api.model.security.entity.Rol;
import edu.api.model.security.entity.User;
import edu.api.model.enums.RolName;
import edu.api.model.security.jwt.JwtProvider;
import edu.api.model.security.service.RolService;
import edu.api.model.security.service.UserService;
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
@RequestMapping("/auth")
@CrossOrigin
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
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("invalid mail or wrong information"), HttpStatus.BAD_REQUEST);
        if(userService.existsByUserName(registerUser.getLastName()))
            return new ResponseEntity(new Message("user name in use"), HttpStatus.BAD_REQUEST);
        if(userService.existsByEmail(registerUser.getEmail()))
            return new ResponseEntity(new Message("mail in use"), HttpStatus.BAD_REQUEST);
        User user =
                new User(registerUser.getName(), registerUser.getLastName(),registerUser.getUserName(), registerUser.getEmail(),passwordEncoder.encode(registerUser.getPassword()),registerUser.getDirection(),registerUser.getCvu(),registerUser.getWallet());
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolName.ROLE_USER).get());
        if(registerUser.getRols().contains("admin"))
            roles.add(rolService.getByRolName(RolName.ROLE_ADMIN).get());
        user.setRols(roles);
        userService.save(user);
        return new ResponseEntity(new Message("user created"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUser loginUser, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("invalid information"), HttpStatus.BAD_REQUEST);
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        List<User> all = userService.getAll();
        if (all == null){
            return new ResponseEntity(new Message("Users not found"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<User>>(all,HttpStatus.OK);
    }
}
