package ttps.java.entregable6_v2.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ttps.java.entregable6_v2.repository.SolicitudJPA;

@org.springframework.stereotype.Service
public class SolicitudService {

    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudJPA dao;

}
