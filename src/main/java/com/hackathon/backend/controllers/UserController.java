package com.hackathon.backend.controllers;

import com.hackathon.backend.dto.userDto.EditUserDto;
import com.hackathon.backend.dto.userDto.LoginUserDto;
import com.hackathon.backend.dto.userDto.RegisterUserDto;
import com.hackathon.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userServices) {
        this.userService = userServices;
    }

    @PostMapping(path="${USER_REGISTER_PATH}")
    public ResponseEntity<?> registerUserDetails(@RequestBody RegisterUserDto registerUserDto){
        return userService.registerUser(registerUserDto);
    }

    @PostMapping(path = "${USER_VERIFICATION_PATH}")
    public ResponseEntity<?> verifyUserDetails(@PathVariable("email") String email,
                                               @PathVariable("token") String token){
        return userService.verifyUser(email);
    }

    @PostMapping(path = "${USER_SEND_VERIFICATION_PATH}")
    public ResponseEntity<?> sendVerificationLink(@PathVariable("userId") long userId,
                                                  @PathVariable("token") String token){
        return userService.sendVerificationLink(userId, token);
    }

    @PostMapping(path="${USER_LOGIN_PATH}")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserDto loginUserDto) {
        return userService.loginUser(loginUserDto);
    }

    @DeleteMapping(path="${USER_DELETE_PATH}")
    public ResponseEntity<?> removeUserDetails(@PathVariable("userId") long userId){
        return userService.deleteUser(userId);
    }

    @PutMapping(path="${USER_EDIT_PATH}")
    public ResponseEntity<?> editUserDetails(@PathVariable("userId") long userId,
                                             @RequestBody EditUserDto editUserDto){
        return userService.editUser(userId,editUserDto);

    }


}