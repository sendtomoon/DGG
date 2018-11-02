package com.sendtomoon.dgg.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpUtils {
	public static JSONObject invokeHttp(String url, Map<String, String> params, String postData,
			Map<String, String> headers) throws IOException {
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httpget
		if (null != params && 0 != params.size()) {
			Set<String> keys = params.keySet();
			String temp = "";
			for (String key : keys) {
				if (temp.length() == 0) {
					temp = "?" + key + "=" + params.get(key);
				} else {
					temp = temp + "&" + key + "=" + params.get(key);
				}
			}
			url = url + temp;
		}
		// 创建httppost
		HttpPost httpPost = new HttpPost(url);
		if (null != headers && 0 != headers.size()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}
		}

		if (StringUtils.isNotBlank(postData)) {
			HttpEntity entity = new ByteArrayEntity(postData.getBytes("UTF-8"));
			httpPost.setEntity(entity);
		}

		try {
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String rspBody = EntityUtils.toString(entity, "UTF-8");
					return JSONObject.parseObject(rspBody);
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			throw e;
		} catch (UnsupportedEncodingException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				throw e;
			}
		}
		return null;

	}

	public static String invokeHttpGet(String url, Map<String, String> params, Map<String, String> headers)
			throws IOException {
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httpget
		if (null != params && 0 != params.size()) {
			Set<String> keys = params.keySet();
			String temp = "";
			for (String key : keys) {
				if (temp.length() == 0) {
					temp = "?" + key + "=" + params.get(key);
				} else {
					temp = temp + "&" + key + "=" + params.get(key);
				}
			}
			url = url + temp;
		}
		// 创建httppost
		HttpGet httpPost = new HttpGet(url);
		if (null != headers && 0 != headers.size()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}
		}

		try {
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, "UTF-8");
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			throw e;
		} catch (UnsupportedEncodingException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				throw e;
			}
		}
		return null;

	}
}
