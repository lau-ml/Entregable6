package ttps.java.entregable6_v2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ttps.java.entregable6_v2.dto.GastoGrupoDTO;
import ttps.java.entregable6_v2.helpers.Pagination.PaginationUtils;
import ttps.java.entregable6_v2.helpers.requests.gastos.GastoRequest;
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.servicios.GastoService;
import ttps.java.entregable6_v2.servicios.GrupoService;
import ttps.java.entregable6_v2.servicios.UsuarioService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static ttps.java.entregable6_v2.helpers.actualizarGasto.Gasto.actualizarGasto;

@RestController
@RequestMapping("/gasto")
public class GastoController {
    private static final String RUTA_ALMACENAMIENTO_IMAGENES = "C:/Users/lauta/OneDrive/Escritorio/Entregable6/cliente/src/assets";

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    GastoService gastoService;

    @Autowired
    GrupoService grupoService;


    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    public ResponseEntity<?> crearGasto(@RequestPart("gastoRequest") GastoRequest gastoCreateRequest,
                                        @RequestPart("imagen") MultipartFile imagen) throws Exception {
        Usuario user = usuarioService.recuperarUsuario();
        try {
            if (!gastoCreateRequest.isValid()) {
                return ResponseEntity.badRequest().body("Datos invalidos");
            }
            Grupo grupo = null;
            if (gastoCreateRequest.getId_grupo() != 0) {
                grupo = grupoService.recuperar(gastoCreateRequest.getId_grupo());
            }
            Set<Usuario> usuarios = new HashSet<>();
            Map<Usuario, Double> valores = new HashMap<>();
            for (int i = 0; i < gastoCreateRequest.getPersonas().size(); i++) {
                Usuario aux = usuarioService.recuperar(gastoCreateRequest.getPersonas().get(i).getNombre());
                if (aux == null) {
                    return ResponseEntity.badRequest().body("No existe el usuario");
                }
                usuarios.add(aux);
                valores.put(aux, gastoCreateRequest.getPersonas().get(i).getMonto());
            }
            assert user != null;
            user.setSaldo(user.getSaldo() - gastoCreateRequest.getMonto());
            usuarioService.actualizar(user);
            String nombreArchivo = imagen.getOriginalFilename();

            Path rutaImagen = Paths.get(RUTA_ALMACENAMIENTO_IMAGENES, nombreArchivo);
            Files.write(rutaImagen, imagen.getBytes());
            gastoCreateRequest.setImagen(nombreArchivo);
            Gasto gasto = new Gasto(gastoCreateRequest.getMonto(), gastoCreateRequest.getFecha(), gastoCreateRequest.getImagen(), usuarios, user, grupo, gastoCreateRequest.getTipo(), valores, gastoCreateRequest.getDivision());
            gastoService.persistir(gasto);
            GastoGrupoDTO gastoDTO = new GastoGrupoDTO(gasto, (gasto.getValores().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getUsuario(), Map.Entry::getValue, (a, b) -> b, HashMap::new))));

            return new ResponseEntity<>(gastoDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear gasto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/{id}/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> crearGasto(@RequestPart("gastoRequest") GastoRequest gastoUpdateRequest,
                                        @RequestPart("imagen") MultipartFile imagen, @PathVariable("id") long id) throws Exception {
        Usuario user = usuarioService.recuperarUsuario();
        try {
            Gasto gasto = gastoService.recuperar(id);
            if (gasto == null) {
                return ResponseEntity.badRequest().body("No existe el gasto");
            }
            assert user != null;
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
            for (int i = 0; i < gastoUpdateRequest.getPersonas().get(i).getNombre(); i++) {
                Usuario aux = usuarioService.recuperar(gastoUpdateRequest.getPersonas().get(i).getNombre());
                if (aux == null) {
                    return ResponseEntity.badRequest().body("No existe el usuario");
                }
                usuarios.add(aux);
                valores.put(aux, gastoUpdateRequest.getPersonas().get(i).getMonto());
            }

            String nombreArchivo = imagen.getOriginalFilename();

            Path rutaImagen = Paths.get(RUTA_ALMACENAMIENTO_IMAGENES, nombreArchivo);
            Files.write(rutaImagen, imagen.getBytes());
            gastoUpdateRequest.setImagen(nombreArchivo);
            actualizarGasto(gasto, gastoUpdateRequest, grupo, usuarios, valores);
            GastoGrupoDTO gastoDTO = new GastoGrupoDTO(gasto, (gasto.getValores().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getUsuario(), Map.Entry::getValue, (a, b) -> b, HashMap::new))));
            gastoService.actualizar(gasto);
            return new ResponseEntity<>(gastoDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar gasto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/todos", method = RequestMethod.GET)
    public ResponseEntity<?> getGastos(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int pageSize) throws Exception {
        Usuario user = usuarioService.recuperarUsuario();
        try {
            assert user != null;
            Page<Gasto> gastosPaginados = gastoService.recuperarGastosPaginados(user.getId(), page - 1, pageSize);
            PaginationUtils<Gasto> paginationUtils = new PaginationUtils<>();
            Map<String, Object> response = paginationUtils.createPaginationResponse(gastosPaginados);
            List<GastoGrupoDTO> gastoDTOs = gastosPaginados.stream()
                    .map(gasto -> new GastoGrupoDTO(gasto, (gasto.getValores().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getUsuario(), Map.Entry::getValue, (a, b) -> b, HashMap::new)))))
                    .collect(Collectors.toList());
            response.put("gastos", gastoDTOs);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<String>("Error al recuperar gastos paginados", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
