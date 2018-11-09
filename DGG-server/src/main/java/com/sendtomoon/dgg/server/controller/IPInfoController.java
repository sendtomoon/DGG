package com.sendtomoon.dgg.server.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sendtomoon.dgg.server.base.BaseController;
import com.sendtomoon.dgg.server.service.IPInfoService;
import com.sendtomoon.dgg.server.utils.CommonVO;

@Controller
@RequestMapping(value = "/ip")
public class IPInfoController extends BaseController {

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

	@RequestMapping(value = "/getdnslist")
	@ResponseBody
	public CommonVO getdnslist(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "ipAddr", required = false) String ipAddr) {
		return iif.getdnslist(name, ipAddr);
	}

	@RequestMapping(value = "/getiprenewlist")
	@ResponseBody
	public CommonVO getIpRenewList(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "eventName", required = false) String eventName,
			@RequestParam(value = "ipAddr", required = false) String ipAddr,
			@RequestParam(value = "ipSource", required = false) String ipSource,
			@RequestParam(value = "device", required = false) String device,
			@RequestParam(value = "startDate", required = false) Date startDate,
			@RequestParam(value = "endDate", required = false) Date endDate) {
		return iif.getdnslist(id, eventName, ipAddr, ipSource, device, startDate, endDate);
	}

	@RequestMapping(value = "/renewdns")
	@ResponseBody
	public CommonVO renewdns(@RequestParam(value = "dns", required = true) String dns,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "ipAddr", required = true) String ipAddr) {
		return iif.renewdns(dns, name, ipAddr);
	}

}
