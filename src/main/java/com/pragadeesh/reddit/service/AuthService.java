package com.pragadeesh.reddit.service;

import com.pragadeesh.reddit.dto.RefreshTokenRequest;
import com.pragadeesh.reddit.repository.UserTableRepository;
import com.pragadeesh.reddit.security.JwtProvider;
import com.pragadeesh.reddit.dto.AuthenticationResponse;
import com.pragadeesh.reddit.dto.LoginRequest;
import com.pragadeesh.reddit.dto.RegisterRequest;
import com.pragadeesh.reddit.exceptions.SpringRedditException;
import com.pragadeesh.reddit.model.NotificationEmail;
import com.pragadeesh.reddit.model.UserTable;
import com.pragadeesh.reddit.model.VerificationToken;
import com.pragadeesh.reddit.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserTableRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        UserTable user = new UserTable();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        String verificationToken = generateVerificationToken(user);
        mailService.sendEmail(new NotificationEmail("Please activate your Account",
                user.getEmail(),
                "Thank you for signing up Reddit clone. <br./> Please click the below link to activate <br/>" +
                        "http://localhost:8080/api/auth/accountVerification/" + verificationToken));
    }

    private String generateVerificationToken(UserTable user) {
        String verificationToken = UUID.randomUUID().toString();
        VerificationToken token = new VerificationToken();
        token.setToken(verificationToken);
        token.setUser(user);
        verificationTokenRepository.save(token);
        return verificationToken;
    }

    public void verifyToken(String token) {
        Optional<VerificationToken> verifyToken =  verificationTokenRepository.findByToken(token);
        verifyToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verifyToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUsername();
        UserTable user =  this.userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with username" + username));;
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(this.refreshTokenService.generateRefreshToken().getToken())
                .username(loginRequest.getUsername())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .build();
    }

    @Transactional(readOnly = true)
    public UserTable getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .username(refreshTokenRequest.getUsername())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .refreshToken(refreshTokenRequest.getRefreshToken()).build();
    }
}
