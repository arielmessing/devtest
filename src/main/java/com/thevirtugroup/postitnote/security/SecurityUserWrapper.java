package com.thevirtugroup.postitnote.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecurityUserWrapper extends User {

    private Long id;

    public SecurityUserWrapper(String username, String password, Collection<? extends GrantedAuthority>
            authorities) {
        super(username, password, authorities);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}