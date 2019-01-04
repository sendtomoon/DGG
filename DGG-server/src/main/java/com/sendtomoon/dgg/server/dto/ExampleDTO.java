package com.sendtomoon.dgg.server.dto;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.sendtomoon.dgg.server.base.BaseDTO;

@Alias("ExampleDTO")
public class ExampleDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
