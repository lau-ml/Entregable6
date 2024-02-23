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

@Service
public class SolictudGrupoService extends SolicitudService {
    @Autowired
    private SolicitudGrupoJPA dao;
    @Autowired
    private UsuarioJPA usuarioDAO;
    @Autowired
    private GrupoService grupoService;




    public ResponseEntity<?> aceptarSolicitud(SolicitudGrupo solicitudPersistir) throws Exception {
        try {

            SolicitudGrupo solicitud = dao.encontrarSolicitudGrupo(solicitudPersistir.getGroupReceiver(), solicitudPersistir.getGrupo());
            if (solicitud == null)
                return new ResponseEntity<>("No existe la solicitud de amistad", HttpStatus.BAD_REQUEST);
            if (solicitud.getEstado() == SolicitudState.ACEPTADA)
                return new ResponseEntity<>("La solicitud ya ha sido aceptada", HttpStatus.BAD_REQUEST);
            if (solicitud.getEstado() == SolicitudState.RECHAZADA)
                return new ResponseEntity<>("La solicitud ya ha sido rechazada", HttpStatus.BAD_REQUEST);
            solicitudPersistir.setEstado(SolicitudState.ACEPTADA);
            dao.save(solicitudPersistir);
            solicitud.getGroupReceiver().agregarGrupo(solicitud.getGrupo());
            usuarioDAO.save(solicitud.getGroupReceiver());
            return new ResponseEntity<>(solicitudPersistir, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al aceptar la solicitud de grupo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean rechazarSolicitud(SolicitudGrupo solicitudPersistir) throws Exception {
        try {
            SolicitudGrupo solicitud = dao.encontrarSolicitudGrupo(solicitudPersistir.getGroupReceiver(), solicitudPersistir.getGrupo());
            if (solicitud == null)
                throw new Exception("No existe la solicitud de amistad");
            if (solicitud.getEstado() == SolicitudState.ACEPTADA)
                throw new Exception("La solicitud ya ha sido aceptada");
            if (solicitud.getEstado() == SolicitudState.RECHAZADA)
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


}
