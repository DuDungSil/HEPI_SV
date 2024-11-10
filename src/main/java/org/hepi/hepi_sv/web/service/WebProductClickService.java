package org.hepi.hepi_sv.web.service;

import org.hepi.hepi_sv.web.repository.mybatis.WebDataMapper;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;


@Service
public class WebProductClickService {

    private final WebDataMapper webDataMapper;

    public WebProductClickService(WebDataMapper webDataMapper) {
        this.webDataMapper = webDataMapper;
    }

    public void recordUserProductClickData(int productId, HttpServletRequest servletRequest) {

        String clientIp = getClientIp(servletRequest);
        String userAgent = servletRequest.getHeader("User-Agent");

        webDataMapper.insertWebClickData(productId, clientIp, userAgent);
    }

    public String getClientIp(HttpServletRequest request) {
        String clientIp = null;
    
        // 가장 흔히 사용되는 헤더들을 순차적으로 검사
        String[] headers = {
            "X-Forwarded-For",
            "X-Real-IP",
            "CF-Connecting-IP",        // Cloudflare
            "True-Client-IP",          // Akamai
            "X-Client-IP",
            "X-Cluster-Client-IP",
            "Forwarded-For",
            "Forwarded"
        };
    
        for (String header : headers) {
            clientIp = request.getHeader(header);
            if (clientIp != null && !clientIp.isEmpty() && !"unknown".equalsIgnoreCase(clientIp)) {
                // 여러 개의 IP가 포함된 경우, 첫 번째 IP가 클라이언트 IP임
                clientIp = clientIp.split(",")[0].trim();
                return clientIp;
            }
        }
    
        // 헤더에서 찾지 못했을 때 fallback
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
    
        return clientIp;
    }

}