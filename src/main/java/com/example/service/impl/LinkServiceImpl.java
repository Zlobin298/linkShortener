package com.example.service.impl;

import com.example.model.Link;
import com.example.repository.LinkRepository;
import com.example.service.LinkService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final LinkRepository repository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private static String encodeCounterToBase62() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return "http://localhost:8080/link/" + sb;
    }

    @Override
    public String generateShortLink(String originalLink) {
        if (isRecordAbsent(originalLink)) return getEncodedLink(originalLink);
        return encodeCounterToBase62();
    }

    @Override
    public String fetchOriginalLink(String shorterLink) {
        return repository.findById("http://localhost:8080/link/" + shorterLink)
                .map(Link::getLink)
                .orElse(null);
    }

    @Override
    public String getEncodedLink(String originalLink) {
        return repository.findByLink(originalLink)
                .map(Link::getId)
                .orElse(null);
    }

    @Override
    public boolean isRecordAbsent(String originalLink) {
        return repository.existsByLink(originalLink);
    }

    @Override
    public boolean isExistsLink(String originalLink) {
        try {
            URL url = new URL(originalLink);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            int responseCode = connection.getResponseCode();

            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveLink(Link link) {
        if (!isRecordAbsent(link.getLink())) {
            repository.save(link);
        }
    }
}
