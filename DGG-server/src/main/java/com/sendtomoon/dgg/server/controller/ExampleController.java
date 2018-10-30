package com.sendtomoon.dgg.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller(value = "example")
public class ExampleController {

	@RequestMapping(value = "/ex1")
	@ResponseBody
	public String ex1(@RequestParam(value = "name", required = true) String name) {
		return "name:" + name;
	}

}
