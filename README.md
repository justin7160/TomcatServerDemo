TomcatServerDemo
Practice Building Tomcat Server in GET Method

0.  將src內的TomcatDemo(此專案為: https://github.com/justin7160/servlet-demo) 資料夾放到C:/目錄之下，
    或者將ProjectUtil.java的webapps 改成對應的路徑
1.  以java applicatioin運行TomcatServerV1，應該見到順利運行
2.  執行 http://localhost:8080/servlet-demo-1.0.1 ，系統會打印出接收到的Request(GET)，
    還有被呼叫的servlet，應返回Hello World於頁面上。


藉這個demo專案了解:
  0.  藉由ServerSocket開啟port讓外部訪問、
  1.  socket藉由多線程Executors.newCachedThreadPool()監聽處理request、
  2.  http請求的過程、
  3.  解析WebXml、如何加載Servlet、
  4.  servlet的生命週期(init()->service()->close())
