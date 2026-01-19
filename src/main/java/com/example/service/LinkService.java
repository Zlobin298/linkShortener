package com.example.service;

import com.example.model.Link;

public interface LinkService {
    void saveLink(Link link);
    String fetchOriginalLink(String sorterLink);
    boolean isRecordAbsent(String original_link);
    String getEncodedLink(String original_link);
}
