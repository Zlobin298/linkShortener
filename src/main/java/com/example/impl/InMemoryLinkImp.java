package com.example.impl;

import com.example.model.Link;
import com.example.repository.InMemoryLinkDAO;
import com.example.service.ILinkService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InMemoryLinkImp implements ILinkService {
    private final InMemoryLinkDAO repository;

    @Override
    public void saveLink(Link link) {
        repository.saveLink(link);
    }

    @Override
    public void deleteLink(String id) {
        repository.deleteLink(id);
    }
}
