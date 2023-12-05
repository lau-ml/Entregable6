package ttps.java.entregable6_v2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.modelos.Division;
import ttps.java.entregable6_v2.modelos.TipoGasto;
import ttps.java.entregable6_v2.modelos.Usuario;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GastoGrupoDTO {
    private Long id;
    private String nombre;
    private double monto;
    private Long id_grupo;
    private TipoGasto tipo;
    private Date fecha;
    private String imagen;
    private Division division;
    private Set<String> participantes;
    private HashMap<String,Double> valores;
    private String responsable;
    private Long id_responsable;

    public GastoGrupoDTO(Gasto gasto,HashMap<String, Double> valores) {
        this.id = gasto.getId();
        this.nombre = gasto.getNombre();
        this.monto = gasto.getMonto();
        this.id_grupo = gasto.getGrupo() != null ? gasto.getGrupo().getId() : null;
        this.tipo = gasto.getTipo();
        this.fecha = gasto.getFecha();
        this.imagen = gasto.getImagen();
        this.division = gasto.getDivision();
        this.participantes = (gasto.getParticipantes().stream().map(Usuario::getUsuario).collect(Collectors.toSet()));
        this.valores = valores;
        this.responsable = gasto.getResponsable().getUsuario();
        this.id_responsable = gasto.getResponsable().getId();
    }
}
