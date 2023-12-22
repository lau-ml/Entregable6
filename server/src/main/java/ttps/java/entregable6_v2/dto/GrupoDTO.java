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
public class GrupoDTO {
    private Long id;
    private String nombre;
    private String categoria;
    private Set<String> participantes;
}
