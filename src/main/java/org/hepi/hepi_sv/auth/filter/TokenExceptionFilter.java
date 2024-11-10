package org.hepi.hepi_sv.auth.filter;

import org.hepi.hepi_sv.auth.exception.TokenException;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TokenExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException, java.io.IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            response.sendError(e.getErrorCode().getHttpStatus().value(), e.getMessage());
        }
    }
}
