package com.sendtomoon.dgg.server.service;

import java.util.Date;

import com.sendtomoon.dgg.server.CommonVO;

public interface IPInfoService {

	CommonVO recviceIpInfo(String eventName, String ipAddr, String ipSource, String device);

	CommonVO getdnslist(String name, String ipAddr);

	CommonVO getdnslist(String id, String eventName, String ipAddr, String ipSource, String device, Date startDate,
			Date endDate);

}
