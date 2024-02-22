package ttps.java.entregable6_v2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String usuario;
    private String email;
    private String nombre;
    private Double saldo;
    private String apellido;
    private Set<String> amigos;
}
