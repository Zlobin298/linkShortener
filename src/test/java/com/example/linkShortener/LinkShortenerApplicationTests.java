package com.example.linkShortener;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class LinkShortenerApplicationTests {
    @Mock
    LinkRepository repository;

    @InjectMocks
    LinkServiceImpl service;

    @Value("${spring.application.url}")
    String url;

    @Test
    @DisplayName("Сокращенная ссылка не быть пустой")
    void generateShortLinkNotNull() {
        String shorterLink = service.generateShortLink("https://google.com");

        assertNotNull(shorterLink, "Сокращенная ссылка не должна быть null");
    }

    @Test
    @DisplayName("Сокращенная ссылка должна состоять только из разрешенных символов (латиница и цифры)")
    void generateShortLinkOnlyAllowedCharacters() {
        String shorterLink = service.generateShortLink("https://goole.com");
        String link = shorterLink.substring(shorterLink.length() - 9, shorterLink.length() - 1);

        assertTrue(link.matches("^[a-zA-Z0-9]+$"),
                "Сокращенная ссылка содержит недопустимые символы: " + shorterLink);
    }

    @Test
    @DisplayName("Генерация 1000 сокращенных ссылок не должна приводить к дубликатам (проверка уникальности)")
    void generateShortLinkUniqueCodes() {
        int count = 1000;
        Set<String> codes = new HashSet<>();

        for (int i = 0; i < count; i++) {
            codes.add(service.generateShortLink("https://goole.com"));
        }

        assertEquals(count, codes.size(), "Обнаружены дубликаты при генерации");
    }

    @RepeatedTest(100)
    @DisplayName("Многократная проверка структуры сокращенных ссылок (на случай редких багов)")
    void generateShortLinkValidCodes() {
        String shorterLink = service.generateShortLink("https://goole.com");

        assertFalse(shorterLink.contains(" "), "Сокращенная ссылка не должна содержать пробелы");
    }

    @Test
    @DisplayName("Должен вернуть null, если ссылка не найдена в репозитории")
    void getEncodedLinkNullWhenNotFound() {
        String shortCode = "jjj";
        String fullId = url + "/link/" + shortCode;

        when(repository.findById(fullId)).thenReturn(Optional.empty());
        assertNull(service.fetchOriginalLink(shortCode), "Метод должен возвращать null, если запись не найдена");
    }

    @Test
    @DisplayName("Должен возвращать null, если ссылка не найдена")
    void fetchOriginalLinkNullWhenNotFound() {
        String shortCode = "nonexistent";
        String fullKey = url + "/link/" + shortCode;

        when(repository.findById(fullKey)).thenReturn(Optional.empty());
        assertNull(service.fetchOriginalLink(shortCode), "Если ссылки нет в базе, должен вернуться null");
    }

    @Test
    @DisplayName("Должен возвращать true, если ссылка уже есть в базе")
    void isRecordAbsentTrueWhenLinkExists() {
        String originalLink = "https://google.com";

        when(repository.existsByLink(originalLink)).thenReturn(true);
        assertTrue(service.isRecordAbsent(originalLink), "Метод должен вернуть true, так как репозиторий подтвердил наличие");
        verify(repository, times(1)).existsByLink(originalLink);
    }

    @Test
    @DisplayName("Должен возвращать false, если ссылки нет в базе")
    void isRecordAbsentFalseWhenLinkDoesNotExist() {
        String originalLink = "https://google.com";

        when(repository.existsByLink(originalLink)).thenReturn(false);
        assertFalse(service.isRecordAbsent(originalLink), "Метод должен вернуть false, так как ссылки нет в репозитории");
        verify(repository, times(1)).existsByLink(originalLink);
    }

    @Test
    @DisplayName("Должен проверять то что ссылка существует")
    void isExistsLinkTrue() {
        String link = "https://ya.ru/";

        assertTrue(service.isExistsLink(link), "Метод должен вернуть true, так как ссылка существует");
    }

    @Test
    @DisplayName("Должен проверять то что ссылка не существует")
    void isExistsLinkFalse() {
        String link = "https://hhdk";

        assertFalse(service.isExistsLink(link), "Метод должен вернуть false, так как ссылки не существует");
    }


    @Test
    @DisplayName("Должен сохранять переданное значения")
    void save() {
        String link = "https://google.com";

        when(repository.save(any(Link.class))).thenAnswer(invocation -> invocation.getArgument(0));
        service.saveLink(new Link(service.generateShortLink(link), link));
        Mockito.verify(repository, Mockito.times(1)).save(any(Link.class));
    }
}
