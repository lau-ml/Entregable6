package ttps.java.entregable6_v2.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.java.entregable6_v2.dto.GastoGrupoDTO;
import ttps.java.entregable6_v2.helpers.requests.gastos.GastoRequest;
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.servicios.GastoService;
import ttps.java.entregable6_v2.servicios.GrupoService;
import ttps.java.entregable6_v2.servicios.UsuarioService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ttps.java.entregable6_v2.helpers.actualizarGasto.Gasto.actualizarGasto;

@RestController
@RequestMapping("/api/gasto")
public class GastoController {


    @Autowired
    UsuarioService usuarioService;

    @Autowired
    GastoService gastoService;

    @Autowired
    GrupoService grupoService;


    @RequestMapping("/crear")
    public ResponseEntity<?> crearGasto(HttpSession httpSession, @RequestBody GastoRequest gastoCreateRequest) throws Exception {
        try {
            Long aux_id = (Long) httpSession.getAttribute("connectedUser");
            if (aux_id == null) {
                return ResponseEntity.badRequest().body("No hay usuario conectado");
            }
            if (!gastoCreateRequest.isValid()) {
                return ResponseEntity.badRequest().body("Datos invalidos");
            }
            Usuario user = usuarioService.recuperar(aux_id);
            Grupo grupo = null;
            if (gastoCreateRequest.getId_grupo() != null) {
                grupo = grupoService.recuperar(gastoCreateRequest.getId_grupo());
            }
            Set<Usuario> usuarios = new HashSet<>();
            Map<Usuario, Double> valores = new HashMap<>();
            for (int i = 0; i < gastoCreateRequest.getParticipantes().size(); i++) {
                Usuario aux = usuarioService.recuperar(gastoCreateRequest.getParticipantes().get(i));
                if (aux == null) {
                    return ResponseEntity.badRequest().body("No existe el usuario");
                }
                usuarios.add(aux);
                valores.put(aux, gastoCreateRequest.getValores().get(i));
            }

            Gasto gasto = new Gasto(gastoCreateRequest.getNombre(), gastoCreateRequest.getMonto(), gastoCreateRequest.getFecha(), gastoCreateRequest.getImagen(), usuarios, user, grupo, gastoCreateRequest.getTipo(), valores, gastoCreateRequest.getDivision());
            gastoService.persistir(gasto);
            GastoGrupoDTO gastoDTO = new GastoGrupoDTO(gasto,(gasto.getValores().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getUsuario(), Map.Entry::getValue, (a, b) -> b, HashMap::new))));

            return new ResponseEntity<>(gastoDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear gasto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/{id}/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> crearGasto(HttpSession httpSession, @PathVariable("id") long id, @RequestBody GastoRequest gastoUpdateRequest) throws Exception {
        try {
            Long aux_id = (Long) httpSession.getAttribute("connectedUser");
            if (aux_id == null) {
                return ResponseEntity.badRequest().body("No hay usuario conectado");
            }
            Usuario user = usuarioService.recuperar(aux_id);
            Gasto gasto = gastoService.recuperar(id);
            if (gasto == null) {
                return ResponseEntity.badRequest().body("No existe el gasto");
            }
            if (!gasto.getResponsable().getUsuario().equals(user.getUsuario())) {
                return ResponseEntity.badRequest().body("El usuario no es dueño del gasto");
            }

            if (!gastoUpdateRequest.isValid()) {
                return ResponseEntity.badRequest().body("Datos invalidos");
            }
            Grupo grupo = null;
            if (gastoUpdateRequest.getId_grupo() != null) {
                grupo = grupoService.recuperar(gastoUpdateRequest.getId_grupo());
            }
            Set<Usuario> usuarios = new HashSet<>();
            Map<Usuario, Double> valores = new HashMap<>();
            for (int i = 0; i < gastoUpdateRequest.getParticipantes().size(); i++) {
                Usuario aux = usuarioService.recuperar(gastoUpdateRequest.getParticipantes().get(i));
                if (aux == null) {
                    return ResponseEntity.badRequest().body("No existe el usuario");
                }
                usuarios.add(aux);
                valores.put(aux, gastoUpdateRequest.getValores().get(i));
            }
            actualizarGasto(gasto, gastoUpdateRequest, grupo, usuarios, valores);

            GastoGrupoDTO gastoDTO = new GastoGrupoDTO(gasto,(gasto.getValores().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getUsuario(), Map.Entry::getValue, (a, b) -> b, HashMap::new))));
            gastoService.actualizar(gasto);
            return new ResponseEntity<>(gastoDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar gasto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
