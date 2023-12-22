package ttps.java.entregable6_v2.modelos;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "grupos")
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private Categoria categoria;

    @Column(name = "saldo")
    private double saldo;


    @ManyToMany(mappedBy = "grupos")
    private Set<Usuario> participantes = new HashSet<>();

    // Otros atributos y métodos


    @OneToMany(mappedBy = "grupo")
    private Set<SolicitudGrupo> solicitudesGrupo;

    @OneToMany(mappedBy = "grupo")
    private Set<Gasto> gastos;

    public Grupo() {
    }

    public Grupo(String nombre, Categoria categoria, double saldo, List<Usuario> integrantes, List<Gasto> gastos) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.saldo = saldo;

    }

    public Grupo(String nombre, Categoria categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.saldo = 0;
    }

    public Grupo(String nombre, Categoria categoria, Double saldo) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.saldo = saldo;
    }

    // Getters y setters

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Set<Usuario> getParticipantes() {
        return participantes;
    }

    public Long getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }

    // Métodos adicionales, por ejemplo, para agregar integrantes y gastos

    public double getSaldo() {
        return saldo;
    }

    public Categoria getCategoria() {
        return categoria;
    }
}

