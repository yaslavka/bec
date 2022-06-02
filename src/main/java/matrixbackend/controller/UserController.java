package matrixbackend.controller;

import matrixbackend.dto.CreateUserDTO;
import matrixbackend.dto.UserDTO;

import matrixbackend.service.StorageService;
import matrixbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    StorageService storageService;

    @Autowired
    ResourceLoader resourceLoader;

    @PostMapping("/registration")
    @ResponseBody
    public ResponseEntity<Map> createUser(@Valid @RequestBody CreateUserDTO userDTO){
            Map map=userService.createNewUser(userDTO);
            if(
                    map.containsValue("Почта занята")
                            || map.containsValue("Логин занят")
                            || map.containsValue("Номер занят")){
                return new ResponseEntity<>(map,HttpStatus.CONFLICT);
            }
            return ResponseEntity.ok(map);
    }

    @GetMapping
    public UserDTO getFullInformation(@RequestHeader (name="Authorization") String token){
        return userService.getFullInformation(token);
    }

    @PostMapping(value = "/avatar")
    public ResponseEntity uploadAvatar(@RequestParam MultipartFile avatar,@RequestHeader (name="Authorization") String token) throws IOException {
        if(avatar.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        storageService.store(avatar,token);
        return ResponseEntity.ok("Success");
    }
}
