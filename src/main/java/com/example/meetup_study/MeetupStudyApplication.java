package com.example.meetup_study;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@SpringBootApplication
@EnableJpaAuditing
@EnableBatchProcessing
public class MeetupStudyApplication {

	public static void main(String[] args) {

		SpringApplication.run(MeetupStudyApplication.class, args);
	}
}

////현재 연결된 db url 확인 test (docker)
//@SpringBootApplication
//@EnableJpaAuditing
//public class MeetupStudyApplication {
//
//	public static void main(String[] args) {
//		ConfigurableApplicationContext context = SpringApplication.run(MeetupStudyApplication.class, args);
//		DataSource dataSource = context.getBean(DataSource.class);
//		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//		String url = getDataSourceURL(jdbcTemplate);
//		System.out.println("현재 연결된 데이터베이스 URL: " + url);
//		context.close();
//	}
//
//	private static String getDataSourceURL(JdbcTemplate jdbcTemplate) {
//		try {
//			Connection connection = jdbcTemplate.getDataSource().getConnection();
//			DatabaseMetaData metaData = connection.getMetaData();
//			String url = metaData.getURL();
//			connection.close();
//
//			return url;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//}

