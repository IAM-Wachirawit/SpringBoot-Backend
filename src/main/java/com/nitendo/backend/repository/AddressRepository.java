package com.nitendo.backend.repository;

import com.nitendo.backend.entity.Address;
import com.nitendo.backend.entity.Social;
import com.nitendo.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends CrudRepository<Address, String> {

    List<Address> findByUser(User user);


}
