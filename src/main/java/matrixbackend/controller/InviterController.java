package matrixbackend.controller;

import matrixbackend.dto.InviterResponseDTO;
import matrixbackend.service.InviterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@RestController
@RequestMapping("inviter")
public class InviterController {

    @Autowired
    InviterService inviterService;

    @GetMapping
    public ResponseEntity checkIsInviterExist(@RequestParam("username") String username){
       try {
           return ResponseEntity.ok(inviterService.checkIfInviterExist(username));
       }
       catch (ResponseStatusException e){
           HashMap hashMap=new HashMap();
           hashMap.put("message","Пользователь с таким ником не найден");
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(hashMap);
       }
    }
}
