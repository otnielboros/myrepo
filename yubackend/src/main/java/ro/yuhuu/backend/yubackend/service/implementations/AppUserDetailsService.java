package ro.yuhuu.backend.yubackend.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ro.yuhuu.backend.yubackend.model.User;
import ro.yuhuu.backend.yubackend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetailsEntity loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> userUsername  = userRepository.findByUsername(s);
        Optional<User> userEmail = userRepository.findByEmail(s);
        User user = null;

        if(userUsername.isPresent()){
            user=userUsername.get();
        }
        else if(userEmail.isPresent()){
            user=userEmail.get();
        }


        if(user==null) {
            throw new UsernameNotFoundException("The user doesn't exist");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleString().toString()));
        });

        UserDetailsEntity userDetails = new UserDetailsEntity(user);
        return userDetails;
    }
}
