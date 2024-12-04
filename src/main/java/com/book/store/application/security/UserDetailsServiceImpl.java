package com.book.store.application.security;

import com.book.store.application.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username :" + username);
        return userRepository.findByUsername(username).map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("user with the given Username:" + username + ", not found use correct username"));
    }
}