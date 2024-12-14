package org.zengyi.springdemo.demos.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.zengyi.springdemo.demos.web.User;

import java.util.List;

@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> selectAll() {
        return jdbcTemplate.query("select * from users",
                (rs, rowNum) -> new User(rs.getString("name"), rs.getInt("age")));
    }
}
