package com.sendtomoon.dgg.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sendtomoon.dgg.server.base.BaseController;

@Controller
@RequestMapping(value = "/")
public class ShiroController extends BaseController {

	@RequestMapping(value = "login")
	@ResponseBody
	public ModelAndView login() {
		ModelAndView model = new ModelAndView();
		model.addObject("sdafsdafsad");
		return model;
	}
}
