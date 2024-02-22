package ttps.java.entregable6_v2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.modelos.TipoGasto;

import java.time.LocalDate;

@Repository
public interface GastoJPA extends JpaRepository<Gasto, Long> {


    @Query("SELECT DISTINCT g FROM Gasto g LEFT JOIN FETCH g.grupo LEFT JOIN FETCH g.valores u LEFT JOIN FETCH g.valores otherUsers WHERE KEY(u).id = :id "
    +"AND g.id = :idGasto"
    )
    public Gasto obtenerGastoParticular(Long id, long idGasto);


    @Query("SELECT DISTINCT g FROM Gasto g LEFT JOIN FETCH g.grupo LEFT JOIN FETCH g.valores u LEFT JOIN FETCH g.valores otherUsers WHERE KEY(u).id = :id " +
            "AND(:tipoGasto IS NULL OR g.tipo = :tipoGasto)" +
            "AND(cast(:fechaDesde as localdate )IS NULL OR g.fecha >= :fechaDesde)" +
            "AND(cast(:fechaHasta as localdate)  IS NULL OR g.fecha <= :fechaHasta)" +
            "AND(:nombreGrupo = '' OR g.grupo.nombre ilike %:nombreGrupo%)"
    )
    Page<Gasto> recuperarGastosPaginados(@Param("id") Long id,
                                         @Param("fechaDesde") LocalDate fechaDesde,
                                         @Param("fechaHasta") LocalDate fechaHasta,
                                         @Param("nombreGrupo") String nombreGrupo,
                                         @Param("tipoGasto") TipoGasto tipoGasto,
                                         Pageable pageable);
}
