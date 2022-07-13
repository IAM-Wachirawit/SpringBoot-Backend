package com.nitendo.backend.mapper;

import com.nitendo.backend.entity.User;
import com.nitendo.backend.model.MRegisterResponse;
import com.nitendo.backend.model.MUserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Function Map from user to class MRegisterResponse
    MRegisterResponse toRegisterResponse(User user);

    MUserProfile toUserProfile(User user);

}
