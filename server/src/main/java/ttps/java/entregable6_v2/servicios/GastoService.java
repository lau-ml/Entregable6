package ttps.java.entregable6_v2.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import ttps.java.entregable6_v2.excepciones.GastoException;
import ttps.java.entregable6_v2.excepciones.GrupoException;
import ttps.java.entregable6_v2.excepciones.UsuarioInvalidoException;
import ttps.java.entregable6_v2.helpers.ImagenUtils.ImageUtils;
import ttps.java.entregable6_v2.helpers.requests.gastos.GastoRequest;
import ttps.java.entregable6_v2.modelos.*;
import ttps.java.entregable6_v2.repository.GastoJPA;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

@org.springframework.stereotype.Service
public class GastoService {

    @Autowired
    private GastoJPA dao;


    @Autowired
    UsuarioService usuarioService;

    @Autowired
    GrupoService grupoService;

    @Autowired
    ServicePago pagoService;

    public void persistir(Gasto entity) {
        dao.save(entity);
    }


    public Gasto recuperar(Long id) throws GastoException {
        return dao.findById(id).orElseThrow(() -> new GastoException("No se encontro el gasto"));
    }

    public Gasto recuperarGastoParticular(Long id, long idGasto) {
        return dao.obtenerGastoParticular(id, idGasto);
    }


    public void actualizar(Gasto entity) throws GastoException {
        Gasto gasto = this.recuperar(entity.getId());
        dao.save(entity);
    }


    public Gasto crearGasto(GastoRequest gastoCreateRequest, String imagen) throws UsuarioInvalidoException, GastoException {
        Grupo grupo=null;
        if (gastoCreateRequest.getGrupoBool() != null && gastoCreateRequest.getGrupoBool()) {
            grupo = grupoService.recuperar(gastoCreateRequest.getId_grupo());
        }
        HashMap<Usuario, Double> usuariosValores = usuarioService.usuariosGastoValores(gastoCreateRequest);
        Gasto gasto = new Gasto(gastoCreateRequest.getMonto(), gastoCreateRequest.getFecha(), imagen, usuariosValores, usuarioService.recuperar(gastoCreateRequest.getResponsable()), grupo, gastoCreateRequest.getTipo(), gastoCreateRequest.getDivision());

        this.persistir(gasto);
        Pago pago = new Pago(LocalDate.now(), gastoCreateRequest.getMonto(), usuarioService.recuperar(gastoCreateRequest.getResponsable()),gasto);

        pagoService.persistir(pago);

        return gasto;

    }

    public Gasto actualizarGasto(Long id, Usuario user, GastoRequest gastoUpdateRequest, String imagen) throws GastoException, GrupoException, UsuarioInvalidoException, IOException {
        Gasto gasto = this.recuperar(id);
        if (!gasto.getResponsable().getUsuario().equals(user.getUsuario())) {
            throw new GastoException("No tiene permisos para modificar el gasto");
        }
        Grupo grupo=null;
        if (gastoUpdateRequest.getGrupoBool() != null && gastoUpdateRequest.getGrupoBool()) {
            grupo = grupoService.recuperar(gastoUpdateRequest.getId_grupo());
        }
        HashMap<Usuario, Double> usuariosGastos = usuarioService.usuariosGastoValores(gastoUpdateRequest);
        Usuario responsable = usuarioService.recuperar(gastoUpdateRequest.getResponsable());
        ttps.java.entregable6_v2.helpers.actualizarGasto.Gasto.actualizarGasto(gasto,responsable, gastoUpdateRequest, grupo, usuariosGastos,imagen);
        this.actualizar(gasto);
        return gasto;
    }


    public Page<Gasto> recuperarGastosPaginados(Long id, int page, int pageSize, LocalDate fechaDesde, LocalDate fechaHasta, String nombreGrupo, TipoGasto tipoGasto) {
        return dao.recuperarGastosPaginados(id, fechaDesde, fechaHasta, nombreGrupo, tipoGasto, PageRequest.of(page, pageSize));
    }
}
