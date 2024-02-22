package ttps.java.entregable6_v2.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @RequestMapping(value = "/{id}/gasto/{gasto}", method = RequestMethod.POST)
    public ResponseEntity<?> saldarGasto(@PathVariable("id") long id, @PathVariable("gasto") long gastoId) {
        return null;
    }

    @RequestMapping(value = "/{id}/deuda/{gasto}", method = RequestMethod.POST)
    public ResponseEntity<?> saldarDeuda(@PathVariable("id") long id, @PathVariable("gasto") long gastoId) {
        return null;
    }



}
