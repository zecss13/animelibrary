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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ReviewController {

    @Autowired private AnimeService animeService;
    @Autowired private AnimeRepository animeRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    @GetMapping("/anime/{apiId}/nova-review")
    public String paginaNovaReview(@PathVariable Long apiId, 
                                   @RequestParam(required = false) Long id,
                                   @RequestParam(required = false) String termo, 
                                   Model model) {
        
        JikanResponse.AnimeData dadosApi = animeService.buscarPorId(apiId);
        model.addAttribute("anime", dadosApi);
        model.addAttribute("termo", termo);

        if (id != null) {
            Review reviewExistente = reviewRepository.findById(id).get();
            model.addAttribute("review", reviewExistente);
        }

        return "nova-review";
    }

    @PostMapping("/salvar-review")
    public String salvarReview(
            @RequestParam(required = false) Long id,
            @RequestParam("apiId") Long apiId,
            @RequestParam("titulo") String titulo,
            @RequestParam("imagemUrl") String imagemUrl,
            @RequestParam("texto") String texto,
            @RequestParam("nota") Double nota,
            @RequestParam(required = false) String termo,
            Principal principal) {

        Usuario usuario = usuarioRepository.findByEmail(principal.getName()).get();
        
        Anime anime = animeRepository.findByApiId(apiId)
                .orElseGet(() -> {
                    Anime novo = new Anime();
                    novo.setApiId(apiId);
                    novo.setTitulo(titulo);
                    novo.setImagemUrl(imagemUrl);
                    return animeRepository.save(novo);
                });

        Review review;
        if (id != null) {
            review = reviewRepository.findById(id).get();
            if (!review.getUsuario().getId().equals(usuario.getId())) {
                 return "redirect:/anime/" + apiId + "?erro=sempermissao";
            }
        } else {
            review = new Review();
            review.setUsuario(usuario);
            review.setAnime(anime);
        }

        review.setTexto(texto);
        review.setNota(nota);
        reviewRepository.save(review);

        String redirectUrl = "redirect:/anime/" + apiId;
        
        if (termo != null && !termo.isEmpty()) {
            redirectUrl += "?termo=" + termo;
        }

        return redirectUrl;
    }

    @PostMapping("/deletar-review")
    public String deletarReview(
            @RequestParam Long id, 
            @RequestParam(required = false) String origem,
            Principal principal) {
        
        Review review = reviewRepository.findById(id).get();
        Usuario usuarioLogado = usuarioRepository.findByEmail(principal.getName()).get();

        if (review.getUsuario().getId().equals(usuarioLogado.getId()) || "ADMIN".equals(usuarioLogado.getRole())) {
            Long apiId = review.getAnime().getApiId();
            
            reviewRepository.delete(review);

            if ("admin".equals(origem)) {
                return "redirect:/admin/reviews";
            }
            
            return "redirect:/anime/" + apiId;
        }

        return "redirect:/home?erro=naoautorizado";
    }
}