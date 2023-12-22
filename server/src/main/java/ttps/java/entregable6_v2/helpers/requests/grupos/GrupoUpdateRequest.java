package ttps.java.entregable6_v2.helpers.requests.grupos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ttps.java.entregable6_v2.modelos.Categoria;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrupoUpdateRequest {
    private String nombre;
    private Categoria categoria;

}
