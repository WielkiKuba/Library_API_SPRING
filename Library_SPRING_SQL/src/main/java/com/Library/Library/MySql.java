package com.Library.Library;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class MySql {

    private final JdbcTemplate jdbcTemplate;

    public MySql(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String select(String query,String[] valuesForQuery){
        String response =  jdbcTemplate.queryForObject(query,valuesForQuery,String.class);
        return response;
    }
    public List<Map<String, Object>> select(String query){
        List<Map<String, Object>> list = new ArrayList<>(jdbcTemplate.queryForList(query));
        return list;
    }
    public void update(String query,String[] valuesForQuery){
        jdbcTemplate.update(query,valuesForQuery);
    }
//    public void put(String query,String[] valuesForQuery){
//        jdbcTemplate.update(query,valuesForQuery);
//    }
}
