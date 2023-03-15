package com.pizzurg.api.config.security;

import com.pizzurg.api.config.exception.UserNotFounException;
import com.pizzurg.api.config.security.UserDetailsImpl;
import com.pizzurg.api.entity.User;
import com.pizzurg.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(UserNotFounException::new);
        return new UserDetailsImpl(user);
    }
}
