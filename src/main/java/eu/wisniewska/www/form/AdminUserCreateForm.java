package eu.wisniewska.www.form;

import eu.wisniewska.www.entity.AdminUserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUserCreateForm {
    @NotBlank
    @Size(min = 3, max = 255)
    private String username;
    @NotBlank
    @Size(min = 8, max = 255)
    private String password;
    @NotNull
    private AdminUserRole role;
}
