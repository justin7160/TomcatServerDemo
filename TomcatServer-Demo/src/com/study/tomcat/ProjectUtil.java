package com.study.tomcat;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;


//�[��servlet
public class ProjectUtil {
	public static Map<String, WebXml> result = new HashMap<>();
	
	public static void load() throws Exception {
		//War�ɦs�񪺸�Ƨ�
		String webapps = "C:\\TomcatDemo\\";
		
		// 0.�����Y war
		File[] projects = new File(webapps).listFiles(File :: isDirectory);
		
		for (File project : projects) {
			// 1.Ū��web.xml���A�ѪRservlet�Aservlet�������|
			WebXml webXml = new WebXmlConfigUtil().loadXml(project.getPath() + "\\WEB-INF\\web.xml");
			webXml.projectPath = project.getPath();
			
			// 2.�[��class �Ыع�H
			webXml.loadServlet();
			result.put(project.getName(), webXml);
		}
	}
	
	//�s��t�m
	public class WebXml {
		
		public String projectPath = null;
		
		//Servlet class ���X
		Map<String, Object> servlets = new HashMap<>();
		
		//Servlet Mapping
		Map<String, String> servletMapping = new HashMap<>();
		
		//��Ҷ��X
		Map<String, Servlet> servletInstances = new HashMap<>();
	
		@SuppressWarnings("deprecation")
		public void loadServlet() throws Exception {
			URLClassLoader loader = new URLClassLoader(new URL[] {
				new URL("file:" + projectPath + "\\WEB-INF\\classes\\")
			});
			
			for(Map.Entry<String, Object> entry :servlets.entrySet()) {
				String servletName = entry.getKey();
				String servletClass = entry.getValue().toString();
				
				// 1.�[��class��JVM
				Class<?> loadedServletClass = loader.loadClass(servletClass);
				// 2.�Ыع�H
				Servlet servlet = (Servlet) loadedServletClass.newInstance();
				servletInstances.put(servletName, servlet);
			}
			loader.close();
		}
	}
}
