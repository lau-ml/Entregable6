package ttps.java.entregable6_v2.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.java.entregable6_v2.excepciones.UsuarioInvalidoException;
import ttps.java.entregable6_v2.helpers.requests.usuarios.LoginRequest;
import ttps.java.entregable6_v2.helpers.requests.usuarios.RegisterRequest;
import ttps.java.entregable6_v2.response.AuthResponse;
import ttps.java.entregable6_v2.servicios.UsuarioService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService userService;


    //Recupero un usuario dado

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpSession httpSession) {


        return new ResponseEntity<AuthResponse>(userService.login(request), HttpStatus.OK);
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
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request, HttpSession httpSession) {

        try {
            return new ResponseEntity<AuthResponse>(userService.register(request), HttpStatus.OK);
        } catch (UsuarioInvalidoException e) {
            return new ResponseEntity<AuthResponse>(AuthResponse.builder().token("Ya existe un usuario registrado").build(), HttpStatus.UNAUTHORIZED);
        }
    }

}
