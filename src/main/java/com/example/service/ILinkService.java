package com.example.service;

import com.example.model.Link;

public interface ILinkService {
    void saveLink(Link link);
    String fetchOriginalLink(String sorterLink);
}
