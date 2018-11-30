package com.sendtomoon.dgg.server.base;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class BaseDTO implements Serializable, Cloneable, IValidatable {

	private static final long serialVersionUID = 1L;

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return null;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
