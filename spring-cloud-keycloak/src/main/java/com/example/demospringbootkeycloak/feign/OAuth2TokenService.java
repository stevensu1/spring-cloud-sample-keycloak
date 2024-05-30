package com.example.demospringbootkeycloak.feign;

import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Service;

/**
 * 获取 OAuth2 令牌。
 */
@Service
public class OAuth2TokenService {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    public OAuth2TokenService(ClientRegistrationRepository clientRegistrationRepository,
                              OAuth2AuthorizedClientRepository authorizedClientRepository) {
        this.authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);
    }

    public String getAccessTokenForClientCredentials() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("keycloak-client-credentials")
                .principal("client_credentials")
                .build();
        OAuth2AuthorizedClient authorizedClient = this.authorizedClientManager.authorize(authorizeRequest);
        return authorizedClient != null ? authorizedClient.getAccessToken().getTokenValue() : null;
    }
}
