package ttps.java.entregable6_v2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ttps.java.entregable6_v2.modelos.Pago;
@Repository
public interface PagoJPA extends JpaRepository<Pago, Long> {


}
