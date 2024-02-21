package ttps.java.entregable6_v2.helpers.requests.gastos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ttps.java.entregable6_v2.helpers.requests.PersonaGasto;
import ttps.java.entregable6_v2.modelos.Division;
import ttps.java.entregable6_v2.modelos.TipoGasto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GastoRequest {

    private double monto;
    private Long id_grupo;
    private TipoGasto tipo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate fecha;
    private String imagen;
    private Division division;
    private List<PersonaGasto> personas;
    private String responsable;
    public void isValid() {
        if (getPersonas() == null || getPersonas().isEmpty()) {
            throw new IllegalArgumentException("No se puede crear un gasto sin participantes");
        }
        if (getPersonas().stream().map(PersonaGasto::getUsuario).collect(Collectors.toUnmodifiableSet()).size() != getPersonas().size()) {
            throw new IllegalArgumentException("No se puede crear un gasto con participantes repetidos");
        }

        if (getFecha() == null) {
            throw new IllegalArgumentException("No se puede crear un gasto sin fecha");
        }
        if (getTipo() == null) {
            throw new IllegalArgumentException("No se puede crear un gasto sin tipo");
        }
        if (getMonto() <= 0) {
            throw new IllegalArgumentException("No se puede crear un gasto con monto menor o igual a 0");
        }
        if (getDivision() == null)
            throw new IllegalArgumentException("No se puede crear un gasto sin division");
        if (getDivision() == Division.MONTO) {
            if (!(getMonto() == getPersonas().stream().map(PersonaGasto::getMonto).reduce(0.0, Double::sum)))
                throw new IllegalArgumentException("La suma de los montos de los participantes debe ser igual al monto del gasto");
        } else {
            if (!(getPersonas().stream().map(PersonaGasto::getMonto).reduce(0.0, Double::sum) == 100))
                throw new IllegalArgumentException("La suma de los porcentajes de los participantes debe ser igual a 100");
        }
    }
}
