package com.sendtomoon.dgg.server.utils;

import org.apache.commons.lang3.StringUtils;

public class StrUtil {

	public static boolean isBlank(final CharSequence cs) {
		return StringUtils.isBlank(cs);
	}

	public static boolean isNotBlank(final CharSequence cs) {
		return !StrUtil.isBlank(cs);
	}

	public static boolean isEmpty(final CharSequence cs) {
		return StringUtils.isEmpty(cs);
	}

	public static boolean isNotEmpty(final CharSequence cs) {
		return !StrUtil.isEmpty(cs);
	}
}
