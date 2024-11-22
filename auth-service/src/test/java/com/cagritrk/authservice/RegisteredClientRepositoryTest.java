package com.cagritrk.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RegisteredClientRepositoryTest {

	@Autowired
	private RegisteredClientRepository registeredClientRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${spring.security.gateway-client.secret-key}")
	private String gatewayClientSecret;

	@Value("${spring.security.gateway-client.id}")
	private String gatewayClientId;

	@Value("${spring.security.gateway-client.scope}")
	private String scope;

	@Test
	void shouldRegisterGatewayClient() {
		RegisteredClient registeredClient = registeredClientRepository.findByClientId(gatewayClientId);
		assertThat(registeredClient).isNotNull();

		assertThat(registeredClient.getClientId()).isEqualTo(gatewayClientId);
		assertThat(passwordEncoder.matches(gatewayClientSecret, registeredClient.getClientSecret())).isTrue();
		assertThat(registeredClient.getScopes()).containsExactlyInAnyOrder(scope);
	}

}
