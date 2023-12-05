package ttps.java.entregable6_v2.modelos;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SolicitudState estado;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    public Solicitud() {
        this.estado = SolicitudState.PENDIENTE;
        this.fecha = new Date();
    }

    public SolicitudState getEstado() {
        return estado;
    }

    public void setEstado(SolicitudState estado) {
        this.estado = estado;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Solicitud && ((Solicitud) obj).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    public void aceptarSolicitud() {
        this.estado = SolicitudState.ACEPTADA;
    }

    public void rechazarSolicitud() {
        this.estado = SolicitudState.RECHAZADA;
    }

    public Date getFecha() {
        return fecha;
    }
}
