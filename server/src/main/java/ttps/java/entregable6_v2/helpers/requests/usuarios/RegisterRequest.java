package ttps.java.entregable6_v2.helpers.requests.usuarios;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    String username;
    String password;
    String confirmPassword;
    String nombre;
    String apellido;
    String email;

    public boolean isValid() {
        return username != null && password != null && nombre != null && apellido != null && email != null
                && !username.isEmpty() && !password.isEmpty() && !nombre.isEmpty() && !apellido.isEmpty()
                && !email.isEmpty() && password.equals(confirmPassword);
    }
}

