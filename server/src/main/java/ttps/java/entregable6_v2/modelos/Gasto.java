package ttps.java.entregable6_v2.modelos;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "gastos")
public class Gasto {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private double monto;
    private Date fecha;
    private String imagen;

    @ManyToMany
    @JoinTable(
            name = "gasto_participantes",
            joinColumns = @JoinColumn(name = "gasto_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id")

    )
    private Set<Usuario> participantes;


    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private Usuario responsable;

    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;


    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_gasto")
    private TipoGasto tipo;

    @ElementCollection
    @CollectionTable(name = "gasto_valores", joinColumns = @JoinColumn(name = "gasto_id"))
    @MapKeyJoinColumn(name = "usuario_id", referencedColumnName = "id")


    @Column(name = "monto")
    private Map<Usuario, Double> valores;

    @OneToMany(mappedBy = "gasto")
    private Set<Pago> pagos;


    @Enumerated(EnumType.STRING)
    @Column(name = "division")
    private Division division;


    // Constructores

    public Gasto() {
    }

    public Gasto(String nombre,double monto, Date fecha, String imagen, List<Usuario> participantes, Usuario responsable, TipoGasto tipo, Map<Usuario, Double> valores, Division division) {
        this.nombre = nombre;
        this.monto = monto;
        this.fecha = fecha;
        this.imagen = imagen;
        this.responsable = responsable;
        this.tipo = tipo;
        this.valores = valores;
        this.division = division;
    }

    public Gasto(String nombre, double monto, Date fecha, String imagen, Set<Usuario> participantes, Usuario responsable, Grupo grupo, TipoGasto tipo, Map<Usuario, Double> valores, Division division) {
        this.nombre = nombre;
        this.monto = monto;
        this.fecha = fecha;
        this.imagen = imagen;
        this.responsable = responsable;
        this.grupo = grupo;
        this.tipo = tipo;
        this.valores = valores;
        this.participantes = participantes;
        this.division = division;
    }

    // Otros métodos

    public void saldarPago(Usuario pagador, double monto, Date fecha) {
        // Implementa esta función
    }

    // Getters y setters para las propiedades

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Set<Usuario> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(Set<Usuario> participantes) {
        this.participantes = participantes;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public TipoGasto getTipo() {
        return tipo;
    }

    public void setTipo(TipoGasto tipo) {
        this.tipo = tipo;
    }

    public Map<Usuario, Double> getValores() {
        return valores;
    }

    public void setValores(Map<Usuario, Double> valores) {
        this.valores = valores;
    }

    public Set<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(Set<Pago> pagos) {
        this.pagos = pagos;
    }

    public Division getDivision() {
        return division;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDivision(Division division) {
        this.division = division;
    }
}
