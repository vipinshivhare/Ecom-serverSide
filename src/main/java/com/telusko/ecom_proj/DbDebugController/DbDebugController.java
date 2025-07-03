package com.telusko.ecom_proj.DbDebugController; // 🔁 change this to your actual package

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

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
    
    @GetMapping("/env-check")
    public Map<String, String> checkEnv() {
        Map<String, String> env = new HashMap<>();
        env.put("DB_URL", System.getenv("DB_URL"));
        env.put("DB_USERNAME", System.getenv("DB_USERNAME"));
        env.put("DB_PASSWORD", System.getenv("DB_PASSWORD"));
        return env;
    }

}
