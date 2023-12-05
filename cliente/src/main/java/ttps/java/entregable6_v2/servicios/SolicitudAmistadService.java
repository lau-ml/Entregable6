package ttps.java.entregable6_v2.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ttps.java.entregable6_v2.modelos.SolicitudAmistad;
import ttps.java.entregable6_v2.modelos.SolicitudState;
import ttps.java.entregable6_v2.repository.SolicitudAmistadJPA;
import ttps.java.entregable6_v2.repository.UsuarioJPA;

@Service
public class SolicitudAmistadService extends SolicitudService {

    @Autowired
    private SolicitudAmistadJPA dao;
    @Autowired
    private UsuarioJPA usuarioDAO;

    public boolean aceptarSolicitud(SolicitudAmistad solicitudPersistir) throws Exception {
        try {
            SolicitudAmistad solicitud = dao.encontrarSolicitudAmistad(solicitudPersistir.getFriendRequester().getUsuario(), solicitudPersistir.getFriendReceiver().getUsuario());
            if (solicitud == null)
                throw new Exception("No existe la solicitud de amistad");
            if (solicitud.getEstado() == SolicitudState.ACEPTADA)
                throw new Exception("La solicitud ya ha sido aceptada");
            if (solicitud.getEstado() == SolicitudState.RECHAZADA)
                throw new Exception("La solicitud ya ha sido rechazada");
            solicitudPersistir.setEstado(SolicitudState.ACEPTADA);
            dao.save(solicitudPersistir);

            solicitud.getFriendReceiver().agregarAmigo(solicitud.getFriendRequester());
            solicitud.getFriendRequester().agregarAmigo(solicitud.getFriendReceiver());
            usuarioDAO.save(solicitud.getFriendReceiver());
            usuarioDAO.save(solicitud.getFriendRequester());
            return true;
        } catch (Exception e) {
            throw new Exception("Error al aceptar la solicitud de amistad");
        }
    }

    public boolean rechazarSolicitud(SolicitudAmistad solicitudPersistir) throws Exception {
        try {
            SolicitudAmistad solicitud = dao.encontrarSolicitudAmistad(solicitudPersistir.getFriendRequester().getUsuario(), solicitudPersistir.getFriendReceiver().getUsuario());
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
            throw new Exception("Error al rechazar la solicitud de amistad");
        }
    }

    public boolean enviarSolicitud(SolicitudAmistad solicitudPersistir) throws Exception {
        try {
            SolicitudAmistad solicitud = dao.encontrarSolicitudAmistad(solicitudPersistir.getFriendRequester().getUsuario(), solicitudPersistir.getFriendReceiver().getUsuario());
            if (solicitud != null)
                throw new Exception("Ya existe la solicitud de amistad");
            if (solicitudPersistir.getFriendRequester().getUsuario().equals(solicitudPersistir.getFriendReceiver().getUsuario()))
                throw new Exception("No se puede enviar una solicitud de amistad a si mismo");
            solicitudPersistir.setEstado(SolicitudState.PENDIENTE);
            dao.save(solicitudPersistir);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al enviar la solicitud de amistad");
        }
    }

}
