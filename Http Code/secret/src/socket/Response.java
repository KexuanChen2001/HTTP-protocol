package socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {

    private static final int BUFFER_SIZE = 1024;

    private Request request;

    private OutputStream output;

    public Response(OutputStream output) {

        this.output = output;

    }

    public void setRequest(Request request) {

        this.request = request;

    }

    public void sendStaticResource() throws IOException {

        byte[] bytes = new byte[BUFFER_SIZE];

        FileInputStream fis = null;

        boolean errorFlag = true;

        if(request.getUri()!=null){

            File file = new File(HttpServer.WEB_ROOT, request.getUri());

            if (file.exists()) {

                fis = new FileInputStream(file);

                int ch = fis.read(bytes, 0, BUFFER_SIZE);

                while (ch != -1) {

                    output.write(bytes, 0, ch);

                    ch = fis.read(bytes, 0, BUFFER_SIZE);

                }

                errorFlag = false;

            }

        }

        if(errorFlag) {

// file not found

            String errorMessage = "HTTP/1.1 404 File NOT Fount\r\n" +

                    "Content-Type: text/html\r\n" +

                    "Content-Length: 23\r\n" +

                    "\r\n" +

                    " File Not Found ";
            output.write(errorMessage.getBytes());

        }

        if (fis != null) {

            fis.close();

        }

    }

}
