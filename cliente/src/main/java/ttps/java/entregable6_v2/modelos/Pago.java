package ttps.java.entregable6_v2.modelos;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    private double monto;


    @ManyToOne
    @JoinColumn(name = "usuario_id") // Ajusta el nombre de la columna según tu esquema de base de datos
    private Usuario pagador;

    @ManyToOne
    @JoinColumn(name = "gasto_id") // Ajusta el nombre de la columna según tu esquema de base de datos
    private Gasto gasto;


    public Pago() {
        // Constructor por defecto necesario para Hibernate
    }

    public Pago(Date fecha, double monto, Usuario pagador) {
        this.fecha = fecha;
        this.monto = monto;
        this.pagador = pagador;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Usuario getPagador() {
        return pagador;
    }

    public void setPagador(Usuario pagador) {
        this.pagador = pagador;
    }

    public void setGasto(Gasto gasto) {
        this.gasto = gasto;
    }

    public Gasto getGasto() {
        return gasto;
    }
}
