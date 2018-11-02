package com.sendtomoon.dgg.server;

import java.util.List;

public class BatchVO<T> extends CommonVO {

	public BatchVO() {
	}

	private long total;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	private List<T> datas;

}
