package eu.wisniewska.www.form;

import eu.wisniewska.www.entity.AdminUser;
import eu.wisniewska.www.entity.AdminUserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class AdminUserEditForm {

    public AdminUserEditForm(AdminUser adminUser) {
        this.id = adminUser.getId();
        this.username = adminUser.getUsername();
        this.role = adminUser.getRole();
    }

    private UUID id;
    private String username;
    private String password;
    @NotNull(message = "Role is required")
    private AdminUserRole role;
}
