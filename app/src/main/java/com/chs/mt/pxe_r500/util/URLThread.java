package com.chs.mt.pxe_r500.util;

import java.io.*;
import java.net.*;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

public class URLThread extends Thread {
	private ILoader loader;
	private HttpURLConnection urlConnection;
	private InputStream inputStream;
	private int timeout;

	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public ILoader getLoader() {
		return loader;
	}
	public void setLoader(ILoader loader) {
		this.loader = loader;
	}

	public URLConnection getUrlConnection() {
		return urlConnection;
	}
	public void setUrlConnection(HttpURLConnection urlConnection) {
		this.urlConnection = urlConnection;
	}

	public URLThread(HttpURLConnection urlConnection, ILoader loader, int timeout) {
		this.urlConnection = urlConnection;
		this.loader = loader;
		this.timeout = timeout;
	}
	
	@Override
	public void run() { 
		urlConnection.setConnectTimeout(timeout);
		urlConnection.setReadTimeout(timeout);
		try{
			inputStream = urlConnection.getInputStream();
			try{
				byte[] bytes = new byte[1024*10];
				int readLen = -1;
				while((readLen = inputStream.read(bytes))!=-1) {
					ByteBuffer tmp = ByteBuffer.allocate(readLen);
					tmp.put(bytes, 0, readLen);
					loader.onReceiveData(urlConnection, tmp.array());
				}
				loader.onFinishLoading(urlConnection);
			}
			catch(Exception e) { 
				LogUtil.log(e);
				if(e instanceof SocketTimeoutException) 
					loader.onTimeout(urlConnection);
				else
					loader.onFail(urlConnection, "");
			}
			finally {
				try {
					inputStream.close();
				}
				catch(Exception e) {
					LogUtil.log(e);
				}
			}
		}
		catch (Exception e) {
			LogUtil.log(e);
			loader.onFail(urlConnection, "");
		}
		finally{
			urlConnection.disconnect();
		}
	}
}
