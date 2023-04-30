package com.pizzurg.api.security.userdetails;

import com.pizzurg.api.exception.UserNotFoundException;
import com.pizzurg.api.entities.User;
import com.pizzurg.api.repositories.UserRepository;
import com.pizzurg.api.security.userdetails.UserDetailsImpl;
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
        User user = userRepository.findByEmail(username).orElseThrow(UserNotFoundException::new);
        return new UserDetailsImpl(user);
    }

}
