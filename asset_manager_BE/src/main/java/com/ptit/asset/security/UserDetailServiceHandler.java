package com.ptit.asset.security;

import com.ptit.asset.domain.User;
import com.ptit.asset.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Service
@Transactional
public class UserDetailServiceHandler implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .getOrElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return UserPrinciple.build(user);
    }


}
