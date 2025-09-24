package com.example.service;

import com.example.model.Link;

public interface ILinkService {
    Link saveLink(Link link);
    void deleetLink(String id);
}
