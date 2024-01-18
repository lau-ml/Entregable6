package ttps.java.entregable6_v2.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ttps.java.entregable6_v2.modelos.Gasto;
import ttps.java.entregable6_v2.repository.GastoJPA;

@org.springframework.stereotype.Service
public class GastoService {

    @Autowired
    private GastoJPA dao;


    public void persistir(Gasto entity) throws Exception {
        dao.save(entity);
    }


    public Gasto recuperar(Long id) throws Exception {
        return dao.findById(id).orElse(null);
    }


    public boolean actualizar(Gasto entity) throws Exception {

        Gasto gasto = dao.findById(entity.getId()).orElse(null);
        if (gasto != null) {
            dao.save(entity);
            return true;
        } else {
            return false;
        }
    }

    public Page<Gasto> recuperarGastosPaginados(Long id, int page, int pageSize) {
        return dao.recuperarGastosPaginados(id,  PageRequest.of(page, pageSize));
    }
}
