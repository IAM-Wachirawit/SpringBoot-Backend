package com.nitendo.backend.repository;

import com.nitendo.backend.entity.Social;
import com.nitendo.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SocialRepository extends CrudRepository<Social, String> {


    Optional<Social> findByUser(User user);


}
