package ttps.java.entregable6_v2.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "grupos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_nombre_responsable", columnNames = {"nombre", "responsable_id"})
})
public class Grupo {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(name = "nombre")
    private String nombre;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private Categoria categoria;

    @Getter
    @Column(name = "saldo")
    private double saldo;


    @Getter
    @ManyToMany(mappedBy = "grupos")
    private Set<Usuario> participantes = new HashSet<>();

    // Otros atributos y métodos

    @ManyToOne
    private Usuario responsable;

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

    public Grupo(String nombre, Categoria categoria, double v, Usuario usuario) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.saldo = v;
        this.responsable = usuario;
    }

    // Getters y setters

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }


    // Métodos adicionales, por ejemplo, para agregar integrantes y gastos

}

