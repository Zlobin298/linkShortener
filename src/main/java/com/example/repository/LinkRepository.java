package com.example.repository;


import com.example.model.Link;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, String> {
    boolean existsByLink(String link);
    Optional<Link> findByLink(String link);
}