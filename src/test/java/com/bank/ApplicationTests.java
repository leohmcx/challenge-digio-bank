package com.bank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ApplicationTests {

	@Test
	void assertConfig() {
		assertDoesNotThrow(() -> Application.main(new String[]{ "--spring.profiles.active=test" }));
	}

}
