package com.protect.security_manager.Mapper;

import com.protect.security_manager.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import security.manager.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity userToUserDto(User user);
}