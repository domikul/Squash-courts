package pl.euvic.squash.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.euvic.squash.exception.WrongRequestException;
import pl.euvic.squash.model.entity.User;
import pl.euvic.squash.model.repository.UserRepository;

import static java.util.Collections.emptyList;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            User user = userRepository.findByEmail(username).get();
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), emptyList());
        } catch (WrongRequestException e) {
            throw new WrongRequestException("Wrong email");
        }
    }
}