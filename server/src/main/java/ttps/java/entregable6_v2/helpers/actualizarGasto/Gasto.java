package ttps.java.entregable6_v2.helpers.actualizarGasto;

import ttps.java.entregable6_v2.helpers.requests.gastos.GastoRequest;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.Usuario;

import java.util.Map;
import java.util.Set;

public class Gasto {

    public static void actualizarGasto(ttps.java.entregable6_v2.modelos.Gasto gasto, GastoRequest gastoUpdateRequest, Grupo grupo, Set<Usuario> usuarios, Map<Usuario, Double> valores) {

        gasto.setMonto(gastoUpdateRequest.getMonto());
        gasto.setFecha(gastoUpdateRequest.getFecha());
        gasto.setImagen(gastoUpdateRequest.getImagen());
        gasto.setParticipantes(usuarios);
        gasto.setResponsable(gasto.getResponsable());
        gasto.setGrupo(grupo);
        gasto.setTipo(gastoUpdateRequest.getTipo());
        gasto.setValores(valores);
        gasto.setDivision(gastoUpdateRequest.getDivision());
    }
}
