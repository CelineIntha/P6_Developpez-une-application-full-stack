package com.openclassrooms.mddapi.dto.topics;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TopicDto {

    @NotBlank(message = "Le nom du thème est obligatoire")
    @Size(max = 100, message = "Le nom du thème ne peut pas dépasser 100 caractères")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
