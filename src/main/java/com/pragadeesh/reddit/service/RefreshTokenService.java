package com.pragadeesh.reddit.service;

import com.pragadeesh.reddit.exceptions.SpringRedditException;
import com.pragadeesh.reddit.model.RefreshToken;
import com.pragadeesh.reddit.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {


    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public void validateRefreshToken(String token){
        refreshTokenRepository.findByToken(token).orElseThrow(() -> new SpringRedditException("Invalid token "+ token));
    }

    @Transactional
    public void deleteRefreshToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }

}
