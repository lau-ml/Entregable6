package ttps.java.entregable6_v2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ttps.java.entregable6_v2.modelos.SolicitudAmistad;

@Repository
public interface SolicitudAmistadJPA  extends JpaRepository<SolicitudAmistad, Long> {


    @Query("select s from SolicitudAmistad s where " +
            "((s.friendReceiver.usuario = :usuario and s.friendRequester.usuario = :usuarioSolicitado) " +
            "or (s.friendReceiver.usuario = :usuarioSolicitado and s.friendRequester.usuario = :usuario))")

    public SolicitudAmistad encontrarSolicitudAmistad(String usuario, String usuarioSolicitado) throws Exception;

}



