package com.sendtomoon.dgg.server.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sendtomoon.dgg.server.base.BaseService;
import com.sendtomoon.dgg.server.dao.IPInfoDAO;
import com.sendtomoon.dgg.server.dto.DNSInfoDTO;
import com.sendtomoon.dgg.server.dto.IPInfoDTO;
import com.sendtomoon.dgg.server.service.IPInfoService;
import com.sendtomoon.dgg.server.utils.BatchVO;
import com.sendtomoon.dgg.server.utils.CommonVO;
import com.sendtomoon.dgg.server.utils.HttpUtils;
import com.sendtomoon.dgg.server.utils.UUIDUtils;

@Service
public class IPInfoServiceImpl extends BaseService implements IPInfoService {

	@Autowired
	private IPInfoDAO dao;

	@Value("${godaddy.sso-key}")
	private String ssoKey;

	@Value("${godaddy.base-url}")
	private String url;

	@Override
	public CommonVO recviceIpInfo(String eventName, String ipAddr, String ipSource, String device) {
		IPInfoDTO dto = new IPInfoDTO();
		dto.setId(UUIDUtils.getUUID());
		dto.setDevice(device);
		dto.setEventName(eventName);
		dto.setIpAddr(ipAddr);
		dto.setIpSource(ipSource);
		dao.insertIPInfo(dto);
		return new CommonVO("Success");
	}

	@Override
	public CommonVO getdnslist(String name, String ipAddr) {
		BatchVO<DNSInfoDTO> vo = new BatchVO<DNSInfoDTO>();
		String json = null;
		try {
			json = HttpUtils.invokeHttpGet(url, null, this.setHeaders());
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<DNSInfoDTO> list = JSONArray.parseArray(json, DNSInfoDTO.class);
		if (StringUtils.isNotEmpty(name) || StringUtils.isNotEmpty(ipAddr)) {
			list = this.selectList(list, name, ipAddr);
		}
		vo.setDatas(list);
		vo.setTotal(list.size());
		return vo;
	}

	private List<DNSInfoDTO> selectList(List<DNSInfoDTO> list, String name, String ipAddr) {
		List<DNSInfoDTO> respList = new ArrayList<DNSInfoDTO>();
		for (DNSInfoDTO dto : list) {
			if ((null != dto.getName() && null != dto.getData())) {
				if (name.equals(dto.getName()) || ipAddr.equals(dto.getData())) {
					respList.add(dto);
				}
			}
		}
		return respList;
	}

	private Map<String, String> setHeaders() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Authorization", ssoKey);
		map.put("Content-Type", "application/json");
		return map;
	}

	@Override
	public CommonVO getdnslist(String id, String eventName, String ipAddr, String ipSource, String device,
			Date startDate, Date endDate) {
		BatchVO<IPInfoDTO> vo = new BatchVO<IPInfoDTO>();
		IPInfoDTO dto = new IPInfoDTO();
		dto.setId(id);
		dto.setDevice(device);
		dto.setEventName(eventName);
		dto.setIpAddr(ipAddr);
		dto.setIpSource(ipSource);
		dto.setStartDate(startDate);
		dto.setEndDate(endDate);
		List<IPInfoDTO> list = dao.getIpRenewList(dto);
		vo.setDatas(list);
		vo.setTotal(list.size());
		return vo;
	}

	@Override
	public CommonVO renewdns(String dns, String name, String ipAddr) {
		ipAddr = this.cutSpace(ipAddr);
		if (!this.isIP(ipAddr)) {
			return new CommonVO("", "IP地址格式错误");
		}
		final String url = this.url + "/" + dns + "/records/A/" + name;
		String json = "[{\"data\":\"" + ipAddr + "\"}]";
		JSONObject result = null;
		try {
			logger.info("renewdns-param:" + url + ";" + json);
			result = HttpUtils.putInvokeHttp(url, null, json, this.setHeaders());
			logger.info("renewdns-result:" + result.toJSONString());
		} catch (IOException e) {
			logger.error("renewdns-request-error:" + e);
			return new CommonVO("", "更新失败");
		}
		return new CommonVO("更新成功");
	}

	/**
	 * 自己写一个不用正则的IP地址校验方法
	 * 
	 * @param ip
	 * @return
	 */
	private boolean isIP(String ip) {
		try {
			String[] iparr = ip.split("\\.");
			if (iparr.length != 4) {
				return false;
			}
			for (String ipPart : iparr) {
				int i = Integer.valueOf(ipPart);
				if (0 > i && i > 255) {
					return false;
				}
			}
		} catch (Exception e) {
			logger.error("isIP-error:", e);
			return false;
		}
		return true;
	}

	private String cutSpace(String ip) {
		return ip.replace(" ", "");
	}

	@Override
	public CommonVO getdnsname(String dns, String name) {
		final String url = this.url + "/" + dns + "/records/A/" + name;
		String result = null;
		try {
			logger.info("renewdns-param:" + url);
			result = HttpUtils.invokeHttpGet(url, null, this.setHeaders());
			logger.info("renewdns-result:" + result);
		} catch (IOException e) {
			logger.error("renewdns-request-error:" + e);
			return new CommonVO("", "获取失败");
		}
		BatchVO<DNSInfoDTO> respVO = new BatchVO<DNSInfoDTO>();
		respVO.setDatas(JSON.parseArray(result, DNSInfoDTO.class));
		respVO.setTotal(respVO.getDatas().size());
		return respVO;
	}

}
