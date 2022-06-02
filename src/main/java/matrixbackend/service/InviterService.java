package matrixbackend.service;

import matrixbackend.dto.InviterResponseDTO;
import matrixbackend.entity.User;
import matrixbackend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class InviterService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;
    public InviterResponseDTO checkIfInviterExist(String username){
        Optional<User> userExist=userRepository.findByUsername(username);
        if(userExist.isEmpty()){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        InviterResponseDTO inviterResponseDTO=new InviterResponseDTO(userExist.get());
        return  inviterResponseDTO;
    }
}
