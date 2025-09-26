package com.example.service;

import com.example.repository.InMemoryLinkDAO;
import com.example.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkShortenerService implements ILinkService {
    @Autowired
    private InMemoryLinkDAO memoryLinkDAO;

    @Override
    public void saveLink(Link link) {

    }

    @Override
    public void deleteLink(String id) {

    }
}
