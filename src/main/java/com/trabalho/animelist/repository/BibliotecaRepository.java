package com.trabalho.animelist.repository;

import com.trabalho.animelist.model.Biblioteca;
import com.trabalho.animelist.model.Usuario;
import com.trabalho.animelist.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface BibliotecaRepository extends JpaRepository<Biblioteca, Long> {
    
    Optional<Biblioteca> findByUsuarioAndAnime(Usuario usuario, Anime anime);

    List<Biblioteca> findByUsuario(Usuario usuario);
}