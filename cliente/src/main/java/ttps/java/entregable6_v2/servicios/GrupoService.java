package ttps.java.entregable6_v2.servicios;

import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.repository.GrupoJPA;
import ttps.java.entregable6_v2.repository.UsuarioJPA;

import java.util.List;

@org.springframework.stereotype.Service
public class GrupoService {
    @Autowired
    private GrupoJPA dao;
    @Autowired
    private UsuarioJPA usuarioDAO;


    public Grupo persistir(Grupo entity) throws Exception {
        return dao.save(entity);
    }


    public Grupo recuperar(Long id) throws Exception {
        return dao.findById(id).orElse(null);
    }


    public boolean actualizar(Grupo entity) throws Exception {

        Grupo grupo = dao.findById(entity.getId()).orElse(null);
        if (grupo != null) {
            dao.save(entity);
            return true;
        } else {
            return false;
        }
    }


    public Usuario usuarioPerteneciente(Grupo grupo, Usuario usuario) {

        try {
            return dao.usuarioPerteneciente(grupo.getId(), usuario.getId());
        } catch (NoResultException e) {
            return null;
        }

    }


    public void crearGrupo(Grupo grupo, Usuario usuario) throws Exception {
        try {
            Grupo grupo_persistido = persistir(grupo);
            Usuario usuarioConGrupos = usuarioDAO.recuperarConGrupos(usuario.getId());
            usuarioConGrupos.agregarGrupo(grupo_persistido);
            usuarioDAO.save(usuarioConGrupos);
        } catch (Exception e) {
            throw new Exception("Error al crear grupo");
        }
    }

    public List<Gasto> recuperarTodosLosGastos(long id) throws Exception {
        return dao.recuperarTodosLosGastos(id);
    }

}
