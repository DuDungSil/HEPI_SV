package org.hepi.hepi_sv.user.service;

import java.util.UUID;

import org.hepi.hepi_sv.auth.dto.OAuth2UserInfo;
import org.hepi.hepi_sv.user.entity.Users;
import org.hepi.hepi_sv.user.repository.jpa.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;

    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean isUserExists(String provider, String providerId) {
        return userRepository.existsByProviderAndProviderId(provider, providerId);
    }

    public UUID getUserUuid(String provider, String providerId) {
        return userRepository.findUserIdByProviderAndProviderId(provider, providerId);
    }
    
    public Users registerOrLoadUser(OAuth2UserInfo oAuth2UserInfo) {


        Users user = userRepository.findUsersByProviderAndProviderId(oAuth2UserInfo.provider(), oAuth2UserInfo.providerId())
                     .orElseGet(oAuth2UserInfo::toEntity);

        return userRepository.save(user);
    }
    
    public void deletionUser() {
        
    }

}
