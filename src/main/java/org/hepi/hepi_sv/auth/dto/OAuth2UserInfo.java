package org.hepi.hepi_sv.auth.dto;

import static org.hepi.hepi_sv.common.errorHandler.CustomErrorCode.ILLEGAL_REGISTRATION_PROVIDER;

import java.util.Map;

import org.hepi.hepi_sv.auth.exception.AuthException;
import org.hepi.hepi_sv.user.entity.Role;
import org.hepi.hepi_sv.user.entity.Users;

import lombok.Builder;

// 유저 정보를 각 provider로부터
@Builder
public record OAuth2UserInfo(
    String provider,
    String providerId,
    String name,
    String email
) {
    
    public static OAuth2UserInfo of(String provider, Map<String, Object> attributes) {
        return switch (provider) { // registration id별로 userInfo 생성
            case "google" -> ofGoogle(provider, attributes);
            case "kakao" -> ofKakao(provider, attributes);
            default -> throw new AuthException(ILLEGAL_REGISTRATION_PROVIDER);
        };
    }
    
    private static OAuth2UserInfo ofGoogle(String provider, Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .provider(provider)
                .providerId((String) attributes.get("sub").toString())
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .build();
    }

    private static OAuth2UserInfo ofKakao(String provider, Map<String, Object> attributes) {
        // @SuppressWarnings("unchecked")
        // Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        // @SuppressWarnings("unchecked")
        // Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .provider(provider)
                .providerId(attributes.get("id").toString())
                // .name((String) profile.get("nmickname"))
                // .email((String) account.get("email"))
                .name("test")
                .email("test@example.com")
                .build();
    }

    public Users toEntity() {
        return Users.builder()
                .provider(provider)
                .providerId(providerId)
                .name(name)
                .email(email)
                .role(Role.USER)
                .build();
    }

}
