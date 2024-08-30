package org.hepi.hepi_sv.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collection;
import java.util.Enumeration;

@Component
public class FltInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(FltInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder headers = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.append(headerName).append(": ").append(headerValue).append("\n");
        }

        logger.info("==================== REQUEST ====================\nClient IP: {}:{} \nRequest URL: {} \nHTTP Method: {} \n[Headers]: \n{}",
                request.getRemoteAddr(),
                request.getRemotePort(),
                request.getRequestURL().toString(),
                request.getMethod(),
                headers.toString()
                );

        response.addHeader("client-ip", request.getRemoteAddr()+":"+request.getRemotePort());
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        Collection<String> headerNames = response.getHeaderNames();
        StringBuilder headers = new StringBuilder();
        for (String headerName : headerNames) {
            String headerValue = response.getHeader(headerName);
            headers.append(headerName).append(": ").append(headerValue).append("\n");
        }

        String userJson = (String) request.getAttribute("userJson");
        logger.info("==================== RESPONSE ====================\nStatus: {} \n[Headers]: \n{} \n[Content]: \n{}\n",
                response.getStatus(),
                headers.toString(),
                (String) request.getAttribute("content")
        );

    }
}
