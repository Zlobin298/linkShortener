package com.example.linkShortener;

import com.example.model.Link;
import com.example.repository.LinkRepository;
import com.example.service.impl.LinkServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class LinkShortenerApplicationTests {
    @Mock
    LinkRepository repository;

    @InjectMocks
    LinkServiceImpl service;


    @Test
    @DisplayName("Код должен иметь корректную длину и не быть пустым")
    void shouldGenerateCodeWithCorrectLength() {
        String shorterLink = service.generateShortLink("https://goole.com");

        assertNotNull(shorterLink, "Код не должен быть null");
        assertEquals(36, shorterLink.length(), "Длина кода должна быть 6 символов");
    }

    @Test
    @DisplayName("Код должен состоять только из разрешенных символов (латиница и цифры)")
    void shouldContainOnlyAllowedCharacters() {
        String shorterLink = service.generateShortLink("https://goole.com");
        String link = shorterLink.substring(shorterLink.length() - 9, shorterLink.length() - 1);

        assertTrue(link.matches("^[a-zA-Z0-9]+$"),
                "Код содержит недопустимые символы: " + shorterLink);
    }

    @Test
    @DisplayName("Генерация 1000 кодов не должна приводить к дубликатам (проверка уникальности)")
    void shouldGenerateUniqueCodes() {
        int count = 1000;
        Set<String> codes = new HashSet<>();

        for (int i = 0; i < count; i++) {
            codes.add(service.generateShortLink("https://goole.com"));
        }

        assertEquals(count, codes.size(), "Обнаружены дубликаты при генерации");
    }

    @RepeatedTest(100)
    @DisplayName("Многократная проверка структуры кода (на случай редких багов)")
    void shouldConsistentlyGenerateValidCodes() {
        String shorterLink = service.generateShortLink("https://goole.com");
        assertFalse(shorterLink.contains(" "), "Код не должен содержать пробелы");
    }


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
