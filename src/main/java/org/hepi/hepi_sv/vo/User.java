package org.hepi.hepi_sv.vo;

import lombok.Data;

@Data
public class User {
    private String name;
    private String id;
    private String pwd;
    private String phone;
    private String gym;

    public User(String name, String id, String pwd, String phone, String gym) {
        this.name = name;
        this.id = id;
        this.pwd = pwd;
        this.phone = phone;
        this.gym = gym;
    }
}
