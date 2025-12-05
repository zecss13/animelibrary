package com.trabalho.animelist.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "biblioteca")
public class Biblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "anime_id")
    private Anime anime;

    private String status;

    private Integer episodiosAssistidos = 0;

    private Double notaPessoal;


    @Column(columnDefinition = "TEXT")
    private String comentario;
}