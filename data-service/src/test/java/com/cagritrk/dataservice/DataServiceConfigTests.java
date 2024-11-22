package com.cagritrk.dataservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DataServiceConfigTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAllowAccessToH2ConsolePath() throws Exception {
        mockMvc.perform(get("/h2-console/"))
                .andExpect(result -> assertTrue(result.getResponse().getStatus() != 401, "Unexpected 401 Unauthorized"));
    }

    @Test
    @WithMockUser
    void shouldAllowAccessToAuthenticatedEndpoints() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnUnauthorizedForUnauthenticatedRequests() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isUnauthorized());
    }
}