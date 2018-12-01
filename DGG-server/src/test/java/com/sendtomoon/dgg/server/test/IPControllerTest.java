package com.sendtomoon.dgg.server.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.sendtomoon.dgg.server.controller.IPInfoController;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "classpath:application.yml" })
@WebAppConfiguration
@WebMvcTest(IPInfoController.class)
public class IPControllerTest {
	@Autowired
	private MockMvc mvc;

	@Test
	public void test1() throws Exception {
		mvc.perform(post());
		
		this.mvc.perform(get("/sboot/vehicle").accept(MediaType.TEXT_PLAIN)).andExpect(status().isOk())
				.andExpect(content().string("Honda Civic"));
	}
}
