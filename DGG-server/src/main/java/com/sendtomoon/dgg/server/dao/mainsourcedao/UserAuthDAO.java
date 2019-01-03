package com.sendtomoon.dgg.server.dao.mainsourcedao;

import org.apache.ibatis.annotations.Param;

import com.sendtomoon.dgg.server.dto.auth.AccountDTO;

public interface UserAuthDAO {

	AccountDTO getUserInfoForLogin(@Param(value = "userName") String userName);

	void insertNewUser(AccountDTO dto);
	
	int existUser(AccountDTO dto);

}
