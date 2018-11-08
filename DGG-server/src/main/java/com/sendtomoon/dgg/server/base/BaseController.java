package com.sendtomoon.dgg.server.base;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public class BaseController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@InitBinder
	protected void initDateBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(Date.class, new PropertiesEditor() {

			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if (StringUtils.isNotEmpty(text)) {
					setValue(new Date(Long.valueOf(text)));
				}
			}

			@Override
			public String getAsText() {
				Date date = (Date) getValue();
				return String.valueOf(date.getTime());
			}

		});
	}
}
