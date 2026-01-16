package com.example.repository;

import com.example.model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class InMemoryLinkDAO {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryLinkDAO.class);
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/publicDB";
    private static final String USER = "postgres";
    private static final String PASS = "1234";

    public boolean isRecordAbsent(String original_link) {
        String sql = "SELECT EXISTS(SELECT 1 FROM link WHERE original_link = ?);";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, original_link);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getEncodedLink(String original_link) {
        String sql = "SELECT shorter_link FROM link WHERE original_link = ?;";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, original_link);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("shorter_link");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void saveLink(Link link) {
        String shorter_link = link.getId();
        String original_link = link.getLink();

        String sql = "INSERT INTO link (shorter_link, original_link) VALUES (?, ?);";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (!isRecordAbsent(original_link)) {
                pstmt.setString(1, shorter_link);
                pstmt.setString(2, original_link);

                int rowsAffected = pstmt.executeUpdate();
                logger.info("Rows affected: {}", rowsAffected);
            }
        } catch (SQLException e) {
            logger.error("Error saving link: ", e);
        }
    }

    public String fetchOriginalLink(String sorterLink) {
        String sql = "SELECT original_link FROM link WHERE link.shorter_link = ?;";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, "http://localhost:8080/" + sorterLink);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("original_link");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}