package woos.bookassist.domain.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import woos.bookassist.domain.user.repository.UserRepository;
import woos.bookassist.domain.user.repository.Users;

import java.util.List;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserDetailsMapper userDetailsMapper;

    public DatabaseUserDetailsService(UserRepository userRepository, UserDetailsMapper userDetailsMapper) {
        this.userRepository = userRepository;
        this.userDetailsMapper = userDetailsMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String id)
            throws UsernameNotFoundException {
        Users user = userRepository.findFirstById(id);
        return userDetailsMapper.toUserDetails(user);
    }

    public boolean isUserIdOrEmailAlreadyExists(String id, String email) {
        List<Users> byIdOrEmail = userRepository.findByIdOrEmail(id, email);
        if (byIdOrEmail.size() > 0) {
            return true;
        }
        return false;
    }

    public Long register(Users user) {
        Users saved = userRepository.save(user);
        return saved.getUserNumber();
    }
}