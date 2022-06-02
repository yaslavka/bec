package matrixbackend.service;


import matrixbackend.dto.CreateUserDTO;
import matrixbackend.dto.UserDTO;
import matrixbackend.entity.User;
import matrixbackend.jwt.JwtTokenProvider;
import matrixbackend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {


    @Value("${front.urlHelper}")
    private String clientHelper;

    private final String signUpPath="/sign-up?ref=";

    @Autowired
    UserRepository userRepository;



    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public Map createNewUser(CreateUserDTO userDTO){

        Optional<User> checkLoginIsNotTaken=userRepository.findByUsername(userDTO.getUsername());
        HashMap map=new HashMap<>();
        if(checkLoginIsNotTaken.isPresent()){
            map.put("message","Логин занят");
            return  map;
        }

        Optional<User> checkEmailIsNotTaken= userRepository.findUserByEmail(userDTO.getEmail());
        if(checkEmailIsNotTaken.isPresent()){
            map.put("message","Почта занята");
            return  map;
        }

        Optional<User> checkIsPhoneIsNotTaken= userRepository.findUserByPhone(userDTO.getPhone());
        if(checkIsPhoneIsNotTaken.isPresent()){
            map.put("message","Телефон занят");
            return map;
        }
        else {
            Optional<User> findReferral=userRepository.findByUsername(userDTO.getReferral());
            User user=modelMapper.map(userDTO, User.class);
            user.setBalance(0);
            user.setReferral(findReferral.get());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setRefLink(clientHelper+signUpPath+user.getUsername());
            findReferral.get().setUserOnLinks(+1);
            userRepository.save(user);
            userRepository.save(findReferral.get());
            map.put("status",true);
        }
        return  map;
    }

    public UserDTO getFullInformation(String token){
        String username= jwtTokenProvider.getLoginFromToken(token.substring(7));
        Optional <User> user=userRepository.findByUsername(username);
        UserDTO userDTO=new UserDTO(user.get());
        return userDTO;
    }
}
