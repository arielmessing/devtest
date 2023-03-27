package com.thevirtugroup.postitnote.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContext {

    public static SecurityUserWrapper getLoggedInUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof SecurityUserWrapper)
                return (SecurityUserWrapper) principal;
        }

        final String errorMsg = "No user has been authenticated on this request";
        throw new IllegalStateException(errorMsg);
    }
}