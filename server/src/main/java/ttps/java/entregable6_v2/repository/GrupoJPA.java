package ttps.java.entregable6_v2.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ttps.java.entregable6_v2.modelos.Categoria;
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.Usuario;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Repository
public interface GrupoJPA extends JpaRepository<Grupo, Long>  {

    @Query("SELECT u FROM Usuario u JOIN u.grupos g WHERE g.id = :grupoId AND u.id = :userId")
    public Usuario usuarioPerteneciente(@Param("grupoId") Long grupoId, @Param("userId") Long userId);

    @Query("SELECT DISTINCT g FROM Gasto g " +
            "LEFT JOIN FETCH g.valores " +
            "INNER JOIN g.responsable u " +
            "INNER JOIN g.grupo gr " +
            "WHERE gr.id = :id")
    public List<Gasto> recuperarTodosLosGastos(@Param("id") long id);

    @Query("SELECT DISTINCT u FROM Usuario u " +
            "LEFT JOIN FETCH u.grupos g " +
            "WHERE g.id = :id")

    public Usuario obtenerUsuariosGrupo(@Param("id") long id);

    @Query("SELECT g FROM Grupo g JOIN g.participantes u " +
            "WHERE u.id = :userId " +
            "AND (:nombre = '' OR g.nombre ilike %:nombre%) " +
            "AND (:categoria IS NULL OR g.categoria = :categoria)")
    Page<Grupo> recuperarGruposPaginados(
            @Param("userId") Long userId,
            @Param("nombre") String nombre,
            @Param("categoria") Categoria categoria,
            Pageable pageable
    );


}
