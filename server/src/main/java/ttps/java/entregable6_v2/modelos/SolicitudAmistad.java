package ttps.java.entregable6_v2.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity

public class SolicitudAmistad extends Solicitud {


    @ManyToOne
    private Usuario friendRequester;

    @ManyToOne
    private Usuario friendReceiver;

    public SolicitudAmistad(Usuario friendRequester, Usuario friendReceiver) {
        this.friendRequester = friendRequester;
        this.friendReceiver = friendReceiver;
    }

    public Usuario getFriendRequester() {
        return friendRequester;
    }

    public Usuario getFriendReceiver() {
        return friendReceiver;
    }

    public SolicitudAmistad() {
        super();
    }
}
