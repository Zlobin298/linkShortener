package com.example.controller;

import com.example.model.Link;
import com.example.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class LinkController {
    private final LinkService SERVICE;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private String linkId;
    private String originalLink;

    private boolean isExistsLink() {
        try {
            URL url = new URL(originalLink);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            int responseCode = connection.getResponseCode();

            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            System.out.println("Ошибка при проверке ссылки: " + e.getMessage());
        }

        return false;
    }

    private static String encodeCounterToBase62() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return "http://localhost:8080/link/" + sb;
    }

    private String generateShortLink() {
        if (SERVICE.isRecordAbsent(originalLink)) return SERVICE.getEncodedLink(originalLink);
        return encodeCounterToBase62();
    }

    @GetMapping("/home")
    public String showHomePage(Model model) {
        try {
            if (isExistsLink()) {
                model.addAttribute("linkShortener", linkId);
            } else {
                model.addAttribute("linkShortener", "Укажите существующую ссылку");
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }

        return "index";
    }

    @PostMapping("save")
    public String handleShortenRequest(@RequestParam String link) {
        originalLink = link;

        if (isExistsLink()) {
            String generateShortLink = generateShortLink();
            Link myLink = new Link(generateShortLink, link);

            SERVICE.saveLink(myLink);
            linkId = generateShortLink;
        }

        return "redirect:/home";
    }

    @GetMapping("/link/{link}")
    public String transferNewLink(@PathVariable String link) {
        return "redirect:" + SERVICE.fetchOriginalLink(link);
    }
}
