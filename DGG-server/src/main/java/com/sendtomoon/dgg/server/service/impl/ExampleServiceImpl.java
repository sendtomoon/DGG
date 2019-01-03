package com.sendtomoon.dgg.server.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sendtomoon.dgg.server.dao.mainsourcedao.ExampleDAO;
import com.sendtomoon.dgg.server.dto.ExampleDTO;
import com.sendtomoon.dgg.server.service.ExampleService;

@Service("example")
public class ExampleServiceImpl implements ExampleService {

	@Autowired
	private ExampleDAO dao;

	@Override
	public String insertId() {
		ExampleDTO dto = new ExampleDTO();
		dto.setId(UUID.randomUUID().toString());
		dao.insertId(dto);
		return "success";
	}

}
