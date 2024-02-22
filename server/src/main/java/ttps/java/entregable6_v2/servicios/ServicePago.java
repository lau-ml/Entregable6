package ttps.java.entregable6_v2.servicios;

import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class ServicePago {
    @Autowired
    private ttps.java.entregable6_v2.repository.PagoJPA dao;
    public ServicePago() {
        super();
    }

    public void persistir(ttps.java.entregable6_v2.modelos.Pago entity) {
        dao.save(entity);
    }

    public ttps.java.entregable6_v2.modelos.Pago recuperar(Long id) {
        return null;
    }

    public void actualizar(ttps.java.entregable6_v2.modelos.Pago entity) {
    }

    public ttps.java.entregable6_v2.modelos.Pago crearPago(java.time.LocalDate now, double monto, ttps.java.entregable6_v2.modelos.Usuario recuperar) {
        return null;
    }


}
