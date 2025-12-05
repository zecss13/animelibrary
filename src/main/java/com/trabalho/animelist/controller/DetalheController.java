package com.trabalho.animelist.controller;

import com.trabalho.animelist.dto.JikanResponse;
import com.trabalho.animelist.model.Anime;
import com.trabalho.animelist.model.Review;
import com.trabalho.animelist.model.Usuario;
import com.trabalho.animelist.repository.AnimeRepository;
import com.trabalho.animelist.repository.ReviewRepository;
import com.trabalho.animelist.repository.UsuarioRepository;
import com.trabalho.animelist.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class DetalheController {

    @Autowired
    private AnimeService animeService;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/anime/{apiId}")
    public String paginaDetalhes(
            @PathVariable Long apiId, 
            @RequestParam(required = false) String termo,
            Model model, 
            Principal principal) {
        
        JikanResponse.AnimeData dadosApi = animeService.buscarPorId(apiId);
        model.addAttribute("dadosApi", dadosApi);

        Optional<Anime> animeNoBanco = animeRepository.findByApiId(apiId);
        List<Review> reviews = new ArrayList<>();
        if (animeNoBanco.isPresent()) {
            reviews = reviewRepository.findByAnimeId(animeNoBanco.get().getId());
        }
        model.addAttribute("reviews", reviews);

        if (principal != null) {
            Usuario logado = usuarioRepository.findByEmail(principal.getName()).orElse(null);
            model.addAttribute("usuarioLogado", logado);
        }

        model.addAttribute("termoAnterior", termo);

        return "detalhes"; 
    }
}