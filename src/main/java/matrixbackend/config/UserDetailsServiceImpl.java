package matrixbackend.config;


import matrixbackend.entity.User;
import matrixbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> checkUser = userRepository.findByUsername(userName);
        if(checkUser.isEmpty()){
            throw new UsernameNotFoundException("User with this login not found");
        }else return new UserDetailsImpl(checkUser);
    }
}
