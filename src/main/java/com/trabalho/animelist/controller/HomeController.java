package com.trabalho.animelist.controller;

import com.trabalho.animelist.service.AnimeService;
import com.trabalho.animelist.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AnimeService animeService;

    @GetMapping("/home")
    public String paginaHome(Model model) {
        model.addAttribute("topAnimes", animeService.buscarTopAnimes());
        return "home";
    }

    @GetMapping("/perfil")
    public String paginaPerfil(Model model, Principal principal) {
        var usuario = usuarioRepository.findByEmail(principal.getName()).get();
        model.addAttribute("usuarioLogado", usuario);
        return "perfil";
    }

    @GetMapping("/")
    public String raiz() {
        return "redirect:/login";
    }
}