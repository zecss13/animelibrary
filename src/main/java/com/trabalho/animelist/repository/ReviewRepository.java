package com.trabalho.animelist.repository;

import com.trabalho.animelist.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByAnimeId(Long animeId);

    List<Review> findByUsuarioId(Long usuarioId);
    
    boolean existsByUsuarioIdAndAnimeId(Long usuarioId, Long animeId);
}