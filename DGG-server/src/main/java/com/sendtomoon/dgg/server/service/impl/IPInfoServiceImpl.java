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

import com.sendtomoon.dgg.server.HttpUtils;
import com.alibaba.fastjson.JSONArray;
import com.sendtomoon.dgg.server.BatchVO;
import com.sendtomoon.dgg.server.CommonVO;
import com.sendtomoon.dgg.server.UUIDUtils;
import com.sendtomoon.dgg.server.dao.IPInfoDAO;
import com.sendtomoon.dgg.server.dto.DNSInfoDTO;
import com.sendtomoon.dgg.server.dto.IPInfoDTO;
import com.sendtomoon.dgg.server.service.IPInfoService;

@Service
public class IPInfoServiceImpl implements IPInfoService {

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

}
