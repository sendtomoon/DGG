package com.sendtomoon.dgg.server.service;

import com.sendtomoon.dgg.server.CommonVO;

public interface IPInfoService {

	CommonVO recviceIpInfo(String eventName, String ipAddr, String ipSource, String device);

	CommonVO getdnslist(String name, String ipAddr);

}
