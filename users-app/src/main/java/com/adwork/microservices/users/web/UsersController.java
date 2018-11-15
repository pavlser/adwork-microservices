package com.adwork.microservices.users.web;

import com.adwork.microservices.users.entity.UserAccount;
import com.adwork.microservices.users.service.IUserService;
import com.adwork.microservices.users.web.dto.UserDto;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController("/api/users")
public class UsersController {

    IUserService service;

    public UsersController(IUserService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public UserDto one(@PathVariable("id") Long userId) {
        return toUserDto(service.getUser(userId));
    }

    @RequestMapping(path="all", method=RequestMethod.GET)
    public List<UserDto> all() {
        /* Resources<Resource<UserDto>> listUsers() {
        List<Resource<UserDto>> usersList = service.listUsers().stream()
                .map(userAccount -> new Resource<>(
                        toUserDto(userAccount),
                        linkTo(methodOn(UsersController.class).getUserById(userAccount.getId())).withSelfRel(),
                        linkTo(methodOn(UsersController.class).listUsers()).withRel("users")))
                .collect(Collectors.toList());

        return new Resources<>(usersList,
                linkTo(methodOn(UsersController.class).listUsers()).withSelfRel());*/

        return service.listUsers().stream()
                .map(userAccount -> toUserDto(userAccount))
                .collect(Collectors.toList());
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto user) {
        UserAccount account = new UserAccount();
        account.setEmail(user.email);
        account.setRole(user.role);
        account.setLocked(user.locked);
        return toUserDto(service.addUser(account));
    }

    @DeleteMapping("{id}")
    public boolean deleteUser(@PathVariable("id") Long userId) {
        return service.deleteUser(userId) != null;
    }

    @PutMapping
    public UserDto modifyUser(@RequestBody UserDto user) {
        UserAccount account = service.getUser(user.id);
        account.setEmail(user.email);
        account.setRole(user.role);
        account.setLocked(user.locked);
        return toUserDto(service.updateUser(account));
    }

    private UserDto toUserDto(UserAccount acc) {
        return new UserDto(
                acc.getId(),
                acc.getEmail(),
                acc.getRole(),
                acc.isLocked());
    }

}
