package ttps.java.entregable6_v2.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ttps.java.entregable6_v2.excepciones.UsuarioInvalidoException;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.repository.UsuarioJPA;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class UsuarioService {

    @Autowired
    private UsuarioJPA dao;


    public Usuario persistir(Usuario register) throws UsuarioInvalidoException {
        System.out.println(register.getUsuario());
        System.out.println(register.getEmail());
        if (dao.findByUsuario(register.getUsuario()) != null || dao.findByEmail(register.getEmail()) != null) {
            throw new UsuarioInvalidoException("El usuario o mail ya existe");
        }
        return dao.save(register);
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


    public Usuario login(String login, String password) throws UsuarioInvalidoException {
        Usuario user = null;
        user = dao.findByUsuario(login);
        if (user != null && user.getContrasena().equals(password)) {
            return user;
        } else {
            throw new UsuarioInvalidoException("Usuario o contraseña incorrectos");
        }
    }

}
