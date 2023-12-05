package ttps.java.entregable6_v2.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class SolicitudGrupo extends Solicitud {
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Grupo grupo;


    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Usuario groupRequester;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Usuario groupReceiver;

    public Grupo getGrupo() {
        return grupo;
    }

    public Usuario getGroupRequester() {
        return groupRequester;
    }

    public Usuario getGroupReceiver() {
        return groupReceiver;
    }

    public SolicitudGrupo(Grupo grupo, Usuario groupRequester, Usuario groupReceiver) {
        super();
        this.grupo = grupo;
        this.groupRequester = groupRequester;
        this.groupReceiver = groupReceiver;

    }


    public SolicitudGrupo() {
        super();
    }

}
