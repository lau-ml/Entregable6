package ttps.java.entregable6_v2.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/amigos")
public class AmigosController {

    public ResponseEntity<?> getAmigos(){
        return new ResponseEntity<>("", HttpStatus.OK);
    }


}
