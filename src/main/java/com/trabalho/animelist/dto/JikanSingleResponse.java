package com.trabalho.animelist.dto;

import com.trabalho.animelist.dto.JikanResponse.AnimeData;
import lombok.Data;

@Data
public class JikanSingleResponse {
    private AnimeData data;
}