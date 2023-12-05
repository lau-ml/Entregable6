package ttps.java.entregable6_v2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ttps.java.entregable6_v2.modelos.Gasto;

@Repository("solicitudDAO")
public interface SolicitudJPA extends JpaRepository<Gasto, Long> {
}
