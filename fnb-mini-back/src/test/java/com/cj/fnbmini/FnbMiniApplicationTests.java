package com.cj.fnbmini;

import com.cj.fnbmini.auth.AuthMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class FnbMiniApplicationTests {

	@MockitoBean
	AuthMapper authMapper;

	@Test
	void contextLoads() {
	}

}
