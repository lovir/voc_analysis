package com.diquest.voc.common.crypto;

import java.io.IOException;
import org.apache.commons.dbcp.BasicDataSource;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings({ "restriction", "unused" })
public class CryptoDataSource extends BasicDataSource{

	@Override
	public synchronized void setUrl(String url) {
		super.setUrl(url);
	}
	
	@Override
	public synchronized void setUsername(String username) {
		super.setUsername(username);
	}
	
	@Override
	public synchronized void setPassword(String pwd) {
		byte[] b1 = null;
		try {
		/*	BASE64Encoder encoder = new BASE64Encoder();
			System.out.println("encoder:"+new String(encoder.encode("test".getBytes())));//"pwd"자리에 encoding할 패스워드 입력*/
			BASE64Decoder decoder = new BASE64Decoder();
			b1 = decoder.decodeBuffer(pwd);
			super.setPassword(new String(b1));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void main(){
		System.out.println("test");
	}
}
