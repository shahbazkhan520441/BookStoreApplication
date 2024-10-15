package com.book.store.application.security;

import com.book.store.application.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

   private final String username;
   private final String password;
   private final List<GrantedAuthority> grantedAuthorities;

    public UserDetailsImpl(User user) {
        username = user.getUsername();
        password = user.getPassword();
        grantedAuthorities = List.of(new SimpleGrantedAuthority(user.getUserRole().name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
