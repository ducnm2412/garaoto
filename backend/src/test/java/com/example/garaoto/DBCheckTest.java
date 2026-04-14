package com.example.garaoto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class DBCheckTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void printConstraints() {
        String query = "SELECT pg_get_constraintdef(oid) FROM pg_constraint WHERE conname = 'phieu_sua_chua_trang_thai_check'";
        String def = jdbcTemplate.queryForObject(query, String.class);
        System.out.println("====== CONSTRAINT DEF ======");
        System.out.println(def);
        System.out.println("============================");

        // Also drop it while we're at it to fix the issue:
        jdbcTemplate.execute("ALTER TABLE phieu_sua_chua DROP CONSTRAINT IF EXISTS phieu_sua_chua_trang_thai_check");
        System.out.println("Dropped constraint to prevent further issues.");
    }
}
