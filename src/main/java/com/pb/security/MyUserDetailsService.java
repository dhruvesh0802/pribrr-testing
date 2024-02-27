package com.pb.security;

import com.pb.model.UserEntity;
import com.pb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User and password wrong"));
        
        return new User(email, "", getAuthority(userEntity));
        
    }
    
    private List<SimpleGrantedAuthority> getAuthority(UserEntity userEntity) {
        List<SimpleGrantedAuthority> userRoleEntity = new ArrayList<>();
        userRoleEntity.add(new SimpleGrantedAuthority(userEntity.getUserType()));
        return userRoleEntity;
    }
}

