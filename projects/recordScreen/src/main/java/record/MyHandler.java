package record;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import static record.MyHttpServer.STATE;

public class MyHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange t) throws IOException {
		String response = "Bye.html";
		int length =513;
		String path=Config.getProp(Constants.VIEW_PATH);
		System.out.println(path);
		try {


			if(STATE==1) {
				//RecordScreen.start();
				response = "Welcome.html";
				length =505;
				STATE=2;
			}else {
				//RecordScreen.stop();
				response = "Bye.html";
				length =513;
				STATE=1;
			}
		}catch(Exception e) {
			response = "Error.html";

		}
		FileInputStream fio=new FileInputStream(path+response);
		byte []  respon=new byte[length];
		fio.read(respon);
		t.sendResponseHeaders(200, length);
		OutputStream os = t.getResponseBody();
		os.write(respon);
		os.close();
	}
}
