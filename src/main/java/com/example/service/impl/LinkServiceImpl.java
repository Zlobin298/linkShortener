package com.example.service.impl;

import com.example.model.Link;
import com.example.repository.LinkRepository;
import com.example.service.LinkService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.application.url}")
    private String url;

    private static String encodeCounterToBase62(String url) {
        StringBuilder sb = new StringBuilder(url + "/link/");
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom rn = new SecureRandom();

        for (int i = 0; i < 9; i++) {
            int index = rn.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

    @Override
    public String generateShortLink(String originalLink) {
        if (isRecordAbsent(originalLink)) return getEncodedLink(originalLink);
        return encodeCounterToBase62(url);
    }

    @Override
    public String fetchOriginalLink(String shorterLink) {
        return repository.findById(url + "/link/" + shorterLink)
                .map(Link::getLink)
                .orElse(null);
    }

    @Override
    public String getEncodedLink(String originalLink) {
        return repository.findByLink(originalLink)
                .map(Link::getShorterLink)
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
            connection.setReadTimeout(7000);
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
