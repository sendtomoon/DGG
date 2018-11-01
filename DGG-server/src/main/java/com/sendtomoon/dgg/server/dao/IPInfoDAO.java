package com.sendtomoon.dgg.server.dao;

import org.springframework.stereotype.Repository;

import com.sendtomoon.dgg.server.dto.IPInfoDTO;

@Repository
public interface IPInfoDAO {

	void insertIPInfo(IPInfoDTO dto);
	
}
