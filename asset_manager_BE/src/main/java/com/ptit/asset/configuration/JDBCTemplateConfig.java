package com.ptit.asset.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Configuration
public class JDBCTemplateConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
    }

    @Value("${spring.schema-name}")
    private String schemaName;

        @Bean(name = "sqlDataSource")
        @Primary
        public DataSource sqlDataSource()
        {
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.url("jdbc:sqlserver://localhost;databaseName="+schemaName);
            dataSourceBuilder.username("sa");
            dataSourceBuilder.password("NguyenDuc@163");
            return dataSourceBuilder.build();
//            DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
//
//            String dbUrl = System.getenv("SPRING_DATASOURCE_URL");
//            String dbUsername = System.getenv("SPRING_DATASOURCE_USERNAME");
//            String dbPassword = System.getenv("SPRING_DATASOURCE_PASSWORD");
//
//            if (dbUrl == null || dbUsername == null || dbPassword == null) {
//                throw new RuntimeException("Database configuration is missing! Check environment variables.");
//            }
//
//            dataSourceBuilder.url(dbUrl);
//            dataSourceBuilder.username(dbUsername);
//            dataSourceBuilder.password(dbPassword);
//            return dataSourceBuilder.build();
        }

//    @Bean(name = "mySqlDataSource")
//    public DataSource mySqlDataSource()
//    {
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.url("jdbc:mysql://localhost/testdb");
//        dataSourceBuilder.username("root");
//        dataSourceBuilder.password("Khanh123456");
//        return dataSourceBuilder.build();
//    }
}
