package org.hepi.hepi_sv.auth.entity;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@RedisHash(value = "jwt", timeToLive = 60 * 60 * 24 * 7)    
public class Token {
    // 저장할 토큰

    @Id
    private final String id;

    private String refreshToken;

    @Indexed
    private String accessToken;

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
}
