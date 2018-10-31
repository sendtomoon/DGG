package com.sendtomoon.dgg.server.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.sendtomoon.dgg.server.dto.ExampleDTO;

@Repository
public interface ExampleDAO {
	void insertId(ExampleDTO dto);
}
