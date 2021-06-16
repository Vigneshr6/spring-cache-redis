package com.vignesh.demo.controller;

import com.vignesh.demo.model.User;
import com.vignesh.demo.service.UserNotFoundException;
import com.vignesh.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        List<User> all = (List<User>) userService.findAll();
        log.debug("count : " + all.size());
        return all;
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable long id) throws UserNotFoundException {
        return userService.findById(id);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public User save(@RequestBody User employee) {
        log.debug("User : {}", employee);
        userService.save(employee);
        return employee;
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable Long id, @RequestBody User employee) {
        User empOld;
        try {
            empOld = userService.update(id, employee);
        } catch (UserNotFoundException e) {
            return new ResponseEntity(NOT_FOUND);
        }
        return new ResponseEntity(empOld, OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return new ResponseEntity(OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity(NOT_FOUND);
        }
    }
}
