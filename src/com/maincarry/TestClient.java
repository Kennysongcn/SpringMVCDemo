package com.maincarry;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.maincarry.model.IMyService;


public class TestClient {

	public static void main(String[] args) {
		try {
			URL url = new URL("http://localhost:8080/ns?wsdl");
			QName qName = new QName("http://impl.services.song.com/","MyServiceImplService");
			Service service =Service.create(url, qName);
			IMyService ms = service.getPort(IMyService.class );
			System.out.println(ms.add(12, 10));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
