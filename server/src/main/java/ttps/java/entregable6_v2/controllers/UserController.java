package ttps.java.entregable6_v2.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.java.entregable6_v2.excepciones.UsuarioInvalidoException;
import ttps.java.entregable6_v2.helpers.requests.usuarios.LoginRequest;
import ttps.java.entregable6_v2.helpers.requests.usuarios.RegisterRequest;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.servicios.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UsuarioService userService;

    //Recupero todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listAllUsers() {
        List<Usuario> users = null;
        try {
            users = userService.recuperarTodos("id");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (users.isEmpty()) {
            return new ResponseEntity<List<Usuario>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Usuario>>(users, HttpStatus.OK);
    }

    //Recupero un usuario dado
    @GetMapping("/{id}")
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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpSession httpSession) {

        Long id = (Long) httpSession.getAttribute("connectedUser");
        if (id != null) {
            return new ResponseEntity<String>("Ya hay una sesión iniciada", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            Usuario user = userService.login(request.getEmail(), request.getPassword());
            httpSession.setAttribute("connectedUser", user.getId());
            return new ResponseEntity<String>("Sesión iniciada", HttpStatus.OK);
        } catch (UsuarioInvalidoException e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<String>("Parámetros de sesión inválidos", HttpStatus.UNAUTHORIZED);
        }
    }


    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<String> logout(HttpSession httpSession) {
        Long id = (Long) httpSession.getAttribute("connectedUser");
        if (id != null) {
            httpSession.removeAttribute("connectedUser");
            return new ResponseEntity<String>("Sesión cerrada", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("No hay sesión inciada", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody RegisterRequest request, HttpSession httpSession) {
        if (httpSession.getAttribute("connectedUser") != null) {
            return new ResponseEntity<String>("Ya hay una sesión iniciada", HttpStatus.UNAUTHORIZED);
        }
        if (!request.isValid()) {
            return new ResponseEntity<String>("Datos inválidos", HttpStatus.UNAUTHORIZED);
        }
        try {
            Usuario entity = new Usuario(request.getNombre(), request.getApellido(), request.getUsername(), request.getEmail(), request.getPassword());
            userService.persistir(entity);
            return new ResponseEntity<String>("Usuario registrado con éxito", HttpStatus.OK);
        } catch (UsuarioInvalidoException e) {
            return new ResponseEntity<String>("Usuario ya registrado", HttpStatus.UNAUTHORIZED);
        }
    }

}
