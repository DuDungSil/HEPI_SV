package org.hepi.hepi_sv.user.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.hepi.hepi_sv.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    
    // 특정 제공자와 제공자 ID로 사용자 존재 여부 확인 메서드
    boolean existsByProviderAndProviderId(String provider, String providerId);

    // 특정 제공자와 제공자 ID로 Users 객체 반환
    Optional<Users> findUsersByProviderAndProviderId(String provider, String providerId);

    // 특정 제공자와 제공자 ID로 사용자 user_id 반환
    @Query("SELECT e.user_id FROM Users e WHERE e.provider = :provider and e.providerId = :providerId")
    UUID findUserIdByProviderAndProviderId(String provider, String providerId);
    
}
