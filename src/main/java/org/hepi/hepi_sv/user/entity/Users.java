package org.hepi.hepi_sv.user.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)") 
    private UUID user_id;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(name = "role")
    private Role role = Role.USER; // 기본값을 USER로 설정

    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    private String email;
    
    private String name;

}
