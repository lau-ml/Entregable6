package ttps.java.entregable6_v2.helpers.requests.grupos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ttps.java.entregable6_v2.modelos.Categoria;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrupoCreateRequest {
    private String nombreGrupo;
    private Categoria categoria;
    private List<String> participantes;
}
