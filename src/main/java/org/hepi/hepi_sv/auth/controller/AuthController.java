package org.hepi.hepi_sv.auth.controller;

import org.hepi.hepi_sv.auth.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    // OAuth2 로그인 성공 후 콜백
    @GetMapping("/success")
    public ResponseEntity<TokenResponse> loginSuccess(@Validated TokenResponse tokenResponse) {
        return ResponseEntity.ok(tokenResponse);
    }

    // 로그아웃
    // 인증 객체 제거
    // 레디스의 리프레시 토큰 제거
    // @GetMapping("/logout")
    // public ResponseEntity<Void> logout(@Validated TokenResponse tokenResponse) {
    //        tokenService.deleteRefreshToken(userDetails.getUsername());
    //         redisMessageService.removeSubscribe(userDetails.getUsername());
    //         return ResponseEntity.noContent().build();
    // }
    
    // @GetMapping("/unlink")
    // public ResponseEntity<TokenResponse> unlink(@Validated TokenResponse tokenResponse) {
    //     return ResponseEntity.ok(tokenResponse);
    // }  

}