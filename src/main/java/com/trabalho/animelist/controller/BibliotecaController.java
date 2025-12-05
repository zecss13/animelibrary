package com.trabalho.animelist.controller;

import com.trabalho.animelist.model.Biblioteca;
import com.trabalho.animelist.repository.BibliotecaRepository;
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
import java.util.List;

@Controller
public class BibliotecaController {

    @Autowired
    private AnimeService animeService;

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/adicionar-anime")
    public String adicionarAnime(
            @RequestParam("apiId") Long apiId,
            @RequestParam("titulo") String titulo,
            @RequestParam("imagemUrl") String imagemUrl,
            @RequestParam("totalEpisodios") Integer totalEpisodios,
            Principal principal) {
        
        String email = principal.getName();

        animeService.salvarAnimeNaLista(email, apiId, titulo, imagemUrl, totalEpisodios);

        return "redirect:/minha-lista";
    }

    @GetMapping("/minha-lista")
    public String paginaMinhaLista(Model model, Principal principal) {
        String email = principal.getName();
        
        var usuario = usuarioRepository.findByEmail(email).get();
        List<Biblioteca> minhaLista = bibliotecaRepository.findByUsuario(usuario);
        
        model.addAttribute("lista", minhaLista);
        
        return "minha-lista";
    }

    @GetMapping("/editar/{id}")
    public String paginaEditar(@PathVariable("id") Long id, Model model) {
        Biblioteca item = bibliotecaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item inválido: " + id));
        
        model.addAttribute("item", item);
        return "editar-anime";
    }

@PostMapping("/atualizar-anime")
    public String atualizarAnime(
            @RequestParam("id") Long id,
            @RequestParam("status") String status,
            @RequestParam("episodios") Integer episodios,
            @RequestParam("nota") Double nota,
            @RequestParam("comentario") String comentario,
            @RequestParam(value = "acao", required = false) String acao) {

        if ("excluir".equals(acao)) {
            bibliotecaRepository.deleteById(id);
            return "redirect:/minha-lista";
        }

        Biblioteca item = bibliotecaRepository.findById(id).get();
        item.setStatus(status);
        item.setEpisodiosAssistidos(episodios);
        item.setNotaPessoal(nota);
        item.setComentario(comentario);
        
        bibliotecaRepository.save(item);

        return "redirect:/minha-lista";
    }
}