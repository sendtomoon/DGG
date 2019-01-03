package com.sendtomoon.dgg.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sendtomoon.dgg.server.service.IPInfoService;
import com.sendtomoon.dgg.server.utils.CommonVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@Api(value = "GoDaddy域名管理")
@RequestMapping(value = "/godaddy")
public class GoDaddyController {

	@Autowired
	private IPInfoService iif;

	@ApiOperation(value = "更新GoDaddy IP", httpMethod = "PUT")
	@RequestMapping(value = "/renewDNS", method = RequestMethod.PUT)
	@ResponseBody
	public CommonVO renewdns(@ApiParam(value = "dns域") @RequestParam(value = "domain", required = true) String domain,
			@ApiParam(value = "dns名称") @RequestParam(value = "name", required = true) String name,
			@ApiParam(value = "IP地址") @RequestParam(value = "ipAddr", required = true) String ipAddr) {
		return iif.renewdns(domain, name, ipAddr);
	}

	@ApiOperation(value = "获取GoDaddy IP信息", httpMethod = "GET")
	@RequestMapping(value = "/getDNSName", method = RequestMethod.GET)
	@ResponseBody
	public CommonVO getdnsname(@ApiParam(value = "dns域") @RequestParam(value = "domain", required = true) String domain,
			@ApiParam(value = "dns名称") @RequestParam(value = "name", required = true) String name) {
		return iif.getdnsname(domain, name);
	}

	@ApiOperation(value = "获取GoDaddy DNS IP信息", httpMethod = "GET")
	@RequestMapping(value = "/getdnslist", method = RequestMethod.GET)
	@ResponseBody
	public CommonVO getdnslist(@ApiParam(value = "dns名称") @RequestParam(value = "name", required = true) String name) {
		return iif.getdnslist(name);
	}
}
