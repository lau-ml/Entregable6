package ttps.java.entregable6_v2.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ttps.java.entregable6_v2.dto.GrupoDTO;
import ttps.java.entregable6_v2.excepciones.UsuarioInvalidoException;
import ttps.java.entregable6_v2.helpers.requests.grupos.GrupoCreateRequest;
import ttps.java.entregable6_v2.helpers.requests.grupos.GrupoUpdateRequest;
import ttps.java.entregable6_v2.mapper.Mapper;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.SolicitudGrupo;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.servicios.GrupoService;
import ttps.java.entregable6_v2.servicios.SolictudGrupoService;
import ttps.java.entregable6_v2.servicios.UsuarioService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grupo")
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario user = usuarioService.findByUsername(username).orElse(null);
        Grupo grupo = new Grupo(grupoCreateRequest.getNombre(), grupoCreateRequest.getCategoria(), .0);
        try {
            grupoService.crearGrupo(grupo, user);
            return new ResponseEntity<Grupo>(grupo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error al crear grupo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/todos", method = RequestMethod.GET)
    public ResponseEntity<?> getGrupos(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int pageSize) throws UsuarioInvalidoException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario user = usuarioService.findByUsername(username).orElse(null);

        try {
            assert user != null;

            Page<Grupo> gruposPaginados = grupoService.recuperarGruposPaginados(user.getId(), page - 1, pageSize);

            List<GrupoDTO> grupoDTOs = gruposPaginados.stream()
                    .map(mapper::grupoDTO)
                    .collect(Collectors.toList());
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", gruposPaginados.getTotalElements());
            response.put("totalPages", gruposPaginados.getTotalPages());
            response.put("currentPage", gruposPaginados.getNumber() + 1);
            response.put("grupos", grupoDTOs);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<String>("Error al recuperar grupos paginados", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> verGrupo(@PathVariable("id") long id) throws UsuarioInvalidoException {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario user = usuarioService.findByUsername(username).orElse(null);
        try {
            assert user != null;
            Grupo grupo = grupoService.recuperar(id);
            if (grupoService.usuarioPerteneciente(grupo, user) == null) {
                return new ResponseEntity<String>("El usuario no pertenece al grupo", HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<GrupoDTO>(mapper.grupoDTO(grupo), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error al recuperar grupo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actulizarGrupo(@RequestBody GrupoUpdateRequest grupoUpdateRequest, HttpSession httpSession, @PathVariable("id") long id) throws UsuarioInvalidoException {


        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Usuario user = usuarioService.findByUsername(username).orElse(null);
            Grupo grupo = grupoService.recuperar(id);
            assert user != null;
            if (grupoService.usuarioPerteneciente(grupo, user) == null) {
                return new ResponseEntity<String>("El usuario no pertenece al grupo", HttpStatus.UNAUTHORIZED);
            }
            grupo.setNombre(grupoUpdateRequest.getNombre());
            grupo.setCategoria(grupoUpdateRequest.getCategoria());
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
        Usuario user = usuarioService.recuperar(user_id);
        try {
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
        Usuario user = usuarioService.recuperar(user_id);
        try {
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
    public ResponseEntity<?> getGastos(HttpSession httpSession, @PathVariable("id") long id) throws UsuarioInvalidoException {
        Long user_id = (Long) httpSession.getAttribute("connectedUser");
        if (user_id == null) {
            return new ResponseEntity<String>("No hay usuario conectado", HttpStatus.UNAUTHORIZED);
        }
        Usuario user = usuarioService.recuperar(user_id);
        try {
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
