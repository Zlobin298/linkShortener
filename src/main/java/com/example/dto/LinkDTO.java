package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LinkDTO {
    @NotBlank(message = "Поле не должно быть пустым")
    private String link;
}

