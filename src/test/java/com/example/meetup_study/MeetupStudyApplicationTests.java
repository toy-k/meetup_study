package com.example.meetup_study;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


@TestPropertySource(locations = "classpath:application-test.yml")
@SpringBootTest

class MeetupStudyApplicationTests {

	@Test
	void contextLoads() {
	}

}

