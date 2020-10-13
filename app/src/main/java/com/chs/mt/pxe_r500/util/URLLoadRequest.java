package com.chs.mt.pxe_r500.util;

import java.nio.*;
import java.net.*;
import java.util.*;

/**
 * @author Administrator
 *
 */

public class URLLoadRequest {
	private String url;
  private ByteBuffer cache;
  private HttpURLConnection connection;
  private HashMap<String, Object> otherInfo;
  
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ByteBuffer getCache() {
		return cache;
	}
	public void setCache(ByteBuffer cache) {
		this.cache = cache;
	}
	public HttpURLConnection getConnection() {
		return connection;
	}
	public void setConnection(HttpURLConnection connection) {
		this.connection = connection;
	}
	public HashMap<String, Object> getOtherInfo() {
		return otherInfo;
	}
	public void setOtherInfo(HashMap<String, Object> otherInfo) {
		this.otherInfo = otherInfo;
	}
}
