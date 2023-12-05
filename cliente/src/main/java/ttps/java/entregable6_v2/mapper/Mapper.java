package ttps.java.entregable6_v2.mapper;

import org.springframework.stereotype.Component;
import ttps.java.entregable6_v2.dto.GastoGrupoDTO;
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.modelos.Usuario;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Mapper {
    public GastoGrupoDTO gastoGrupoDTO(Gasto gasto) {
        Set<String> participantes= gasto.getParticipantes().stream().map(Usuario::getUsuario).collect(Collectors.toSet());
        HashMap<String, Double> valores = new HashMap<>();
        for (Usuario usuario : gasto.getParticipantes()) {
            valores.put(usuario.getUsuario(), gasto.getValores().get(usuario));
        }

        return GastoGrupoDTO.builder()
                .id(gasto.getId())
                .nombre(gasto.getNombre())
                .monto(gasto.getMonto())
                .id_grupo(gasto.getGrupo().getId())
                .tipo(gasto.getTipo())
                .fecha(new Date(gasto.getFecha().getTime()))
                .imagen(gasto.getImagen())
                .division(gasto.getDivision())
                .participantes(participantes)
                .valores(valores)
                .responsable(gasto.getResponsable().getUsuario())
                .id_responsable(gasto.getResponsable().getId())
                .build();
    }
}
