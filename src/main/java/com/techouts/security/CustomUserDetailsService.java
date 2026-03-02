package com.techouts.security;

import com.techouts.entity.User;
import com.techouts.repository.UserRespository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRespository userRespository;

    public CustomUserDetailsService(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identity) throws UsernameNotFoundException {
        String normalizedIdentity = identity == null ? "" : identity.trim();

        Optional<User> user = userRespository.findByUsername(normalizedIdentity);
        if (user.isEmpty()) {
            user = userRespository.findByEmail(normalizedIdentity.toLowerCase());
        }
        if (user.isEmpty()) {
            user = userRespository.findByPhone(normalizedIdentity);
        }

        return user.map(CustomUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials."));
    }
}
