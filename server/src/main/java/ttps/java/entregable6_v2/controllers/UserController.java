package ttps.java.entregable6_v2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ttps.java.entregable6_v2.excepciones.UsuarioInvalidoException;
import ttps.java.entregable6_v2.mapper.Mapper;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.servicios.UsuarioService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UsuarioService userService;

    private Mapper mapper = new Mapper();

    @GetMapping(value = "/")
    public ResponseEntity<?> getUser() {
        try {
            Usuario user = userService.recuperarUsuario();
            return new ResponseEntity<>(mapper.usuarioDTO(user), HttpStatus.OK);
        } catch (UsuarioInvalidoException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
}
