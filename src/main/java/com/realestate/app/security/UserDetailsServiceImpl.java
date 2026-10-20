package com.realestate.app.security;

import com.realestate.app.entity.User;
import com.realestate.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Custom {@link UserDetailsService} that loads users by their UUID (stored as subject in JWT).
 * Spring Security uses this to build the authentication principal.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * @param username the UUID string of the user (JWT subject)
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(java.util.UUID.fromString(username))
            .orElseThrow(() -> new UsernameNotFoundException(
                "User not found with id: " + username));

        return new org.springframework.security.core.userdetails.User(
            user.getId().toString(),
            "",  // No password — stateless JWT auth
            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
