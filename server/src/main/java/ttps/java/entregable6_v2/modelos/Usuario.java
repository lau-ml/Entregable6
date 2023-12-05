package ttps.java.entregable6_v2.modelos;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    @Column(unique = true)
    private String usuario;
    private String contrasena;
    @Column(unique = true)
    private String email;

    private Double saldo;


    @ManyToMany
    @JoinTable(
            name = "usuario_grupos",
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "grupo_id", referencedColumnName = "id"
            )
    )
    private Set<Grupo> grupos;


    public Usuario(String nombre, String apellido, String username, String email, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.usuario = username;
        this.email = email;
        this.contrasena = password;
        this.grupos = new HashSet<>();
        this.requestedFriends = new HashSet<>();
        this.receivedFriends = new HashSet<>();
        this.requestedGroups = new HashSet<>();
        this.receivedGroups = new HashSet<>();
        this.amigos = new HashSet<>();
    }

    public Set<Grupo> getGrupos() {
        return grupos;
    }

    @OneToMany
    private Set<Usuario> amigos;

    public Set<Usuario> getAmigos() {
        return amigos;
    }

    public void setAmigos(Set<Usuario> amigos) {
        this.amigos = amigos;
    }

    public void setGrupos(Set<Grupo> grupos) {
        this.grupos = grupos;
    }

    public void agregarGrupo(Grupo grupo) {
        this.grupos.add(grupo);
    }

    public void agregarAmigo(Usuario amigo) {
        this.amigos.add(amigo);
    }

    @OneToMany(mappedBy = "groupRequester")
    private Set<SolicitudGrupo> requestedGroups;

    @OneToMany(mappedBy = "groupReceiver")
    private Set<SolicitudGrupo> receivedGroups;

    @OneToMany(mappedBy = "friendRequester")
    private Set<SolicitudAmistad> requestedFriends;

    @OneToMany(mappedBy = "friendReceiver")
    private Set<SolicitudAmistad> receivedFriends;

    public Set<SolicitudGrupo> getRequestedGroups() {
        return requestedGroups;
    }

    public Set<SolicitudGrupo> getReceivedGroups() {
        return receivedGroups;
    }

    public Set<SolicitudAmistad> getRequestedFriends() {
        return requestedFriends;
    }

    public Set<SolicitudAmistad> getReceivedFriends() {
        return receivedFriends;
    }

    public Usuario(String nombre, String apellido, String usuario, String contrasena, String email, double saldo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.email = email;
        this.saldo = saldo;
        this.grupos = new HashSet<>();
        this.requestedFriends = new HashSet<>();
        this.receivedFriends = new HashSet<>();
        this.requestedGroups = new HashSet<>();
        this.receivedGroups = new HashSet<>();
        this.amigos = new HashSet<>();
    }

    public Usuario() {

        this.grupos = new HashSet<>();
        this.requestedFriends = new HashSet<>();
        this.receivedFriends = new HashSet<>();
        this.requestedGroups = new HashSet<>();
        this.receivedGroups = new HashSet<>();
        this.amigos = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
