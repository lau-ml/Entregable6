package ttps.java.entregable6_v2.modelos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @Column(name = "contra_code", length = 64)
    private String contraCode;

    private String nombre;
    private String apellido;
    @Column(unique = true)
    private String usuario;
    @JsonIgnore
    private String contrasena;
    @Column(unique = true)
    private String email;

    private Double saldo;

    private Boolean activo;

    @ManyToMany
    @JoinTable(
            name = "usuario_grupos",
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "grupo_id", referencedColumnName = "id"
            )
    )
    private Set<Grupo> grupos;


    @ManyToMany
    @JoinTable(
            name = "usuarios_amigos",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "amigo_id")
    )
    private Set<Usuario> amigos;


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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return usuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getActivo();
    }

    @Override
    public String toString(){
        return this.usuario;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(usuario.getUsuario(), usuario.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario);
    }
}
