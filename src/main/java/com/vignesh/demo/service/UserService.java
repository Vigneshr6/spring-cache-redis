package com.vignesh.demo.service;

import com.vignesh.demo.model.User;
import com.vignesh.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Cacheable("user")
    public User findById(long id) throws UserNotFoundException {
        log.debug("find user : {}", id);
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Cacheable(value = "users")
    public List<User> findAll() {
        List<User> all = (List<User>) userRepository.findAll();
        log.debug("count : " + all.size());
        return all;
    }

    @Caching(evict =
            {@CacheEvict(value = "users", allEntries = true), @CacheEvict(value = "user", key = "#user.getId()")})
    public User save(User user) {
        log.debug("User : {}", user);
        userRepository.save(user);
        return user;
    }

    @Transactional
    @Caching(evict =
            {@CacheEvict(value = "user", key = "#id"), @CacheEvict(value = "users", allEntries = true)})
    public User update(long id, User updatedUser) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            log.error("User with id : {}  not found", id);
            throw new UserNotFoundException(id);
        }
        User user = optionalUser.get();
        user.setName(updatedUser.getName());
        user.setGender(updatedUser.getGender());
        userRepository.save(user);
        return user;
    }

    @Caching(evict =
            {@CacheEvict(value = "user"), @CacheEvict(value = "users", allEntries = true)})
    public void delete(Long id) throws UserNotFoundException {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("User with id : {}  not found", id);
            throw new UserNotFoundException(id);
        }
    }
}
