package com.example.service.impl;

import com.example.model.Link;
import com.example.repository.LinkRepository;
import com.example.service.LinkService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final LinkRepository repository;

    @Override
    public void saveLink(Link link) {
        if (!isRecordAbsent(link.getLink())) {
            repository.save(link);
        }
    }

    @Override
    public String fetchOriginalLink(String shorterLink) {
        return repository.findById("http://localhost:8080/link/" + shorterLink)
                .map(Link::getLink)
                .orElse(null);
    }

    @Override
    public boolean isRecordAbsent(String originalLink) {
        return repository.existsByLink(originalLink);
    }

    @Override
    public String getEncodedLink(String originalLink) {
        return repository.findByLink(originalLink)
                .map(Link::getId)
                .orElse(null);
    }
}
