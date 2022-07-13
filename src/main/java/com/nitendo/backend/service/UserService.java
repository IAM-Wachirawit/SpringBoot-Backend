package com.nitendo.backend.service;

import com.nitendo.backend.entity.User;
import com.nitendo.backend.exception.BaseException;
import com.nitendo.backend.exception.UserException;
import com.nitendo.backend.repository.UserRepository;
import com.nitendo.backend.util.SecurityUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(String email, String password, String name, String token, Date tokenExpireDate) throws BaseException {
        // Validate
        if (Objects.isNull(email)) {
            throw UserException.createEmailNull();  // throw error email null
        }

        if (Objects.isNull(password)) {
            throw UserException.createPasswordNull();   // throw error password null
        }

        if (Objects.isNull(name)) {
            throw  UserException.createNameNull();  // throw error name null
        }

        // Verify check have email in system (email unique key in file entity)
        if ( repository.existsByEmail(email) ) {
            throw UserException.createEmailDuplicated();    // throw error email duplicated
        }

        // Save
        User entity = new User();
        entity.setEmail(email);
        entity.setPassword(passwordEncoder.encode(password));
        entity.setName(name);
        entity.setToken(token);
        entity.setTokenExpire(tokenExpireDate);
        return repository.save(entity);
    }



    // Find by email
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    // Find by ID
    @Cacheable(value = "user", key = "#id", unless = "#result == null")
    public Optional<User> findById(String id) {
        log.info("Load User From DB: " + id);
        return repository.findById(id);
    }

    // Find by Token
    public Optional<User> findByToken(String token) {
        return repository.findByToken(token);
    }

    public boolean matchPassword(String rawPassword, String encodePassword) {
      return passwordEncoder.matches(rawPassword, encodePassword);
    }

    // Function update save data not check validate
    public User update(User user) {
        return repository.save(user);
    }

    // Function update save data type receipt method data have validate check
    @CachePut(value = "user", key = "#id")
    public User updateName(String id, String name) throws BaseException {
        Optional<User> opt = repository.findById(id);
        if ( opt.isEmpty() ) {
            throw UserException.notFound();
        }
        User user = opt.get();
        user.setName(name);
        return repository.save(user);
    }

    @CacheEvict(value = "user", key = "#id")
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @CacheEvict(value = "user", allEntries = true)
    public void deleteAll() {
        repository.deleteAll();
    }
}
