package com.thevirtugroup.postitnote.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JsonAuthenticationFilter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            LoginRequest loginRequest = this.getLoginRequest(request);
            return doLogin(request, loginRequest.username, loginRequest.password);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }

    private Authentication doLogin(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private LoginRequest getLoginRequest(HttpServletRequest request) throws IOException {
        final LoginRequest loginRequest;
        if (request.getContentLength() > 0) {
            try (BufferedReader reader = request.getReader()) {
                loginRequest = objectMapper.readValue(reader, LoginRequest.class);
            } catch (IOException e) {
                LOG.warn("Json /login request is invalid", e);
                throw e;
            }
        } else {
            loginRequest = new LoginRequest();
            loginRequest.setUsername(request.getParameter(getUsernameParameter()));
            loginRequest.setPassword(request.getParameter(getPasswordParameter()));
        }
        return loginRequest;
    }

    static class LoginRequest {
        private String username;
        private String password;

        LoginRequest() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}