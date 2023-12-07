package ttps.java.entregable6_v2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ttps.java.entregable6_v2.excepciones.UsuarioInvalidoException;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.servicios.UsuarioService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UsuarioService userService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Usuario> getUser(@PathVariable("id") long id) {
        System.out.println("Obteniendo usuario con id " + id);
        Usuario user = null;
        try {
            user = userService.recuperar(id);
        } catch (UsuarioInvalidoException e) {
            System.out.println("Usuario con id " + id + " no encontrado");
            return new ResponseEntity<Usuario>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Usuario>(user, HttpStatus.OK);
    }

}
