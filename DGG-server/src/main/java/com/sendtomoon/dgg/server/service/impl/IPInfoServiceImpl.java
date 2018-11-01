package com.sendtomoon.dgg.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sendtomoon.dgg.server.CommonVO;
import com.sendtomoon.dgg.server.UUIDUtils;
import com.sendtomoon.dgg.server.dao.IPInfoDAO;
import com.sendtomoon.dgg.server.dto.IPInfoDTO;
import com.sendtomoon.dgg.server.service.IPInfoService;
@Service
public class IPInfoServiceImpl implements IPInfoService {

	@Autowired
	private IPInfoDAO dao;

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

}
