package org.hepi.hepi_sv.vo;

import lombok.Data;

@Data
public class Chatting {
    private String time;
    private String gym_name;
    private String user_id;
    private String user_name;
    private String message;

    public Chatting(String time, String gym_name, String user_id, String user_name, String message) {
        this.time = time;
        this.gym_name = gym_name;
        this.user_id = user_id;
        this.user_name = user_name;
        this.message = message;
    }
}
