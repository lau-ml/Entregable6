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
import ttps.java.entregable6_v2.helpers.requests.grupos.GrupoUpdateRequest;
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.Usuario;
import ttps.java.entregable6_v2.repository.GastoJPA;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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


    public Gasto crearGasto(GastoRequest gastoCreateRequest, String imagen) throws  UsuarioInvalidoException {
        Grupo grupo = grupoService.recuperar(gastoCreateRequest.getId_grupo());
        Set<Usuario> usuarios = new HashSet<>();
        Map<Usuario, Double> valores = new HashMap<>();
        usuarioService.usuariosValoresGasto(gastoCreateRequest, usuarios, valores);
        Gasto gasto = new Gasto(gastoCreateRequest.getMonto(), gastoCreateRequest.getFecha(), imagen, usuarios, usuarioService.recuperarUsuario(), grupo, gastoCreateRequest.getTipo(), valores, gastoCreateRequest.getDivision());
        this.persistir(gasto);
        return gasto;

    }

    public Gasto actualizarGasto(Long id, Usuario user, GastoRequest gastoUpdateRequest, MultipartFile imagen) throws GastoException, GrupoException, UsuarioInvalidoException, IOException {
        Gasto gasto = this.recuperar(id);
        if (!gasto.getResponsable().getUsuario().equals(user.getUsuario())) {
            throw new GastoException("No tiene permisos para modificar el gasto");
        }
        Grupo grupo = grupoService.recuperar(gastoUpdateRequest.getId_grupo());
        Set<Usuario> usuarios = new HashSet<>();
        Map<Usuario, Double> valores = new HashMap<>();
        usuarioService.usuariosValoresGasto(gastoUpdateRequest, usuarios, valores);
        ttps.java.entregable6_v2.helpers.actualizarGasto.Gasto.actualizarGasto(gasto, gastoUpdateRequest, grupo, usuarios, valores, ImageUtils.guardarImagen(imagen));
        this.actualizar(gasto);
        return gasto;

    }

    public Page<Gasto> recuperarGastosPaginados(Long id, int page, int pageSize) {
        return dao.recuperarGastosPaginados(id, PageRequest.of(page, pageSize));
    }


}
