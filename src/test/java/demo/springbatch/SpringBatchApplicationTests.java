package demo.springbatch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ConfigurationPropertiesScan({ "env" })
class SpringBatchApplicationTests {

	@Test
	void contextLoads() {
	}

}
