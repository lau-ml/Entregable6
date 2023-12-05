package ttps.java.entregable6_v2.helpers.requests.usuarios;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    String email;
    String password;

    public boolean isValid() {
        return email != null && !email.isEmpty() && password != null && !password.isEmpty();
    }
}
