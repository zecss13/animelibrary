package com.trabalho.animelist.controller;

import com.trabalho.animelist.dto.JikanResponse;
import com.trabalho.animelist.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BuscaController {

    @Autowired
    private AnimeService animeService;

    @GetMapping("/buscar")
    public String paginaBusca(@RequestParam(name = "anime", required = false) String nome, Model model) {
        
        if (nome != null) {
            List<JikanResponse.AnimeData> resultados = animeService.buscarAnimes(nome);
            model.addAttribute("resultados", resultados);
            model.addAttribute("termoBusca", nome);
        }
        
        return "buscar";
    }
}