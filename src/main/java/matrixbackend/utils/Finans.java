package matrixbackend.utils;

import matrixbackend.entity.User;
import matrixbackend.jwt.JwtTokenProvider;
import matrixbackend.repository.UserRepository;
import matrixbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Finans {

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    public boolean IncreaseUserBalance (String token, int sum) {

        String login = jwtTokenProvider.getLoginFromToken(token.substring(7));
        Optional<User> user = userRepository.findByUsername(login);

        int currentBallace = user.get().getBalance();
        user.get().setBalance(currentBallace + sum);
        userRepository.save(user.get());

        return true;
    }
}
