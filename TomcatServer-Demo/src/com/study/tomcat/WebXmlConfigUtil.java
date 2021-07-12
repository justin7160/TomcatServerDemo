package com.study.tomcat;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.study.tomcat.ProjectUtil.WebXml;

public class WebXmlConfigUtil extends DefaultHandler {
	//Servlet class 集合
	Map<String, Object> servlets = new HashMap<>();
	
	//Servlet Mapping
	Map<String, String> servletMapping = new HashMap<>();
	
	//實例集合
	Map<String, Servlet> servletInstances = new HashMap<>();
	
	//文件地址
	private String xmlPath;
	
	public WebXml loadXml(String xmlPath) throws Exception {
		this.xmlPath = xmlPath;
		
		//解析XML的工廠對象
		SAXParserFactory parserFactory =SAXParserFactory.newInstance();
		//創建一個解析XML的對象
		SAXParser parser = parserFactory.newSAXParser();
		//創建解析助手類
		parser.parse(this.xmlPath, this);
		
		WebXml webXml = new ProjectUtil().new WebXml();
		webXml.servletInstances = servletInstances;
		webXml.servlets = this.servlets;
		webXml.servletMapping = this.servletMapping;
		return webXml;	
	}
	
	String currentServlet = null;
	String currentServletMapping = null;
	String currentParam = null;
	String qName = null;
	
	
	@Override
	public void startDocument() throws SAXException {
		System.out.println("------開始解析:" + this.xmlPath);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		this.qName = qName;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String currentValue = new String(ch, start, length);
		
		if (currentValue == null || currentValue.trim().equals("") ) {
			return;
		}
		if("servlet-name".equals(qName)) {
			currentServlet = currentValue;
			currentServletMapping = currentValue;
		}else if (qName.equals("servlet-class")) {
			String servletClass = currentValue;
			servlets.put(currentServlet, servletClass);
		}else if (qName.equals("param-name")) {
			currentParam = currentValue;
		}else if (qName.equals("param-value")) {
			String paramValue = currentValue;
			HashMap<String, String> params = new HashMap<>();
			params.put(currentParam, currentValue);
		} else if ("servlet-name".equals(qName)) {
			currentServletMapping = currentValue;
		} else if (qName.equals("url-pattern")) {
			String urlPattern = currentValue;
			
			servletMapping.put(urlPattern, currentServletMapping);
		}
		
		
	}



	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}



	public Map<String, Object> getServlets() {
		return servlets;
	}

	public void setServlets(Map<String, Object> servlets) {
		this.servlets = servlets;
	}

	public Map<String, String> getServletMapping() {
		return servletMapping;
	}

	public void setServletMapping(Map<String, String> servletMapping) {
		this.servletMapping = servletMapping;
	}

	public Map<String, Servlet> getServletInstances() {
		return servletInstances;
	}

	public void setServletInstances(Map<String, Servlet> servletInstances) {
		this.servletInstances = servletInstances;
	}
	
	
	
}
