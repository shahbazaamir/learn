package record;


import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class MyHttpServer {

	public  static int  STATE=1;
	
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8099), 0);
        server.createContext("/record", new MyHandler());
        server.setExecutor(null); 
        server.start();
    }

}