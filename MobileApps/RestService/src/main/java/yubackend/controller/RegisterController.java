package yubackend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import yubackend.model.User;
import yubackend.service.GenericService;

@RestController
public class RegisterController {
    @Autowired
    private GenericService userService;

    @PostMapping(value = "/register")
    public ResponseEntity register(@RequestBody User user){
        User user1 = userService.save(user);
        if (user1 != null)
            return new ResponseEntity(HttpStatus.CREATED);
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
