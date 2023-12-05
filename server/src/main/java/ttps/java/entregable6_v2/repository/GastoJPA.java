package ttps.java.entregable6_v2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ttps.java.entregable6_v2.modelos.Gasto;

@Repository
public interface GastoJPA extends JpaRepository<Gasto, Long> {

    @Query("select g from Gasto g inner join Usuario u on(u.id= g.responsable.id) inner join Grupo gru on (gru.id=g.grupo.id)  where g.id = :id")
    public Gasto obtenerGasto(Long id);

}
