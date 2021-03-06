package com.sendtomoon.dgg.server.dto;

import java.util.Date;

import com.sendtomoon.dgg.server.base.BaseDTO;

public class IPInfoDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6424426686559817007L;

	private String id;
	private String eventName;
	private String ipAddr;
	private String ipSource;
	private String device;
	private Date createdDate;
	private Date startDate;
	private Date endDate;
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getIpSource() {
		return ipSource;
	}

	public void setIpSource(String ipSource) {
		this.ipSource = ipSource;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
