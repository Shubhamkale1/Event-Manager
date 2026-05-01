package com.shubham.event_manager.security;

import com.shubham.event_manager.entity.User;
import com.shubham.event_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService
        extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User =
                super.loadUser(userRequest);

        String email = oAuth2User
                .getAttribute("email");
        String name = oAuth2User
                .getAttribute("name");

        userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .name(name)
                            .password("OAUTH2_USER")
                            .role("USER")
                            .build();
                    userRepository.save(newUser);
                    log.info("New OAuth2 user created: {}",
                            email);
                    return newUser;
                });

        return oAuth2User;
    }
}
