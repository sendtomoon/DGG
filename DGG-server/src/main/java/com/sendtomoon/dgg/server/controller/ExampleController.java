package com.sendtomoon.dgg.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sendtomoon.dgg.server.base.BaseController;
import com.sendtomoon.dgg.server.service.impl.ExampleServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller(value = "exampleService")
public class ExampleController extends BaseController {

	@Autowired
	private ExampleServiceImpl server;

	@ApiOperation(value = "测试1", httpMethod = "GET")
	@RequestMapping(value = "/ex1", method = RequestMethod.GET)
	@ResponseBody
	public String ex1(@ApiParam(value = "name") @RequestParam(value = "name", required = true) String name) {
		return "name:" + name;
	}

	@ApiOperation(value = "测试2", httpMethod = "GET")
	@RequestMapping(value = "/ex2", method = RequestMethod.GET)
	@ResponseBody
	public String ex2() {
		return server.insertId();
	}

}
