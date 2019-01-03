package com.sendtomoon.dgg.server.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sendtomoon.dgg.server.base.BaseController;
import com.sendtomoon.dgg.server.service.IPInfoService;
import com.sendtomoon.dgg.server.utils.CommonVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/dns")
@Api(value = "动态更新IP地址",tags="DDNS管理")
public class IPInfoController extends BaseController {

	@Autowired
	private IPInfoService iif;

	@ApiOperation(value = "插入IP更新信息", httpMethod = "PUT")
	@RequestMapping(value = "/recviceIPInfo", method = RequestMethod.PUT)
	@ResponseBody
	public CommonVO recviceipinfo(
			@ApiParam(value = "事件名称") @RequestParam(value = "eventName", required = true) String eventName,
			@ApiParam(value = "IP地址") @RequestParam(value = "ipAddr", required = true) String ipAddr,
			@ApiParam(value = "IP来源") @RequestParam(value = "ipSource", required = true) String ipSource,
			@ApiParam(value = "请求设备") @RequestParam(value = "device", required = true) String device) {
		return iif.recviceIpInfo(eventName, ipAddr, ipSource, device);
	}

	@ApiOperation(value = "获取IP更新信息", httpMethod = "GET")
	@RequestMapping(value = "/getIPRenewList", method = RequestMethod.GET)
	@ResponseBody
	public CommonVO getIpRenewList(@ApiParam(value = "ID") @RequestParam(value = "id", required = false) String id,
			@ApiParam(value = "事件名称") @RequestParam(value = "eventName", required = false) String eventName,
			@ApiParam(value = "IP地址") @RequestParam(value = "ipAddr", required = false) String ipAddr,
			@ApiParam(value = "来源IP") @RequestParam(value = "ipSource", required = false) String ipSource,
			@ApiParam(value = "请求设备") @RequestParam(value = "device", required = false) String device,
			@ApiParam(value = "开始日期") @RequestParam(value = "startDate", required = false) Date startDate,
			@ApiParam(value = "结束日期") @RequestParam(value = "endDate", required = false) Date endDate) {
		return iif.getdnslist(id, eventName, ipAddr, ipSource, device, startDate, endDate);
	}

}
