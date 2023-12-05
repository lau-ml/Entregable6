package ttps.java.entregable6_v2.helpers.requests.gastos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ttps.java.entregable6_v2.modelos.Division;
import ttps.java.entregable6_v2.modelos.TipoGasto;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GastoRequest {
    private String nombre;
    private double monto;
    private Long id_grupo;
    private TipoGasto tipo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fecha;
    private String imagen;
    private Division division;
    private List<Long> participantes;
    private List<Double> valores;

    public boolean isValid() {
        if (getParticipantes() == null || getParticipantes().isEmpty()) {
            return false;
        }
        System.out.println(getFecha());
        if (getValores() == null || getValores().isEmpty()) {
            return false;
        }
        if (getNombre() == null || getNombre().isEmpty()) {
            return false;
        }
        if (getFecha() == null || getFecha().toString().isEmpty()) {
            return false;
        }
        if (getTipo() == null) {
            return false;
        }
        if (getMonto() <= 0) {
            return false;
        }
        if (getDivision() == null)
            return false;
        if (getImagen() == null) {
            return false;
        }

        if (getParticipantes().size() != getValores().size()) {
            return false;
        }
        if (getDivision() == Division.MONTO) {
            return getMonto() == getValores().stream().reduce(0.0, Double::sum);
        } else {
            return getValores().stream().reduce(0.0, Double::sum) == 100;
        }


    }
}
