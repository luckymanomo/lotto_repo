package com.fitdogcat.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class InternetDownloader {
	public static void main(String[] args) throws MalformedURLException,IOException {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.scb.co.th", 8080));
		Authenticator authenticator = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return (new PasswordAuthentication("sumonkarnp","sup45425".toCharArray()));
			}
		};
		Authenticator.setDefault(authenticator);
		String url = "http://ftp.yz.yamagata-u.ac.jp/pub/eclipse/technology/epp/downloads/release/mars/2/eclipse-java-mars-2-win32.zip";
		URLConnection connection = new URL(url).openConnection(proxy);

		// BufferedReader bufferedReader = new BufferedReader(new
		// InputStreamReader(connection.getInputStream(),"TIS-620"));
		byte[] buffer = new byte[8 * 1024];
		InputStream inputStream = connection.getInputStream();
		try {
			OutputStream output = new FileOutputStream(new File(
					"d:/eclipse.zip"));
			try {
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					output.write(buffer, 0, bytesRead);
				}
			} finally {
				output.close();
			}
		} finally {
			inputStream.close();
		}
	}
}
