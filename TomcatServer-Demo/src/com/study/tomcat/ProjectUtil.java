package com.study.tomcat;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;


//加載servlet
public class ProjectUtil {
	public static Map<String, WebXml> result = new HashMap<>();
	
	public static void load() throws Exception {
		//War檔存放的資料夾
		String webapps = "C:\\TomcatDemo\\";
		
		// 0.解壓縮 war
		File[] projects = new File(webapps).listFiles(File :: isDirectory);
		
		for (File project : projects) {
			// 1.讀取web.xml文件，解析servlet，servlet對應路徑
			WebXml webXml = new WebXmlConfigUtil().loadXml(project.getPath() + "\\WEB-INF\\web.xml");
			webXml.projectPath = project.getPath();
			
			// 2.加載class 創建對象
			webXml.loadServlet();
			result.put(project.getName(), webXml);
		}
	}
	
	//存放配置
	public class WebXml {
		
		public String projectPath = null;
		
		//Servlet class 集合
		Map<String, Object> servlets = new HashMap<>();
		
		//Servlet Mapping
		Map<String, String> servletMapping = new HashMap<>();
		
		//實例集合
		Map<String, Servlet> servletInstances = new HashMap<>();
	
		@SuppressWarnings("deprecation")
		public void loadServlet() throws Exception {
			URLClassLoader loader = new URLClassLoader(new URL[] {
				new URL("file:" + projectPath + "\\WEB-INF\\classes\\")
			});
			
			for(Map.Entry<String, Object> entry :servlets.entrySet()) {
				String servletName = entry.getKey();
				String servletClass = entry.getValue().toString();
				
				// 1.加載class到JVM
				Class<?> loadedServletClass = loader.loadClass(servletClass);
				// 2.創建對象
				Servlet servlet = (Servlet) loadedServletClass.newInstance();
				servletInstances.put(servletName, servlet);
			}
			loader.close();
		}
	}
}
