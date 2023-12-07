package ttps.java.entregable6_v2.servicios;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    private final  PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) throws UsuarioInvalidoException {

        if (dao.findByUsuario(request.getUsername()).orElse(null) != null || dao.findByEmail(request.getEmail()) != null) {
            throw new UsuarioInvalidoException("El usuario o mail ya existe");
        }
        Usuario entity = Usuario
                .builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .usuario(request.getUsername())
                .email(request.getEmail())
                .contrasena(passwordEncoder.encode(request.getPassword()))
                .build();
        dao.save(entity);
        return AuthResponse.builder().token(jwtService.getToken(entity)).build();

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


    public Optional<Usuario> findByUsername(String username) {
        return dao.findByUsuario(username);
    }
}
