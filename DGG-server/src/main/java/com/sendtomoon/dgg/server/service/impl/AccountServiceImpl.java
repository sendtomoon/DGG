package com.sendtomoon.dgg.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sendtomoon.dgg.server.base.BaseService;
import com.sendtomoon.dgg.server.dao.mainsourcedao.UserAuthDAO;
import com.sendtomoon.dgg.server.dto.auth.AccountDTO;
import com.sendtomoon.dgg.server.service.AccountService;

@Service
public class AccountServiceImpl extends BaseService implements AccountService {

	@Autowired
	private UserAuthDAO uad;

	private void createUser(AccountDTO account) throws Exception {
		String userName = account.getUserName();
		String loginName = account.getLoginName();
		if (uad.existUser(account) > 0) {
			throw new Exception("用户已存在");
		}
//		
	}

}
