package com.adwork.microservices.users.dto;

import com.adwork.microservices.users.entity.UserRole;

import io.swagger.annotations.ApiModelProperty;

//@Data
public class UserDto {

    @ApiModelProperty(position = 0)
    public long id;

    @ApiModelProperty(position = 1)
    public String email;

    @ApiModelProperty(position = 2)
    public UserRole role;

    @ApiModelProperty(position = 3)
    public boolean locked;

    public UserDto() {
    }

    public UserDto(long id, String email, UserRole role, boolean locked) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.locked = locked;
    }

}
