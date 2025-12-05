package com.trabalho.animelist.service;

import com.trabalho.animelist.dto.JikanResponse;
import com.trabalho.animelist.dto.JikanSingleResponse;
import com.trabalho.animelist.model.Anime;
import com.trabalho.animelist.model.Biblioteca;
import com.trabalho.animelist.model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class AnimeService {

    public List<JikanResponse.AnimeData> buscarAnimes(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String url = "https://api.jikan.moe/v4/anime?q=" + nome + "&limit=10";

        RestTemplate restTemplate = new RestTemplate();
        
        try {
            JikanResponse resposta = restTemplate.getForObject(url, JikanResponse.class);
            
            if (resposta != null && resposta.getData() != null) {
                return resposta.getData();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar na API: " + e.getMessage());
        }

        return Collections.emptyList();
    }

    @Autowired
    private com.trabalho.animelist.repository.AnimeRepository animeRepository;
    
    @Autowired
    private com.trabalho.animelist.repository.UsuarioRepository usuarioRepository;

    @Autowired
    private com.trabalho.animelist.repository.BibliotecaRepository bibliotecaRepository;

    public void salvarAnimeNaLista(String emailUsuario, Long apiId, String titulo, String imagemUrl, Integer totalEpisodios) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Anime anime = animeRepository.findByApiId(apiId)
                .orElseGet(() -> {
                    Anime novoAnime = new Anime();
                    novoAnime.setApiId(apiId);
                    novoAnime.setTitulo(titulo);
                    novoAnime.setImagemUrl(imagemUrl);
                    return animeRepository.save(novoAnime);
                });
        if (bibliotecaRepository.findByUsuarioAndAnime(usuario, anime).isPresent()) {
            return;
        }

        Biblioteca item = new Biblioteca();
        item.setUsuario(usuario);
        item.setAnime(anime);
        item.setStatus("ASSISTINDO");
        item.setEpisodiosAssistidos(0);
        bibliotecaRepository.save(item);
    }

    public JikanResponse.AnimeData buscarPorId(Long apiId) {
        String url = "https://api.jikan.moe/v4/anime/" + apiId;
        RestTemplate restTemplate = new RestTemplate();
        
        try {
            JikanSingleResponse resposta = restTemplate.getForObject(url, JikanSingleResponse.class);
            if (resposta != null) {
                return resposta.getData();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar detalhes: " + e.getMessage());
        }
        return null;
    }
    
    public List<JikanResponse.AnimeData> buscarTopAnimes() {
        String url = "https://api.jikan.moe/v4/top/anime?filter=bypopularity&limit=10";
        RestTemplate restTemplate = new RestTemplate();
        
        try {
            JikanResponse resposta = restTemplate.getForObject(url, JikanResponse.class);
            if (resposta != null && resposta.getData() != null) {
                return resposta.getData();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar top animes: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}