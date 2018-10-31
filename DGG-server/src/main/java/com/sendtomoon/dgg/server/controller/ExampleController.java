package com.sendtomoon.dgg.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sendtomoon.dgg.server.service.impl.ExampleServiceImpl;

@Controller(value = "exampleService")
public class ExampleController {

	@Autowired
	private ExampleServiceImpl server;

	@RequestMapping(value = "/ex1")
	@ResponseBody
	public String ex1(@RequestParam(value = "name", required = true) String name) {
		return "name:" + name;
	}

	@RequestMapping(value = "/ex2")
	@ResponseBody
	public String ex2() {
		return server.insertId();
	}

}
