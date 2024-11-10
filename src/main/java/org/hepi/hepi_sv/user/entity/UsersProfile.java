package org.hepi.hepi_sv.user.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USERS_PROFILE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersProfile {

    @Id
    private int profile_id;

    private UUID user_id;

    private String nickname;

    private String gender;

    private int age;

    private double height;
    
    private double weight;

    private LocalDate birth_date;

    private String profile_img;

    private String intro_text;

}
