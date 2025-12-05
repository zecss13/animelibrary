package com.trabalho.animelist.controller;

import com.trabalho.animelist.model.Usuario;
import com.trabalho.animelist.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model, java.security.Principal principal) {
        model.addAttribute("listaUsuarios", usuarioRepository.findAll());
        
        Usuario logado = usuarioRepository.findByEmail(principal.getName()).get();
        model.addAttribute("usuarioLogado", logado);
        
        return "admin-usuarios";
    }

    @PostMapping("/deletar-usuario")
    public String deletarUsuario(Long id, java.security.Principal principal) {
        Usuario usuarioLogado = usuarioRepository.findByEmail(principal.getName()).get();

        if (usuarioLogado.getId().equals(id)) {
            return "redirect:/admin/usuarios?erro=autoban";
        }

        usuarioRepository.deleteById(id);
        return "redirect:/admin/usuarios";
    }

    @Autowired
    private com.trabalho.animelist.repository.ReviewRepository reviewRepository;

    @GetMapping("/reviews")
    public String listarReviews(Model model) {
        model.addAttribute("listaReviews", reviewRepository.findAll());
        return "admin-reviews";
    }
}