package com.example.impl;

import java.util.TreeMap;

public class InMemoryLinkDAO {
    private final TreeMap<String, String> LINK = new TreeMap();
    
    public String saveLink(String id, String link) {
        LINK.put(id, link);
        return String.format("Сокращенная ссылка: %s\nОбычная ссылка: %s", id, link);
    }

    public void deleetLink(String id) {
        if (LINK.containsKey(id)) {
            LINK.remove(id);
        } else {
            System.out.println("Ключь не найден");
        }
    }
}
