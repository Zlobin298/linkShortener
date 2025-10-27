package com.example.repository;

import com.example.model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class InMemoryLinkDAO {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryLinkDAO.class);
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/publicDB";
    private static final String USER = "postgres";
    private static final String PASS = "1234";

    public void saveLink(Link link) {
        String shorter_link = link.getId();
        String original_link = link.getLink();

        String sql = "INSERT INTO link (shorter_link, original_link) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shorter_link);
            pstmt.setString(2, original_link);

            int rowsAffected = pstmt.executeUpdate();
            logger.info("Rows affected: {}", rowsAffected);
        } catch (SQLException e) {
            logger.error("Error saving link: ", e);
        }
    }
}