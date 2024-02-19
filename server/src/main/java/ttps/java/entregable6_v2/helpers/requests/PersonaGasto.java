package ttps.java.entregable6_v2.helpers.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonaGasto {
    private String usuario;
    private Double monto;

    // Constructors, getters, setters, and other methods go here
}
