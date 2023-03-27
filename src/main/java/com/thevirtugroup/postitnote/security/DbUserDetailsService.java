package com.thevirtugroup.postitnote.security;

import com.thevirtugroup.postitnote.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * integrate the spring security with our user repository
 */
@Service
public class DbUserDetailsService implements UserDetailsService {
    private static Logger LOG = LoggerFactory.getLogger(DbUserDetailsService.class);

    @Autowired
    private UserRepository userRepo;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SecurityUserWrapper securityUser;

        final com.thevirtugroup.postitnote.model.User user = userRepo.findUserByUsername(email);
        securityUser = new SecurityUserWrapper(
                user.getUsername(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList()
        );
        securityUser.setId(user.getId());
        return securityUser;
    }
}