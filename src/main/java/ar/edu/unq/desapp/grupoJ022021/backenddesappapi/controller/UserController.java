package ar.edu.unq.desapp.grupoJ022021.backenddesappapi.controller;

import ar.edu.unq.desapp.grupoJ022021.backenddesappapi.config.JwtTokenUtil;
import ar.edu.unq.desapp.grupoJ022021.backenddesappapi.aspects.ExceptionAspect;
import ar.edu.unq.desapp.grupoJ022021.backenddesappapi.dto.LoginUserDto;
import ar.edu.unq.desapp.grupoJ022021.backenddesappapi.dto.UserRegisterDto;
import ar.edu.unq.desapp.grupoJ022021.backenddesappapi.dto.UserResultDto;
import ar.edu.unq.desapp.grupoJ022021.backenddesappapi.exceptions.UserAlreadyExistsException;
import ar.edu.unq.desapp.grupoJ022021.backenddesappapi.exceptions.UserDoesntExistException;
import ar.edu.unq.desapp.grupoJ022021.backenddesappapi.exceptions.ValidationException;
import ar.edu.unq.desapp.grupoJ022021.backenddesappapi.model.JwtResponse;
import ar.edu.unq.desapp.grupoJ022021.backenddesappapi.configKeyValue.KeyValueSaver;
import ar.edu.unq.desapp.grupoJ022021.backenddesappapi.service.UserService;
import ar.edu.unq.desapp.grupoJ022021.backenddesappapi.wrapper.UserDetail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "Users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ExceptionAspect
    @ApiOperation(value = "register")
    @PostMapping(value="/api/auth/register")
    public ResponseEntity<JwtResponse> register(@Valid @RequestBody UserRegisterDto user) throws ValidationException, UserAlreadyExistsException {
           UserDetail userDetail = (UserDetail) userService.registerUser(user);
           String token = jwtTokenUtil.generateToken(userDetail);
           JwtResponse response= new JwtResponse(userDetail.getId(),userDetail.getUsername(),token);
           KeyValueSaver.putKeyValue(token,userDetail.getId());
           return ResponseEntity.ok().body(response);



    }

    @ExceptionAspect
    @ApiOperation(value = "login")
    @PostMapping(value="/api/auth/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginUserDto user) throws UserDoesntExistException {
            UserDetail userDetail = (UserDetail) userService.login(user);
            String token = jwtTokenUtil.generateToken(userDetail);
            JwtResponse response= new JwtResponse(userDetail.getId(),userDetail.getUsername(),token);
            KeyValueSaver.putKeyValue(token,userDetail.getId());
            return ResponseEntity.ok().body(response);
        }


    @GetMapping(value="/api/users")
    public ResponseEntity<List<UserResultDto>> getUsers() {
     List<UserResultDto> users= userService.getUsers();
        return ResponseEntity.ok().body(users);
    }



}
