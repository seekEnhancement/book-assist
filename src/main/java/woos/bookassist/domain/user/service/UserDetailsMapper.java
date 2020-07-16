package woos.bookassist.domain.user.service;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import woos.bookassist.domain.user.repository.Users;

@Component
public class UserDetailsMapper {

    public UserDetails toUserDetails(Users user) {
        return new User(
                user.getId(), user.getPassword(), true, true, true,
                true, makeDefaultUserAuthority());
    }

    private Collection<? extends GrantedAuthority> makeDefaultUserAuthority() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }
}
