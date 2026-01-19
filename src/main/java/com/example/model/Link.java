package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "link", schema = "public")
public class Link {
    @Id
    @Column(name = "shorter_link", nullable = false)
    private String id;

    @Column(name = "original_link", nullable = false)
    private String link;
}
