package ttps.java.entregable6_v2.servicios;
import java.io.UnsupportedEncodingException;

import net.bytebuddy.utility.RandomString;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ttps.java.entregable6_v2.excepciones.UsuarioInvalidoException;
import ttps.java.entregable6_v2.helpers.requests.usuarios.LoginRequest;
import ttps.java.entregable6_v2.helpers.requests.usuarios.RegisterRequest;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.repository.UsuarioJPA;
import ttps.java.entregable6_v2.response.AuthResponse;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class UsuarioService {


    private final UsuarioJPA dao;

    private final JwtService jwtService;

    @Autowired
    private JavaMailSender mailSender;

    private final  PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request, String siteUrl) throws UsuarioInvalidoException, MessagingException, UnsupportedEncodingException {

        if (dao.findByUsuario(request.getUsername()).orElse(null) != null || dao.findByEmail(request.getEmail()) != null) {
            throw new UsuarioInvalidoException("El usuario o mail ya existe");
        }
        String randomCode = RandomString.make(64);
        Usuario entity = Usuario
                .builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .usuario(request.getUsername())
                .email(request.getEmail())
                .contrasena(passwordEncoder.encode(request.getPassword()))
                .activo(false)
                .verificationCode(randomCode)
                .build();
        sendVerificationEmail(entity, siteUrl);
        dao.save(entity);
        return AuthResponse.builder().msg("Usuario creado con exito").build();

    }


    private void sendVerificationEmail(Usuario user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "lautaromoller345@gmail.com";
        String senderName = "CuentasClaras";
        String subject = "Confirmar cuenta";
        String content = "Estimado [[name]],<br>"
                + "Haz click en el sigueiente enlace para verificar tu cuenta:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Verificar</a></h3>"
                + "Muchas gracias,<br>"
                + "Atentamente el equipo de CuentasClaras.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getApellido());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }


    public Usuario recuperar(Serializable id) throws UsuarioInvalidoException {
        return dao.findById((Long) id).orElse(null);

    }


    public List<Usuario> recuperarTodos(String columnOrder) throws Exception {
        return dao.findAll().stream().sorted((a, b) -> switch (columnOrder) {
            case "apellido" -> a.getApellido().compareTo(b.getApellido());
            case "usuario" -> a.getUsuario().compareTo(b.getUsuario());
            case "email" -> a.getEmail().compareTo(b.getEmail());
            default -> a.getNombre().compareTo(b.getNombre());
        }).collect(Collectors.toList());
    }


    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user=dao.findByUsuario(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();

    }

    public boolean verify(String verificationCode) {
        Usuario user = dao.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setActivo(true);
            dao.save(user);

            return true;
        }

    }
    public Optional<Usuario> findByUsername(String username) {
        return dao.findByUsuario(username);
    }
}
