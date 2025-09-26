package com.example.repository;

import com.example.model.Link;
import org.springframework.stereotype.Repository;

import java.util.TreeMap;

@Repository
public class InMemoryLinkDAO {
    private final TreeMap<String, String> LINK = new TreeMap<>();
    
    public void saveLink(Link link) {
        LINK.put(link.getId(), link.getLink());
    }

    public void deleteLink(String id) {
        if (LINK.containsKey(id)) {
            LINK.remove(id);
        } else {
            System.out.println("Ключ не найден");
        }
    }
}
