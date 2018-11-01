package com.sendtomoon.dgg.server.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sendtomoon.dgg.server.CommonVO;

@Controller("ip")
public class IPInfoController {

	@RequestMapping(value = "recviceipinfo")
	@ResponseBody
	public CommonVO recviceipinfo(@RequestParam(value = "eventName", required = true) String eventName,
			@RequestParam(value = "ipAddr", required = true) String ipAddr,
			@RequestParam(value = "ipSource", required = true) String ipSource) {
		
	}

}
