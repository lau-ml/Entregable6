package ttps.java.entregable6_v2.helpers.requests.gastos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ttps.java.entregable6_v2.helpers.requests.PersonaGasto;
import ttps.java.entregable6_v2.modelos.Division;
import ttps.java.entregable6_v2.modelos.TipoGasto;

import java.sql.Date;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fecha;
    private String imagen;
    private Division division;
    private List<PersonaGasto> personas;

    public boolean isValid() {
        if (getPersonas() == null || getPersonas().isEmpty()) {
            return false;
        }
        if(getPersonas().stream().map(PersonaGasto::getNombre).collect(Collectors.toUnmodifiableSet()).size() != getPersonas().size()){
            return false;
        }

        if (getFecha() == null ) {
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


        if (getDivision() == Division.MONTO) {
            return getMonto() == getPersonas().stream().map(PersonaGasto::getMonto).reduce(0.0, Double::sum);
        } else {
            return getPersonas().stream().map(PersonaGasto::getMonto).reduce(0.0, Double::sum) == 100;
        }


    }
}
