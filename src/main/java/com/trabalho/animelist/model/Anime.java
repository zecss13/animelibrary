package com.trabalho.animelist.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "animes")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long apiId;

    private String titulo;

    private String imagemUrl;

    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL)
    private List<Review> reviews;
}