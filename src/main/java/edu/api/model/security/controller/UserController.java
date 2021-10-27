package edu.api.model.security.controller;

import edu.api.model.dto.Message;
import edu.api.model.security.entity.User;
import edu.api.model.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/users")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;


}
