package ttps.java.entregable6_v2.mapper;

import org.springframework.stereotype.Component;
import ttps.java.entregable6_v2.dto.GastoGrupoDTO;
import ttps.java.entregable6_v2.dto.GrupoDTO;
import ttps.java.entregable6_v2.dto.UsuarioDTO;
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.Usuario;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Mapper {
    public GastoGrupoDTO gastoGrupoDTO(Gasto gasto) {

        return GastoGrupoDTO.builder()
                .id(gasto.getId())
                .nombreGrupo(gasto.getGrupo().getNombre())
                .monto(gasto.getMonto())
                .id_grupo(gasto.getGrupo().getId())
                .tipo(gasto.getTipo())
                .fecha(new Date(gasto.getFecha().getTime()))
                .imagen(gasto.getImagen())
                .division(gasto.getDivision())
                .valores(new HashMap<>(gasto.getValores().keySet().stream()
                        .collect(Collectors.toMap(
                                Usuario::getUsuario,
                                gasto.getValores()::get
                        )))
                )
                .responsable(gasto.getResponsable().getUsuario())
                .id_responsable(gasto.getResponsable().getId())
                .build();
    }

    public GrupoDTO grupoDTO(Grupo grupo) {
        return GrupoDTO.builder()
                .id(grupo.getId())
                .nombre(grupo.getNombre())
                .categoria(grupo.getCategoria().toString())
                .saldo(grupo.getSaldo())
                .participantes(grupo.getParticipantes().stream().map(Usuario::getUsuario).collect(Collectors.toSet()))
                .build();
    }

    public UsuarioDTO usuarioDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .saldo(usuario.getSaldo())
                .amigos(usuario.getAmigos().stream().map(Usuario::getUsuario).collect(Collectors.toSet()))
                .build();
    }

}
