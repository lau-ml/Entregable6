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
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.TipoGasto;
import ttps.java.entregable6_v2.modelos.Usuario;
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

    public void persistir(Gasto entity) {
        dao.save(entity);
    }


    public Gasto recuperar(Long id) throws GastoException {
        return dao.findById(id).orElseThrow(() -> new GastoException("No se encontro el gasto"));
    }


    public void actualizar(Gasto entity) throws GastoException {
        Gasto gasto = this.recuperar(entity.getId());
        dao.save(entity);
    }


    public Gasto crearGasto(GastoRequest gastoCreateRequest, String imagen) throws UsuarioInvalidoException {
        Grupo grupo = gastoCreateRequest.getGrupoBool() ? grupoService.recuperar(gastoCreateRequest.getId_grupo()) : null;
        HashMap<Usuario, Double> usuariosValores = usuarioService.usuariosGastoValores(gastoCreateRequest);
        Gasto gasto = new Gasto(gastoCreateRequest.getMonto(), gastoCreateRequest.getFecha(), imagen, usuariosValores, usuarioService.recuperar(gastoCreateRequest.getResponsable()), grupo, gastoCreateRequest.getTipo(), gastoCreateRequest.getDivision());
        this.persistir(gasto);
        return gasto;

    }

    public Gasto actualizarGasto(Long id, Usuario user, GastoRequest gastoUpdateRequest, MultipartFile imagen) throws GastoException, GrupoException, UsuarioInvalidoException, IOException {
        Gasto gasto = this.recuperar(id);
        if (!gasto.getResponsable().getUsuario().equals(user.getUsuario())) {
            throw new GastoException("No tiene permisos para modificar el gasto");
        }
        Grupo grupo = grupoService.recuperar(gastoUpdateRequest.getId_grupo());
        HashMap<Usuario, Double> usuariosGastos = usuarioService.usuariosGastoValores(gastoUpdateRequest);
        ttps.java.entregable6_v2.helpers.actualizarGasto.Gasto.actualizarGasto(gasto, gastoUpdateRequest, grupo, usuariosGastos, ImageUtils.guardarImagen(imagen));
        this.actualizar(gasto);
        return gasto;

    }


    public Page<Gasto> recuperarGastosPaginados(Long id, int page, int pageSize, LocalDate fechaDesde, LocalDate fechaHasta, String nombreGrupo, TipoGasto tipoGasto) {
        return dao.recuperarGastosPaginados(id, fechaDesde, fechaHasta, nombreGrupo, tipoGasto, PageRequest.of(page, pageSize));
    }
}
