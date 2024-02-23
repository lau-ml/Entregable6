package ttps.java.entregable6_v2.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ttps.java.entregable6_v2.modelos.Solicitud;
import ttps.java.entregable6_v2.modelos.SolicitudGrupo;
import ttps.java.entregable6_v2.modelos.SolicitudState;
import ttps.java.entregable6_v2.repository.SolicitudGrupoJPA;
import ttps.java.entregable6_v2.repository.UsuarioJPA;

import java.util.List;

@Service
public class SolictudGrupoService extends SolicitudService {
    @Autowired
    private SolicitudGrupoJPA dao;
    @Autowired
    private UsuarioJPA usuarioDAO;
    @Autowired
    private GrupoService grupoService;




    public boolean aceptarSolicitud(SolicitudGrupo solicitudPersistir) throws Exception {
        try {
            if (solicitudPersistir == null)
                throw new Exception("No existe la solicitud");
            if (solicitudPersistir.getEstado() == SolicitudState.ACEPTADA)
                throw new Exception("La solicitud ya ha sido aceptada");
            if (solicitudPersistir.getEstado() == SolicitudState.RECHAZADA)
                throw new Exception("La solicitud ya ha sido rechazada");
            solicitudPersistir.setEstado(SolicitudState.ACEPTADA);
            dao.save(solicitudPersistir);
            solicitudPersistir.getGroupReceiver().agregarGrupo(solicitudPersistir.getGrupo());
            usuarioDAO.save(solicitudPersistir.getGroupReceiver());
            return true;
        } catch (Exception e) {
            throw new Exception("Error al aceptar la solicitud de grupo");
        }
    }

    public boolean rechazarSolicitud(SolicitudGrupo solicitudPersistir) throws Exception {
        try {

            if (solicitudPersistir.getEstado() == SolicitudState.ACEPTADA)
                throw new Exception("La solicitud ya ha sido aceptada");
            if (solicitudPersistir.getEstado() == SolicitudState.RECHAZADA)
                throw new Exception("La solicitud ya ha sido rechazada");
            solicitudPersistir.setEstado(SolicitudState.RECHAZADA);
            dao.save(solicitudPersistir);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al rechazar la solicitud de grupo");
        }
    }

    public ResponseEntity<?> enviarSolicitud(SolicitudGrupo solicitudPersistir) throws Exception {
        try {
            SolicitudGrupo solicitud = dao.encontrarSolicitudGrupo(solicitudPersistir.getGroupReceiver(), solicitudPersistir.getGrupo());
            if (solicitud != null)
                return new ResponseEntity<>("Ya existe la solicitud de amistad", HttpStatus.BAD_REQUEST);
            if (solicitudPersistir.getGroupReceiver().getUsuario().equals(solicitudPersistir.getGroupRequester().getUsuario()))
                return new ResponseEntity<>("No puede enviarse la solicitud a si mismo", HttpStatus.BAD_REQUEST);
            if (grupoService.usuarioPerteneciente(solicitudPersistir.getGrupo(), solicitudPersistir.getGroupRequester()) == null) {
                return new ResponseEntity<String>("El usuario no pertenece al grupo", HttpStatus.UNAUTHORIZED);
            }
            if (grupoService.usuarioPerteneciente(solicitudPersistir.getGrupo(), solicitudPersistir.getGroupReceiver()) != null) {
                return new ResponseEntity<String>("El usuario ya pertenece al grupo", HttpStatus.UNAUTHORIZED);
            }
            solicitudPersistir.setEstado(SolicitudState.PENDIENTE);
            dao.save(solicitudPersistir);
            return new ResponseEntity<>("Solicitud enviada", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al enviar la solicitud de grupo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   public Page<SolicitudGrupo> solicitudesPendientes(long id, int page, int pageSize) {
        return dao.findAllByGroupReceiver(id, PageRequest.of(page, pageSize));
    }

    public SolicitudGrupo encontrarSolicitudGrupo(long id) {
        return dao.findById(id).orElse(null);
    }


    public List<String> getSolicitudesEnviadasUsuarios(long id) {
        return dao.getSolicitudesEnviadasUsuarios(id);
    }
}
