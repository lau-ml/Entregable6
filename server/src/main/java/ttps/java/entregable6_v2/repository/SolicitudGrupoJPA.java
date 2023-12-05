package ttps.java.entregable6_v2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.SolicitudGrupo;
import ttps.java.entregable6_v2.modelos.Usuario;

@Repository
public interface SolicitudGrupoJPA extends JpaRepository<SolicitudGrupo, Long> {

    @Query("select s from SolicitudGrupo s where " +
            "((s.groupReceiver = :usuario and s.grupo = :grupo) " +
            "or (s.groupRequester = :usuario and s.grupo = :grupo))")
    public SolicitudGrupo encontrarSolicitudGrupo(@Param("usuario") Usuario usuario, @Param("grupo") Grupo grupo);

}
