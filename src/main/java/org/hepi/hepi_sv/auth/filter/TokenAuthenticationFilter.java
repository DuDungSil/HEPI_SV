package org.hepi.hepi_sv.auth.filter;

import org.hepi.hepi_sv.auth.jwt.TokenHeader;
import org.hepi.hepi_sv.auth.jwt.TokenProvider;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    // 각 요청마다 한번씩 실행 : 토큰추출 -> 토근 검증 and 재발급 -> 다음 필터로 요청 전달
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        String accessToken = resolveToken(request);

        // accessToken 검증
        if (tokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken);
        } else {
            // 만료되었을 경우 accessToken 재발급
            String reissueAccessToken = tokenProvider.reissueAccessToken(accessToken);

            if (StringUtils.hasText(reissueAccessToken)) {
                setAuthentication(reissueAccessToken);

                // 재발급된 accessToken 다시 전달
                response.setHeader(AUTHORIZATION, TokenHeader.TOKEN_PREFIX + reissueAccessToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    // 유효한 accessToken을 기반으로 Authentication 객체를 설정
    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // 요청에서 JWT 토큰을 추출
    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (ObjectUtils.isEmpty(token) || !token.startsWith(TokenHeader.TOKEN_PREFIX)) {
            return null;
        }
        return token.substring(TokenHeader.TOKEN_PREFIX.length());
    }
}
