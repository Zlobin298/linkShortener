package com.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Link {
    private String id;
    private String link;
}
