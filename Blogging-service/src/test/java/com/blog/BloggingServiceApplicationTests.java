package com.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BloggingServiceApplicationTests {



	@Test
	void contextLoads()  {
		String status = "I";
		if(!status.getClass().getPackageName().equalsIgnoreCase("java.lang")){
			System.out.println("Inside the if condition....!");
		}

	}



}
