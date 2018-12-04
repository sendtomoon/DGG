package com.sendtomoon.dgg.server.shiro;

import java.io.Serializable;
import java.util.UUID;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

public class DGGSessionID implements SessionIdGenerator {
	@Override
	public Serializable generateId(Session session) {
		return this.getSession();
	}

	private String getSession() {
		StringBuffer sb = new StringBuffer(UUID.randomUUID().toString().trim().replaceAll("-", ""));
		sb.append("DGG-SESSION-");
		return sb.toString();
	}
}
