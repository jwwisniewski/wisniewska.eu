package eu.wisniewska.www.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admin_users", indexes = {
        @Index(name = "username_idx", columnList = "username", unique = true)
})
@Data
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 3, max = 255, message = "Password must be between 3 and 255 characters")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "Role cannot be blank")
    @Pattern(regexp = "ADMIN|USER", message = "Role must be either ADMIN or USER")
    private String role;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column()
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
