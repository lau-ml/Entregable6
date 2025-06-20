package ttps.java.entregable6_v2.helpers.actualizarGasto;

import ttps.java.entregable6_v2.helpers.requests.gastos.GastoRequest;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.Usuario;

import java.util.HashSet;
import java.util.Map;

public class Gasto {

    public static void actualizarGasto(ttps.java.entregable6_v2.modelos.Gasto gasto, Usuario responsable, GastoRequest gastoUpdateRequest, Grupo grupo, Map<Usuario, Double> valores, String nombreArchivo) {

        gasto.setMonto(gastoUpdateRequest.getMonto());
        gasto.setFecha(gastoUpdateRequest.getFecha());
        gasto.setImagen(nombreArchivo != null ? nombreArchivo : gasto.getImagen());
        gasto.setResponsable(responsable);
        gasto.setGrupo(grupo);
        gasto.setTipo(gastoUpdateRequest.getTipo());
        gasto.setValores(valores);
        gasto.setDivision(gastoUpdateRequest.getDivision());
    }

}
