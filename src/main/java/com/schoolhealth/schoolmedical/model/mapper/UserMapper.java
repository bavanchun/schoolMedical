package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.model.dto.response.UserResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @BeanMapping(ignoreByDefault = true)
//    @Mapping(target = "userId", source = "userId")
//    @Mapping(target = "firstName", source = "firstName")
//    @Mapping(target = "lastName", source = "lastName")
//    @Mapping(target = "birthDate", source = "birthDate")
//    @Mapping(target = "email", source = "email")
//    @Mapping(target = "phoneNumber", source = "phoneNumber")
//    @Mapping(target = "avatar", source = "avatar")
//    @Mapping(target = "createdAt", source = "createdAt")
//    @Mapping(target = "role", source = "role")
    UserResponse toUserResponse(User user);
}
