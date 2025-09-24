package com.example.controller;

import com.example.impl.InMemoryLinkDAO;
import com.example.model.Link;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/generatorLink")
public class ControllerGeneratorLink {
    private InMemoryLinkDAO memoryLink = new InMemoryLinkDAO();
    private static final String BASE62_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static long counter = 0;

    private static String encodeCounterToBase62(long id) {
        if (id == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();

        while (id > 0) {
            id = id / 62;
            sb.append(BASE62_CHARS.charAt((int)(id % 62)));
        }

        return sb.reverse().toString();
    }

    public static String generateShortLink() {
        synchronized (ControllerGeneratorLink.class) {
            counter++;
            return encodeCounterToBase62(counter);
        }
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("linkShorter", generateShortLink());

        return "markup";
    }

    @PostMapping("save_link")
    public String saveLink(@RequestBody Link link) {
        memoryLink.saveLink(link.getId(), link.getLink());

        return "Данные успешно записаны";
    }
}
