package org.hepi.hepi_sv.user.entity;

import java.time.LocalDateTime;
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
@Table(name = "USERS_META")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersMeta {
    
    @Id
    private int meta_id;

    private UUID user_id;

    private String source;
    
    private LocalDateTime created_at;

    private LocalDateTime last_updated_at;

    private LocalDateTime last_login_at;

    private Boolean is_delete;

    private LocalDateTime deleted_at;
    
}
