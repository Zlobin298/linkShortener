package com.example.service;

import com.example.model.Link;

public interface LinkService {
    String fetchOriginalLink(String sorterLink);
    String getEncodedLink(String originalLink);
    String generateShortLink(String originalLink);
    boolean isRecordAbsent(String originalLink);
    boolean isExistsLink(String originalLink);
    void saveLink(Link link);
}
