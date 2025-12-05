package com.trabalho.animelist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class JikanResponse {
    private List<AnimeData> data;

    @Data
    public static class AnimeData {
        @JsonProperty("mal_id")
        private Long malId;

        private String title;
        
        private Integer episodes;

        private String status;
        
        private String synopsis;

        private Images images;
    }

    @Data
    public static class Images {
        private Jpg jpg;
    }

    @Data
    public static class Jpg {
        @JsonProperty("image_url")
        private String imageUrl;
    }
}