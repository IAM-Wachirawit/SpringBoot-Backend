package com.nitendo.backend.service;

import com.nitendo.backend.entity.User;
import com.nitendo.backend.exception.BaseException;
import com.nitendo.backend.exception.UserException;
import com.nitendo.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    public User create(String email, String password, String name) throws BaseException {
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
        return repository.save(entity);
    }

    // Find by email
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    // Find by ID
    public Optional<User> findById(String id) {
        return repository.findById(id);
    }

    public boolean matchPassword(String rawPassword, String encodePassword) {
      return passwordEncoder.matches(rawPassword, encodePassword);
    }

    // Function update save data not check validate
    public User update(User user) {
        return repository.save(user);
    }
    // Function update save data type receipt method data have validate check
    public User updateName(String id, String name) throws BaseException {
        Optional<User> opt = repository.findById(id);
        if ( opt.isEmpty() ) {
            throw UserException.notFound();
        }
        User user = opt.get();
        user.setName(name);
        return repository.save(user);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
