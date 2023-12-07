package ttps.java.entregable6_v2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ttps.java.entregable6_v2.modelos.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioJPA extends JpaRepository<Usuario, Long> {

    public Optional<Usuario> findByUsuario(String usuario);

    public Usuario findByEmail(String email);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.grupos WHERE u.id = :id")
    public Usuario recuperarConGrupos(long id);


}
