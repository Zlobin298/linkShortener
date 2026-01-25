package com.example.linkShortener;

import com.example.model.Link;
import com.example.repository.LinkRepository;
import com.example.service.impl.LinkServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class LinkShortenerApplicationTests {
    @Mock
    LinkRepository repository;

    @InjectMocks
    LinkServiceImpl service;

    @Test
    @DisplayName("Метод должен сохранять переданное значения")
    void save() {
        String link = "https://google.com";

        String shorterLink = service.generateShortLink(link);

        Mockito.when(repository.save(any(Link.class))).thenAnswer(invocation -> invocation.getArgument(0));
        service.saveLink(new Link(shorterLink, link));
        Mockito.verify(repository, Mockito.times(1)).save(any(Link.class));
    }
}
