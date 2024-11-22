package com.cagritrk.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthorizationServerSecurityFilterChainTest {


    @Autowired
    @Qualifier("authorizationServerSecurityFilterChain")
    private SecurityFilterChain securityFilterChain;

    @Test
    void securityFilterChainShouldBeConfigured() {
        // SecurityFilterChain should not be null
        assertThat(securityFilterChain).isNotNull();
    }
}