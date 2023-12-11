package ttps.java.entregable6_v2.controllers;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.java.entregable6_v2.excepciones.UsuarioInvalidoException;
import ttps.java.entregable6_v2.helpers.requests.usuarios.CambiarPassRequest;
import ttps.java.entregable6_v2.helpers.requests.usuarios.EmailRequest;
import ttps.java.entregable6_v2.helpers.requests.usuarios.LoginRequest;
import ttps.java.entregable6_v2.helpers.requests.usuarios.RegisterRequest;
import ttps.java.entregable6_v2.response.AuthResponse;
import ttps.java.entregable6_v2.servicios.UsuarioService;
import org.springframework.core.env.Environment;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private Environment environment;
    @Autowired
    private UsuarioService userService;


    //Recupero un usuario dado

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
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
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {

        try {
            return new ResponseEntity<AuthResponse>(userService.register(request, environment.getProperty("url") + "/auth"), HttpStatus.OK);
        } catch (UsuarioInvalidoException e) {
            return new ResponseEntity<AuthResponse>(AuthResponse.builder().message("Ya existe un usuario registrado").build(), HttpStatus.UNAUTHORIZED);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value="/resend", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> resendVerification(@Valid @RequestBody EmailRequest emailRequest) {
        if (userService.resendVerification(emailRequest.getEmail(), environment.getProperty("url") + "/auth")) {
            return new ResponseEntity<AuthResponse>(AuthResponse.builder().message("Codigo de verificación reenviado").build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<AuthResponse>(AuthResponse.builder().message("No se pudo reenviar el codigo de verificación").build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping("/verify")
    public ResponseEntity<AuthResponse> verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return new ResponseEntity<AuthResponse>(AuthResponse.builder().message("Usuario verificado").build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<AuthResponse>(AuthResponse.builder().message("No se pudo verificar el usuario").build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/recover", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> recoverUser(@Valid @RequestBody EmailRequest emailRequest) {
        if (userService.recover(emailRequest, environment.getProperty("url") + "/auth")) {
            return new ResponseEntity<AuthResponse>(AuthResponse.builder().message("Codigo de recuperación enviado").build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<AuthResponse>(AuthResponse.builder().message("No se pudo recuperar el usuario").build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> resetUser(@Param("code") String code, @Valid @RequestBody CambiarPassRequest request) {
        if (userService.reset(code, request)) {
            return new ResponseEntity<AuthResponse>(AuthResponse.builder().message("Contraseña cambiada").build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<AuthResponse>(AuthResponse.builder().message("No se pudo cambiar la contraseña").build(), HttpStatus.UNAUTHORIZED);
        }
    }


}
