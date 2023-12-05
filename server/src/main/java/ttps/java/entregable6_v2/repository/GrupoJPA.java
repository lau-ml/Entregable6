package ttps.java.entregable6_v2.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.Usuario;

import java.util.List;

@Repository
public interface GrupoJPA extends JpaRepository<Grupo, Long>  {

    @Query("SELECT u FROM Usuario u JOIN u.grupos g WHERE g.id = :grupoId AND u.id = :userId")
    public Usuario usuarioPerteneciente(@Param("grupoId") Long grupoId, @Param("userId") Long userId);

    @Query("SELECT DISTINCT g FROM Gasto g " +
            "LEFT JOIN FETCH g.valores " +
            "LEFT JOIN FETCH g.participantes " +
            "INNER JOIN g.responsable u " +
            "INNER JOIN g.grupo gr " +
            "WHERE gr.id = :id")
    public List<Gasto> recuperarTodosLosGastos(@Param("id") long id);

}
