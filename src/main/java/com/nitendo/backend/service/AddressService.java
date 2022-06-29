package com.nitendo.backend.service;

import com.nitendo.backend.entity.Address;
import com.nitendo.backend.entity.Social;
import com.nitendo.backend.entity.User;
import com.nitendo.backend.repository.AddressRepository;
import com.nitendo.backend.repository.SocialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    public List<Address> findByUser(User user) {
        return repository.findByUser(user);
    }

    public Address create(User user, String line1, String line2, String zipcode) {
        // TODO: validate

        // Create
        Address entity = new Address();
        entity.setUser(user);
        entity.setLine1(line1);
        entity.setLine2(line2);
        entity.setZipcode(zipcode);

        return repository.save(entity);
    }
}
