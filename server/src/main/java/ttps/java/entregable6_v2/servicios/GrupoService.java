package ttps.java.entregable6_v2.servicios;

import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ttps.java.entregable6_v2.excepciones.GrupoException;
import ttps.java.entregable6_v2.helpers.requests.grupos.GrupoCreateRequest;
import ttps.java.entregable6_v2.helpers.requests.grupos.GrupoUpdateRequest;
import ttps.java.entregable6_v2.modelos.Categoria;
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.repository.GrupoJPA;
import ttps.java.entregable6_v2.repository.UsuarioJPA;

import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service
public class GrupoService {
    @Autowired
    private GrupoJPA dao;
    @Autowired
    private UsuarioJPA usuarioDAO;


    public Grupo persistir(Grupo entity) throws Exception {
        try {
        return dao.save(entity);
        } catch (Exception e) {
            throw new Exception("Ya administra un grupo con ese nombre");
        }
    }


    public Grupo recuperar(Long id) {
        return dao.findById(id).orElse(null);
    }


    public void actualizar(Grupo entity) throws Exception {

        Grupo grupo = this.recuperar(entity.getId());
        dao.save(entity);
    }


    public Usuario usuarioPerteneciente(Grupo grupo, Usuario usuario) throws GrupoException {

        try {
            return dao.usuarioPerteneciente(grupo.getId(), usuario.getId());
        } catch (NoResultException e) {
            throw new GrupoException("El usuario no pertenece al grupo");
        }

    }

    public void actualizarGrupo(Grupo grupo, GrupoUpdateRequest grupoRequest, Usuario user) throws Exception {
        this.usuarioPerteneciente(grupo, user);
        grupo.setNombre(grupoRequest.getNombre());
        grupo.setCategoria(grupoRequest.getCategoria());
        this.actualizar(grupo);
    }


    public Grupo crearGrupo(GrupoCreateRequest grupoCreateRequest, Usuario usuario) throws GrupoException {
        try {


            Usuario usuarioConGrupos = usuarioDAO.recuperarConGrupos(usuario.getId());
            Grupo grupo_persistido = persistir(new Grupo(grupoCreateRequest.getNombreGrupo(), grupoCreateRequest.getCategoria(), .0, usuarioConGrupos));
            usuarioConGrupos.agregarGrupo(grupo_persistido);
            usuarioDAO.save(usuarioConGrupos);
            return grupo_persistido;
        } catch (Exception e) {
            throw new GrupoException(e.getMessage());
        }
    }

    public List<Gasto> recuperarTodosLosGastos(long id) {
        return dao.recuperarTodosLosGastos(id);
    }


    public Set<Grupo> recuperarTodos(Usuario usuario) {
        return usuario.getGrupos();
    }


    public Page<Grupo> recuperarGruposPaginados(Long userId, int page, int pageSize, String nombre, Categoria categoria) {
        return dao.recuperarGruposPaginados(userId, nombre, categoria, PageRequest.of(page, pageSize));

    }


}
