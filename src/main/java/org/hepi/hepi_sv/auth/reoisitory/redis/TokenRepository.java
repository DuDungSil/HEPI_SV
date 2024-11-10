package org.hepi.hepi_sv.auth.reoisitory.redis;

import java.util.Optional;

import org.hepi.hepi_sv.auth.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {

    // 액세스 토큰으로 리프레시 토큰 찾기?
    Optional<Token> findByAccessToken(String accessToken);
}
