package com.telusko.ecom_proj.DbDebugController; // 🔁 change this to your actual package

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
public class DbDebugController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/db")
    public String showDbUrl() {
        try (Connection conn = dataSource.getConnection()) {
            return "✅ Connected to: " + conn.getMetaData().getURL();
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }
}
