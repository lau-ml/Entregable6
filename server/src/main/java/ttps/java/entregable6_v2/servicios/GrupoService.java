package ttps.java.entregable6_v2.servicios;

import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ttps.java.entregable6_v2.excepciones.GrupoException;
import ttps.java.entregable6_v2.helpers.requests.grupos.GrupoCreateRequest;
import ttps.java.entregable6_v2.helpers.requests.grupos.GrupoUpdateRequest;
import ttps.java.entregable6_v2.modelos.*;
import ttps.java.entregable6_v2.repository.GrupoJPA;
import ttps.java.entregable6_v2.repository.SolicitudGrupoJPA;
import ttps.java.entregable6_v2.repository.UsuarioJPA;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class GrupoService {
    @Autowired
    private GrupoJPA dao;
    @Autowired
    private UsuarioJPA usuarioDAO;

    @Autowired
    private SolicitudGrupoJPA solicitudGrupoDAO;

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

    public boolean esAdmin(Grupo grupo, Usuario usuario) throws GrupoException {

        return Objects.equals(grupo.getResponsable().getId(), usuario.getId());
    }

    public void actualizarGrupo(Grupo grupo, GrupoUpdateRequest grupoRequest, Usuario user) throws Exception {
        this.usuarioPerteneciente(grupo, user);
         isValid(grupo, grupoRequest, user);
        grupo.setNombre(grupoRequest.getNombreGrupo());
        grupo.setCategoria(grupoRequest.getCategoria());
        Set<Usuario> invitados = grupoRequest.getParticipantes().stream().map(usuarioDAO::recuperarConGrupos).collect(Collectors.toSet());
        List<String> lista = solicitudGrupoDAO.getSolicitudesEnviadasUsuarios(grupo.getId());
        Set<Usuario> participantesAntiguos = grupo.getParticipantes();
        Set<Usuario> participantes = new HashSet<Usuario>();
        grupo.getParticipantes().forEach(participante -> {
            participante.eliminarGrupo(grupo);
            usuarioDAO.save(participante);
        });

        invitados.forEach(invitado -> {
            if (!participantesAntiguos.contains(invitado) && !lista
                    .contains(invitado.getUsuario())) {
                solicitudGrupoDAO.save(new SolicitudGrupo(grupo, user, invitado));
            } else if (participantesAntiguos.contains(invitado)) {
                participantes.add(invitado);
                invitado.agregarGrupo(grupo);
                usuarioDAO.save(invitado);
            }
        });
        participantes.add(user);
        user.agregarGrupo(grupo);
        usuarioDAO.save(user);
        grupo.setParticipantes(participantes);
        this.actualizar(grupo);
    }

    private void isValid(Grupo grupo, GrupoUpdateRequest grupoRequest, Usuario user) throws GrupoException {
        if (!esAdmin(grupo, user)) {
            throw new GrupoException("No es admin del grupo");
        }
        if (grupoRequest.getNombreGrupo() == null || grupoRequest.getNombreGrupo().isEmpty()) {
            throw new GrupoException("El nombre no puede ser nulo");
        }
        if (grupoRequest.getCategoria() == null) {
            throw new GrupoException("La categoria no puede ser nula");
        }
        if(grupoRequest.getParticipantes().isEmpty()){
            throw new GrupoException("No hay participantes");
        }
    }


    public Grupo crearGrupo(GrupoCreateRequest grupoCreateRequest, Usuario usuario) throws GrupoException {
        try {

            extracted(grupoCreateRequest);

            Usuario usuarioConGrupos = usuarioDAO.recuperarConGrupos(usuario.getId());
            Set<Usuario> invitaciones = grupoCreateRequest.getParticipantes().stream().map(usuarioDAO::recuperarConGrupos).collect(Collectors.toSet());
            Set<Usuario> participantes = new HashSet<Usuario>();
            participantes.add(usuarioConGrupos);
            Grupo grupo_persistido = persistir(new Grupo(grupoCreateRequest.getNombreGrupo(), grupoCreateRequest.getCategoria(), .0, usuarioConGrupos, participantes));
            invitaciones.forEach(participante -> {
                solicitudGrupoDAO.save(new SolicitudGrupo(grupo_persistido, usuarioConGrupos, participante));
            });
            usuarioConGrupos.agregarGrupo(grupo_persistido);
            dao.save(grupo_persistido);
            usuarioDAO.save(usuarioConGrupos);

            return grupo_persistido;
        } catch (Exception e) {
            throw new GrupoException(e.getMessage());
        }
    }

    private static void extracted(GrupoCreateRequest grupoCreateRequest) throws GrupoException {
        if (grupoCreateRequest.getNombreGrupo() == null || grupoCreateRequest.getNombreGrupo().isEmpty()) {
            throw new GrupoException("El nombre no puede ser nulo");
        }
        if (grupoCreateRequest.getCategoria() == null) {
            throw new GrupoException("La categoria no puede ser nula");
        }
        if(grupoCreateRequest.getParticipantes().isEmpty()){
            throw new GrupoException("No hay participantes");
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
