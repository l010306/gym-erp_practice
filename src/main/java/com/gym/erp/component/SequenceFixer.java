package com.gym.erp.component;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 修复 PostgreSQL 序列问题
 * 这个组件会在应用启动时自动重置序列
 */
@Component
public class SequenceFixer {

    private final JdbcTemplate jdbcTemplate;

    public SequenceFixer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void fixSequences() {
        try {
            // 修复 courses 表的序列
            jdbcTemplate.execute(
                    "SELECT setval('courses_id_seq', COALESCE((SELECT MAX(id) FROM courses), 1))");
            System.out.println("✅ Courses sequence fixed successfully!");
        } catch (Exception e) {
            System.err.println("⚠️  Failed to fix courses sequence: " + e.getMessage());
        }
    }
}
