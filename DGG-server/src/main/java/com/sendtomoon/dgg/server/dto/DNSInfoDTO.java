package com.sendtomoon.dgg.server.dto;

public class DNSInfoDTO extends BaseDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9187207851285525481L;
	private String data;
	private String name;
	private int ttl;
	private String type;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
