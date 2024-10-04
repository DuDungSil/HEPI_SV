package org.hepi.hepi_sv.model.vo;

import lombok.Data;

@Data
public class User {
    private String name;
    private String id;
    private String pwd;
    private String phone;
    private int gym_id;

    public User(String name, String id, String pwd, String phone, int gym_id) {
        this.name = name;
        this.id = id;
        this.pwd = pwd;
        this.phone = phone;
        this.gym_id = gym_id;
    }

    @Override
    public String toString() {
        return "NAME : " + this.name
             + ", ID : " + this.id
             + ", PHONE : " + this.phone
             + ", GYM_ID : " + this.gym_id;
    }
}
