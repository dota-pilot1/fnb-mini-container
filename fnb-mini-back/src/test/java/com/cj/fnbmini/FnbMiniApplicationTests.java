package com.cj.fnbmini;

import com.cj.fnbmini.auth.AuthMapper;
import com.cj.fnbmini.brand.BrandMapper;
import com.cj.fnbmini.settlement.SettlementMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class FnbMiniApplicationTests {

	@MockitoBean
	AuthMapper authMapper;

	@MockitoBean
	BrandMapper brandMapper;

	@MockitoBean
	SettlementMapper settlementMapper;

	@Test
	void contextLoads() {
	}

}
