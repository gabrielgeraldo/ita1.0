package br.com.ita.controle.util;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

public class NetwrokUtil {

	public static String getMecAddr() {
		String macAddr = null;
		try {
			Enumeration<NetworkInterface> eth = NetworkInterface.getNetworkInterfaces();
			while (eth.hasMoreElements()) {
				NetworkInterface eth0 = eth.nextElement();
				byte[] mac = eth0.getHardwareAddress();
				StringBuilder sb = new StringBuilder();
				if (mac != null) {
					for (int i = 0; i < mac.length; i++) {
						String aux = String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
						sb.append(aux);
					}
					if (sb.toString().length() <= 18) {
						macAddr = sb.toString();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return macAddr;
	}

	public static boolean verificaConexaoInternet(String address) {
		try {
			URL url = new URL(address);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(100);
			connection.connect();
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
			return false;
		}
	}

	public static boolean verificaConexaoInternet2(String address) {
		try {
			java.net.URL mandarMail = new java.net.URL(address);
			java.net.URLConnection conn = mandarMail.openConnection();
			java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) conn;
			httpConn.connect();
			int x = httpConn.getResponseCode();
			if (x == 200) {
				return true;
			}
			return false;
		} catch (java.net.MalformedURLException urlmal) {
			return false;
		} catch (java.io.IOException ioexcp) {
			return false;
		}
	}

}
