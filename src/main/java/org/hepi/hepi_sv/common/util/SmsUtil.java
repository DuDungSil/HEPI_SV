package org.hepi.hepi_sv.common.util;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Component
public class SmsUtil {
    Environment env = (Environment)ApplicationContextProvider.getBean(Environment.class);

    // 단일 메시지 발송 예제
    public void sendOne(String to, String verificationCode) throws Exception {
        String apiKey = env.getProperty("coolsms.api.key");
        String apiSecretKey = env.getProperty("coolsms.api.secret");

        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");

        Message message = new Message();
        message.setFrom("01043493573");
        message.setTo(to);
        message.setText("[HEPI] 아래의 인증번호를 입력해주세요\n" + verificationCode);

        messageService.send(message);
    }
}