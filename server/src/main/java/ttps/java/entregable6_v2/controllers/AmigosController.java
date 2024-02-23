package ttps.java.entregable6_v2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ttps.java.entregable6_v2.dto.GastoGrupoDTO;
import ttps.java.entregable6_v2.dto.UsuarioDTO;
import ttps.java.entregable6_v2.excepciones.UsuarioInvalidoException;
import ttps.java.entregable6_v2.helpers.Pagination.PaginationUtils;
import ttps.java.entregable6_v2.mapper.Mapper;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.servicios.UsuarioService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/amigos")
public class AmigosController {

    @Autowired
    private UsuarioService userService;

    private Mapper mapper = new Mapper();

    @GetMapping("")
    public ResponseEntity<?> getAmigos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "") String nombreAmigo

    ) throws UsuarioInvalidoException {
        try {
            Page<Usuario> usuarioPage = userService.recuperarAmigos(userService.recuperarUsuario(), page - 1, pageSize, nombreAmigo);
            PaginationUtils<Usuario> paginationUtils = new PaginationUtils<>();
            Map<String, Object> response = paginationUtils.createPaginationResponse(usuarioPage);
            List<UsuarioDTO> usuarioDTOs = usuarioPage.getContent().stream().map(mapper::usuarioDTO).toList();
            response.put("amigos", usuarioDTOs);


            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UsuarioInvalidoException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
    }


}
