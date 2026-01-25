package com.example.service;

import com.example.model.Link;

public interface LinkService {
    String fetchOriginalLink(String sorterLink);
    String getEncodedLink(String original_link);
    String generateShortLink(String originalLink);
    boolean isRecordAbsent(String original_link);
    boolean isExistsLink(String originalLink);
    void saveLink(Link link);
}
