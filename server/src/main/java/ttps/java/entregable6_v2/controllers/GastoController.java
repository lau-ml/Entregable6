package ttps.java.entregable6_v2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ttps.java.entregable6_v2.dto.GastoGrupoDTO;
import ttps.java.entregable6_v2.helpers.ImagenUtils.ImageUtils;
import ttps.java.entregable6_v2.helpers.Pagination.PaginationUtils;
import ttps.java.entregable6_v2.helpers.requests.gastos.GastoRequest;
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.modelos.TipoGasto;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.servicios.GastoService;
import ttps.java.entregable6_v2.servicios.GrupoService;
import ttps.java.entregable6_v2.servicios.UsuarioService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gastos")
public class GastoController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    GastoService gastoService;

    @Autowired
    GrupoService grupoService;


    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    public ResponseEntity<?> crearGasto(@RequestPart("gastoRequest") GastoRequest gastoCreateRequest,
                                        @RequestPart("imagen") MultipartFile imagen) {

        try {
            gastoCreateRequest.isValid();
            Gasto gasto = gastoService.crearGasto(gastoCreateRequest, ImageUtils.guardarImagen(imagen));
            GastoGrupoDTO gastoDTO = new GastoGrupoDTO(gasto, (gasto.getValores().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getUsuario(), Map.Entry::getValue, (a, b) -> b, HashMap::new))));
            return new ResponseEntity<>(gastoDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/{id}/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> crearGasto(@RequestPart("gastoRequest") GastoRequest gastoUpdateRequest,
                                        @RequestPart("imagen") MultipartFile imagen, @PathVariable("id") long id) throws Exception {
        try {
            Usuario user = usuarioService.recuperarUsuario();
            gastoUpdateRequest.isValid();
            Gasto gasto = gastoService.actualizarGasto(id, user, gastoUpdateRequest, imagen);
            GastoGrupoDTO gastoDTO = new GastoGrupoDTO(gasto, (gasto.getValores().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getUsuario(), Map.Entry::getValue, (a, b) -> b, HashMap::new))));
            return new ResponseEntity<>(gastoDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getGasto(@PathVariable("id") long id) {
        try {
            Usuario user = usuarioService.recuperarUsuario();
            Gasto gasto = gastoService.recuperarGastoParticular(user.getId(), id);
            GastoGrupoDTO gastoDTO = new GastoGrupoDTO(gasto, (gasto.getValores().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getUsuario(), Map.Entry::getValue, (a, b) -> b, HashMap::new))));
            return new ResponseEntity<>(gastoDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getGastos(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int pageSize,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate fechaDesde,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate fechaHasta,
                                       @RequestParam(defaultValue = "") String nombreGrupo,
                                       @RequestParam(defaultValue = "") TipoGasto tipoGasto


    ) {
        try {
            Usuario user = usuarioService.recuperarUsuario();
            Page<Gasto> gastosPaginados = gastoService.recuperarGastosPaginados(user.getId(), page - 1, pageSize, fechaDesde, fechaHasta, nombreGrupo, tipoGasto);
            PaginationUtils<Gasto> paginationUtils = new PaginationUtils<>();
            Map<String, Object> response = paginationUtils.createPaginationResponse(gastosPaginados);
            List<GastoGrupoDTO> gastoDTOs = gastosPaginados.stream()
                    .map(gasto -> new GastoGrupoDTO(gasto, (gasto.getValores().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getUsuario(), Map.Entry::getValue, (a, b) -> b, HashMap::new)))))
                    .collect(Collectors.toList());
            response.put("gastos", gastoDTOs);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
