package com.sendtomoon.dgg.server.dao.mainsourcedao;

import org.springframework.stereotype.Repository;

import com.sendtomoon.dgg.server.dto.ExampleDTO;

@Repository
public interface ExampleDAO {
	void insertId(ExampleDTO dto);
}
