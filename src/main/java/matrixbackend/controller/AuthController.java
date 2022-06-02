package matrixbackend.controller;

import matrixbackend.dto.AuthRequestDto;
import matrixbackend.dto.AuthResponseDto;
import matrixbackend.entity.User;
import matrixbackend.jwt.JwtTokenProvider;
import matrixbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AuthController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    private PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @PostMapping(value = "/auth/token" ,consumes = {"multipart/form-data"})
    public AuthResponseDto auth(@ModelAttribute AuthRequestDto request) {
        Optional <User> user = userRepository.findByUsername(request.getUsername());
        if(user!=null){
            if(passwordEncoder().matches(request.getPassword(), user.get().getPassword())){
                String token = jwtTokenProvider.createToken(user.get().getUsername());
                return new AuthResponseDto(token);
            }
        }
        throw new UsernameNotFoundException("User doesn't exist");
    }
}
