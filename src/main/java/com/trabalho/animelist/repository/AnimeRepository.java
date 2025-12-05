package com.trabalho.animelist.repository;

import com.trabalho.animelist.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
    Optional<Anime> findByApiId(Long apiId);
}