package com.sendtomoon.dgg.server.service.impl;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController  {

	
	
	@RequestMapping("/")
	@ResponseBody
	public String index(Model model, HttpServletResponse response) {
	    return "/index";
	}
}
