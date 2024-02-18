package ttps.java.entregable6_v2.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.java.entregable6_v2.dto.GrupoDTO;
import ttps.java.entregable6_v2.excepciones.UsuarioInvalidoException;
import ttps.java.entregable6_v2.helpers.Pagination.PaginationUtils;
import ttps.java.entregable6_v2.helpers.requests.grupos.GrupoCreateRequest;
import ttps.java.entregable6_v2.helpers.requests.grupos.GrupoUpdateRequest;
import ttps.java.entregable6_v2.mapper.Mapper;
import ttps.java.entregable6_v2.modelos.Categoria;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.SolicitudGrupo;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.servicios.GrupoService;
import ttps.java.entregable6_v2.servicios.SolictudGrupoService;
import ttps.java.entregable6_v2.servicios.UsuarioService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grupos")
public class GrupoController {
    @Autowired
    GrupoService grupoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    SolictudGrupoService solictudGrupoService;
    private Mapper mapper = new Mapper();

    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    public ResponseEntity<?> crearGrupo(@RequestBody GrupoCreateRequest grupoCreateRequest) throws UsuarioInvalidoException {
        try {
            return new ResponseEntity<Grupo>(grupoService.crearGrupo(grupoCreateRequest, usuarioService.recuperarUsuario()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error al crear grupo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getGrupos(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "" + Integer.MAX_VALUE) int pageSize,
                                       @RequestParam(defaultValue = "") String nombre,
                                       @RequestParam(defaultValue = "") Categoria categoria) throws UsuarioInvalidoException {

        try {
            Usuario user = usuarioService.recuperarUsuario();
            Page<Grupo> gruposPaginados = grupoService.recuperarGruposPaginados(user.getId(), page - 1, pageSize, nombre, categoria);

            List<GrupoDTO> grupoDTOs = gruposPaginados.stream()
                    .map(mapper::grupoDTO)
                    .collect(Collectors.toList());
            PaginationUtils<Grupo> paginationUtils = new PaginationUtils<>();
            Map<String, Object> response = paginationUtils.createPaginationResponse(gruposPaginados);
            response.put("grupos", grupoDTOs);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<String>("Error al recuperar grupos paginados", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> verGrupo(@PathVariable("id") long id) {

        try {
            Usuario user = usuarioService.recuperarUsuario();
            Grupo grupo = grupoService.recuperar(id);
            grupoService.usuarioPerteneciente(grupo, user);
            return new ResponseEntity<GrupoDTO>(mapper.grupoDTO(grupo), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error al recuperar grupo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actulizarGrupo(@RequestBody GrupoUpdateRequest grupoUpdateRequest, HttpSession httpSession, @PathVariable("id") long id) {

        try {
            Grupo grupo = grupoService.recuperar(id);
            Usuario user = usuarioService.recuperarUsuario();
            grupoService.actualizarGrupo(grupo, grupoUpdateRequest, user);
            grupoService.actualizar(grupo);
            return new ResponseEntity<GrupoDTO>(mapper.grupoDTO(grupo), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error al actualizar grupo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}/enviarSolicitud/{id_usuario}", method = RequestMethod.POST)
    public ResponseEntity<?> agregarUsuario(HttpSession httpSession, @PathVariable("id") long id, @PathVariable("id_usuario") long id_usuario) throws UsuarioInvalidoException {
        Long user_id = (Long) httpSession.getAttribute("connectedUser");
        if (user_id == null) {
            return new ResponseEntity<String>("No hay usuario conectado", HttpStatus.UNAUTHORIZED);
        }

        try {
            Usuario user = usuarioService.recuperar(user_id);
            Usuario usuario = usuarioService.recuperar(id_usuario);
            if (usuario == null) {
                return new ResponseEntity<String>("El usuario no existe", HttpStatus.BAD_REQUEST);
            }
            Grupo grupo = grupoService.recuperar(id);
            if (grupo == null) {
                return new ResponseEntity<String>("El grupo no existe", HttpStatus.BAD_REQUEST);
            }
            SolicitudGrupo solicitud = new SolicitudGrupo(grupo, user, usuario);
            return solictudGrupoService.enviarSolicitud(solicitud);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error al actualizar grupo", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/{id}/aceptarSolicitud/{id_usuario}", method = RequestMethod.POST)
    public ResponseEntity<?> aceptarSolicitud(HttpSession httpSession, @PathVariable("id") long id, @PathVariable("id_usuario") long id_usuario) throws UsuarioInvalidoException {
        Long user_id = (Long) httpSession.getAttribute("connectedUser");
        if (user_id == null) {
            return new ResponseEntity<String>("No hay usuario conectado", HttpStatus.UNAUTHORIZED);
        }

        try {
            Usuario user = usuarioService.recuperar(user_id);
            Usuario usuario = usuarioService.recuperar(id_usuario);
            if (usuario == null) {
                return new ResponseEntity<String>("El usuario no existe", HttpStatus.BAD_REQUEST);
            }
            Grupo grupo = grupoService.recuperar(id);
            if (grupo == null) {
                return new ResponseEntity<String>("El grupo no existe", HttpStatus.BAD_REQUEST);
            }
            SolicitudGrupo solicitud = new SolicitudGrupo(grupo, user, usuario);
            return solictudGrupoService.aceptarSolicitud(solicitud);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error al actualizar grupo", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/{id}/gastos", method = RequestMethod.GET)
    public ResponseEntity<?> getGastos(@PathVariable("id") long id) throws UsuarioInvalidoException {


        try {
            Usuario user = usuarioService.recuperarUsuario();
            Grupo grupo = grupoService.recuperar(id);
            if (grupoService.usuarioPerteneciente(grupo, user) == null) {
                return new ResponseEntity<String>("El usuario no pertenece al grupo", HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(grupoService.recuperarTodosLosGastos(id).stream().map(mapper::gastoGrupoDTO).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error al actualizar grupo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
