package com.boardhub.boardhub;

import com.boardhub.boardhub.support.TestcontainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfig.class) // 여기 중요!
class BoardhubApplicationTests {

	@Test
	void contextLoads() { }
}
