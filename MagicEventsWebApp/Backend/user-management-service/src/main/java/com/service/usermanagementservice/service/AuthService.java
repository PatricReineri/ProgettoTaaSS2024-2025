package com.service.usermanagementservice.service;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.service.usermanagementservice.dto.LoginWithTokenDTO;
import com.service.usermanagementservice.dto.UserDTO;
import com.service.usermanagementservice.model.OauthToken;
import com.service.usermanagementservice.model.User;
import com.service.usermanagementservice.repository.OauthTokenRepository;
import com.service.usermanagementservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OauthTokenRepository tokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String clientSecret;

    public String registerUser(String name, String surname, String email, String password, String username) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setUsername(username);
        user.setRole("USER");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        String response;
        User alreadyExists = userRepository.findByEmail(email);
        if (alreadyExists == null) {
            userRepository.save(user);
            response = "User saved";
        }else {
            response = "User already exists";
        }
        return response;
    }

    public UserDTO login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if(user != null && passwordEncoder.matches(password, user.getPassword())){
            OauthToken oauthToken = tokenRepository.findByUser(user);
            if(oauthToken == null) {
                saveTokenForUser(user);
            }
            return new UserDTO(
                    user.getMagicEventTag(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getProfileImageUrl(),
                    user.getName(),
                    user.getSurname(),
                    user.getRole()
            );
        }

        throw new BadCredentialsException("Invalid username or password");
    }

    public UserDTO processGrantCode(String code) {
        String accessToken = getOauthAccessTokenGoogle(code);

        User googleUser = getProfileDetailsGoogle(accessToken);
        User user = userRepository.findByEmail(googleUser.getEmail());

        if (user == null) {
            googleUser.setRole("USER");
            if (googleUser.getUsername() == null || googleUser.getUsername().isEmpty()) {
                String generatedUsername = googleUser.getEmail().split("@")[0];
                googleUser.setUsername(generatedUsername);
            }
            user = userRepository.save(googleUser);
        }

        OauthToken oauthToken = tokenRepository.findByUser(user);
        if (oauthToken != null) {
            tokenRepository.delete(oauthToken);
        }

        saveTokenForUser(user);
        return new UserDTO(
                user.getMagicEventTag(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getName(),
                user.getSurname(),
                user.getRole()
        );
    }

    private LoginWithTokenDTO generateToken() {
        LoginWithTokenDTO res = new LoginWithTokenDTO();
        res.setAccessToken(UUID.randomUUID().toString());
        res.setRefreshToken(UUID.randomUUID().toString());
        res.setExpirationTime(LocalDateTime.now().plusHours(1));
        return res;
    }

    private LoginWithTokenDTO saveTokenForUser(User user) {
        LoginWithTokenDTO dto = generateToken();
        OauthToken token = new OauthToken();
        token.setAccessToken(dto.getAccessToken());
        token.setRefreshToken(dto.getRefreshToken());
        token.setExpirationTime(dto.getExpirationTime());
        token.setUser(user);
        tokenRepository.save(token);
        return dto;
    }

    private User getProfileDetailsGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);

        User user = new User();

        user.setEmail(jsonObject.get("email").getAsString());
        user.setUsername(jsonObject.get("email").getAsString().split("@")[0]);
        user.setName(jsonObject.has("given_name") ? jsonObject.get("given_name").getAsString() : "");
        user.setSurname(jsonObject.has("family_name") ? jsonObject.get("family_name").getAsString() : "");
        user.setProfileImageUrl(jsonObject.has("picture") ? jsonObject.get("picture").getAsString() : "");
        user.setRole("USER");

        return user;
    }
    private String getOauthAccessTokenGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("redirect_uri", "https://localhost:8443/login/grantcode");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
        params.add("scope", "openid");
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        String url = "https://oauth2.googleapis.com/token";
        String response = restTemplate.postForObject(url, requestEntity, String.class);
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);

        return jsonObject.get("access_token").toString().replace("\"", "");
    }

    public LoginWithTokenDTO refreshAccessToken(String refreshToken) {
        OauthToken oauthToken = tokenRepository.findByRefreshToken(refreshToken);
        if(oauthToken == null) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        LoginWithTokenDTO res = saveTokenForUser(oauthToken.getUser());
        tokenRepository.delete(oauthToken);
        return res;
    }

    public String logout(String email) {
        User user = userRepository.findByEmail(email);
        OauthToken oauthToken = tokenRepository.findByUser(user);
        tokenRepository.delete(oauthToken);
        return "Signed out successfully";
    }
}
