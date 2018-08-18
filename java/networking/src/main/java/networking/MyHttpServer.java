package networking;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MyHttpServer {

	static int  state=1;
	
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8099), 0);
        server.createContext("/record", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "<h1>Stopped Recording Screen<\\h1>";
            try {
            	
            
            if(state==1) {
            	RecordScreen.start();
            	response = "<h1>Recording Screen<\\h1>";
            	state=2;
            }else {
            	RecordScreen.stop();
            	response = "<h1>Stopped Recording Screen<\\h1>";
            	state=1;
            }
            }catch(Exception e) {
            	response = "<h1> Recording Screen Failed<\\h1>";
            }
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}