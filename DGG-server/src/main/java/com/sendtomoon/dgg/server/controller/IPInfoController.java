package com.sendtomoon.dgg.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sendtomoon.dgg.server.CommonVO;
import com.sendtomoon.dgg.server.service.IPInfoService;

@Controller
@RequestMapping(value = "/ip")
public class IPInfoController {

	@Autowired
	private IPInfoService iif;

	@RequestMapping(value = "/recviceipinfo")
	@ResponseBody
	public CommonVO recviceipinfo(@RequestParam(value = "eventName", required = true) String eventName,
			@RequestParam(value = "ipAddr", required = true) String ipAddr,
			@RequestParam(value = "ipSource", required = true) String ipSource,
			@RequestParam(value = "device", required = true) String device) {
		return iif.recviceIpInfo(eventName, ipAddr, ipSource, device);
	}
	
	

}
