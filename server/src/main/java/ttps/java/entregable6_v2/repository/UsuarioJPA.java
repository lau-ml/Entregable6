package ttps.java.entregable6_v2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ttps.java.entregable6_v2.modelos.Grupo;
import ttps.java.entregable6_v2.modelos.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioJPA extends JpaRepository<Usuario, Long> {

    public Optional<Usuario> findByUsuario(String usuario);

    public Usuario findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.verificationCode = ?1")
    public Usuario findByVerificationCode(String code);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.grupos WHERE u.id = :id")
    public Usuario recuperarConGrupos(long id);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.grupos WHERE u.usuario = :usuario")
    public Usuario recuperarConGrupos(String usuario);


    @Query("SELECT u FROM Usuario u WHERE u.contraCode = ?1")
    public Usuario findByContraCode(String code);

    @Query("SELECT u from Usuario u LEFT join fetch u.amigos WHERE u.id  !=  :id" +
            " AND (:usuario is null or u.usuario like %:usuario% ) ")
    Page<Usuario> getAmigos(Long id, String usuario, Pageable pageable );
}
