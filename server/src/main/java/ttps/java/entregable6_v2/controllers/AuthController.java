package ttps.java.entregable6_v2.controllers;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ttps.java.entregable6_v2.excepciones.UsuarioInvalidoException;
import ttps.java.entregable6_v2.helpers.requests.usuarios.*;
import ttps.java.entregable6_v2.response.AuthResponse;
import ttps.java.entregable6_v2.response.MessageResponse;
import ttps.java.entregable6_v2.servicios.UsuarioService;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private Environment environment;
    @Autowired
    private UsuarioService userService;



    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){

        try {
            return new ResponseEntity<AuthResponse>(userService.login(request), HttpStatus.OK);
        } catch (UsuarioInvalidoException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) throws MessagingException, UsuarioInvalidoException, UnsupportedEncodingException {
        return new ResponseEntity<>(userService.register(request, environment.getProperty("url")), HttpStatus.OK);
    }

    @RequestMapping(value = "/resend", method = RequestMethod.POST)
    public ResponseEntity<?> resendVerification(@Valid @RequestBody EmailRequest emailRequest) {
        if (userService.resendVerification(emailRequest.getEmail(), environment.getProperty("url"))) {
            return new ResponseEntity<>(MessageResponse.builder().message("Código de verificación enviado").build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.builder().message("No se pudo enviar el mensaje de verificación").build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/verify", method = RequestMethod.PATCH)
    public ResponseEntity<?> verifyUser(@RequestBody VerificacionRequest verificacionRequest) {

        if (userService.verify(verificacionRequest)) {
            return new ResponseEntity<>(MessageResponse.builder().message("Usuario verificado").build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.builder().message("No se pudo verificar el usuario").build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/recover", method = RequestMethod.POST)
    public ResponseEntity<?> recoverUser(@Valid @RequestBody EmailRequest emailRequest) {
        if (userService.recover(emailRequest, environment.getProperty("url"))) {
            return new ResponseEntity<>(MessageResponse.builder().message("Código de recuperación enviado").build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.builder().message("No se pudo recuperar el usuario").build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/reset", method = RequestMethod.PATCH)
    public ResponseEntity<MessageResponse> resetUser( @Valid @RequestBody CambiarPassRequest request) {
        if (userService.reset(request)) {
            return new ResponseEntity<>(MessageResponse.builder().message("Contraseña cambiada").build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.builder().message("No se pudo cambiar la contraseña").build(), HttpStatus.UNAUTHORIZED);
        }
    }


}
