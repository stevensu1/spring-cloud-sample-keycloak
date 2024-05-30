package com.example.demospringbootkeycloak;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {

    private final KeycloakLogoutHandler keycloakLogoutHandler;

    SecurityConfig(KeycloakLogoutHandler keycloakLogoutHandler) {

        this.keycloakLogoutHandler = keycloakLogoutHandler;
    }

    /**
     * 我们通过创建一个SecurityFilterChain bean来配置HttpSecurity。
     * 此外，我们还需要使用http.oauth2Login()来启用OAuth2登录。
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/**"))
                .authenticated()
                .anyRequest()
                .permitAll());
        httpSecurity.oauth2ResourceServer((oauth2) -> oauth2
                .jwt(Customizer.withDefaults()));
        httpSecurity.oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout.addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/"));

        return httpSecurity.build();
    }

    /**
     * Keycloak返回一个包含所有相关信息的令牌。为了使Spring Security能够根据用户分配的角色做出决策，我们必须解析令牌并提取相关的细节。 然而，Spring
     * Security通常会在每个角色名称前添加“ROLES_”前缀， 而Keycloak发送的是纯角色名称。为了解决这个问题，我们创建一个帮助方法，将从Keycloak检索到的每个角色添加“ROLE_”前缀。
     */
    Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {

        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(
                Collectors.toList());
    }

    /**
     * 解析令牌。首先，我们需要检查令牌是否为OidcUserAuthority或OAuth2UserAuthority的实例。
     * 由于Keycloak令牌可以是任一类型，所以我们需要实现一个解析逻辑。下面的代码会检查令牌的类型，并决定解析机制。
     *
     * @return
     */
    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {

        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            var authority = authorities.iterator().next();
            boolean isOidc = authority instanceof OidcUserAuthority;

            if (isOidc) {
                var oidcUserAuthority = (OidcUserAuthority) authority;
                var userInfo = oidcUserAuthority.getUserInfo();

                // Tokens can be configured to return roles under
                // Groups or REALM ACCESS hence have to check both
                if (userInfo.hasClaim("realm_access")) {
                    var realmAccess = userInfo.getClaimAsMap("realm_access");
                    var roles = (Collection<String>) realmAccess.get("roles");
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                } else if (userInfo.hasClaim("groups")) {
                    Collection<String> roles = userInfo.getClaim("groups");
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            } else {
                var oauth2UserAuthority = (OAuth2UserAuthority) authority;
                Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

                if (userAttributes.containsKey("realm_access")) {
                    Map<String, Object> realmAccess = (Map<String, Object>) userAttributes.get("realm_access");
                    Collection<String> roles = (Collection<String>) realmAccess.get("roles");
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            }
            return mappedAuthorities;
        };
    }
}
