package com.example.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.TreeMap;

@AllArgsConstructor
public class InMemoryLinkDAO {
    @Getter
    @Setter
    private final TreeMap<String, String> LINK;
    
    public void saveLink(String id, String link) {
        LINK.put(id, link);
//        return String.format("Сокращенная ссылка: %s\nОбычная ссылка: %s", id, link);
    }

    public void deleetLink(String id) {
        if (LINK.containsKey(id)) {
            LINK.remove(id);
        } else {
            System.out.println("Ключь не найден");
        }
    }
}
