package ttps.java.entregable6_v2.helpers.requests.usuarios;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    @Email(message = "El email debe ser válido")
    String email;
}
