package github.ricemonger.users_ubi_accs_reauthorizer.services.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUnauthorizedUbiAccount {
    private Long id;
    private String email;
}
