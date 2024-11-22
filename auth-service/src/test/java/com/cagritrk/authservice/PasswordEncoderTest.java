package com.cagritrk.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordShouldBeEncodedCorrectly() {
        String rawPassword = "test-password-13ASD!@#?>M]";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Assertions
        assertThat(encodedPassword).isNotNull();
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }
}