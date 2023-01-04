package com.callsign.userauthentication.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb28iLCJleHAiOjE2NzQwNDc5NTAsImlhdCI6MTY3MjY4NTc4NH0.SCuew4pQJEO3KCx5HxzSdGT4395YpfEq_67Jzn5c8Yw";
    @InjectMocks
    private JwtUtil jwtUtil;

    @Test
    void extractUsername() {
        String username = jwtUtil.extractUsername(token);

        assertEquals("foo", username);
        assertThat(username, instanceOf(String.class));
    }

    @Test
    void extractExpiration() {
        Date expiry = jwtUtil.extractExpiration(token);

        assertEquals("Wed Jan 18 18:19:10 PKT 2023", expiry.toString());
        assertThat(expiry, instanceOf(Date.class));
    }

    @Test
    void generateToken() {
        UserDetails userDetails = new User("foo", "pass", Arrays.asList());

        String generatedToken = jwtUtil.generateToken(userDetails);

        assertNotNull(generatedToken);
        assertThat(generatedToken, instanceOf(String.class));
    }

    @Test
    void validateToken() {
        UserDetails userDetails = new User("foo", "pass", Arrays.asList());

        Boolean validate = jwtUtil.validateToken(token, userDetails);

        assertTrue(validate);
        assertThat(validate, instanceOf(Boolean.class));
    }
}