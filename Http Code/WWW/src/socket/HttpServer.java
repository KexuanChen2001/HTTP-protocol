package socket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class HttpServer {

    private static final int BUFFER_SIZE = 1024;

    public static final String WEB_ROOT = "D:\\-------UIC-------\\Year 4\\Course\\WWW\\Assignment\\WWW";
    public static final String secret_ROOT = "D:\\-------UIC-------\\Year 4\\Course\\WWW\\Assignment\\secret";


    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutdown = false;

    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer();
        server.await();
    }

//    public void sendResponse(HttpServletResponse)

    public void await() throws IOException {

        ServerSocket serverSocket = null;

        int port = 8080;

        try {

            serverSocket = new ServerSocket(port, 1, InetAddress

                    .getByName("127.0.0.1"));

        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

            System.exit(1);

        }

// Loop waiting for a request

        while (!shutdown) {

            Socket socket = null;

            InputStream input = null;

            OutputStream output = null;

            PrintStream writer = null;
            File logger = new File(secret_ROOT, "logger.txt");
            if(!logger.exists()) {
                logger.createNewFile(); // get new file
            }
            FileOutputStream fos = new FileOutputStream(logger,true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);



            try {

//                assert serverSocket != null;

                assert serverSocket != null;
                socket = serverSocket.accept();

                input = socket.getInputStream();

                output = socket.getOutputStream();

                byte[] bytes = new byte[BUFFER_SIZE];
// create Request object and parse

                Request request = new Request(input);

                request.parse();

// create Response object

                writer = new PrintStream(output);

                boolean errorFlag = true;


                if (request.getUri() != null) {

                    if (request.getUri().equals("/")) {

                        File file = new File(WEB_ROOT, "index.html");

                        writer.println("HTTP/1.0 200 OK");// return response

                        writer.println("Content-Type:text/html");

                        writer.println("Content-Length:" + file.length());

                        writer.println();// over the info
                        bw.write("HTTP/1.0 200 OK");bw.newLine();
                        bw.write("Content-Type:text/html");bw.newLine();
                        bw.write("Content-Length:" + file.length());bw.newLine();
                        bw.newLine();
                        if (file.exists()) {

                            FileInputStream fisE = new FileInputStream(file);

                            int ch = fisE.read(bytes, 0, BUFFER_SIZE);

                            while (ch != -1) {

                                writer.write(bytes, 0, ch);

                                ch = fisE.read(bytes, 0, BUFFER_SIZE);

                            }
                        }
                    } else {
                        String uriString = request.getUri(); //uri /download/ooo/duck.gif
                        int count = 0;
                        for(char c : uriString.toCharArray()){
                            if(c == '/'){
                                count++;
                            }
                        }
                        int judge = 0;
                        int password = 0;
                        if(count == 3 || count==2){
                            if(uriString.length()>9) {
                                if(uriString.substring(0, 9).equals("/download")){
                                    judge = 1;
                                }else if(uriString.substring(4,13).equals("/download")){
                                    judge = 1;
                                }
                            }
                        }
                        //password : ooo

                        if(count == 3 || count == 2){
                            if(uriString.length()>13) {
                                if(uriString.substring(9, 13).equals("/ooo")){
                                    password = 1;
                                }
                                if(uriString.length()>4) {
                                    if(uriString.substring(0, 4).equals("/ooo")){
                                        password = 1;
                                    }
                                }
                            }
                        }
                        // judge whether password
//                        System.out.println("12.8++++++++++++++++++"+password+"     "+judge);
                        if(password == 1){//vip
                            if(judge == 1){//download
                                String[] zero = uriString.split("/");
                                String onezero = zero[zero.length-1];
                                String[] one = onezero.split("/");
                                String two = one[one.length-1];
                                String[] three = two.split("/");
                                String fileName = three[three.length-1];
                                String[] array = fileName.split("[.]");
                                String fileType = array[array.length-1].toLowerCase();
                                FileInputStream fis = null;
                                File file = new File(HttpServer.secret_ROOT, fileName);
                                if (file.exists()) {
                                    writer.println("HTTP/1.0 200 OK");// return the information
                                    writer.println("Content-Type:application/binary");
                                    writer.println("Content-Length:" + file.length());
                                    writer.println();// finish the header
                                    bw.write("HTTP/1.0 200 OK");bw.newLine();
                                    bw.write("Content-Type:application/binary");bw.newLine();
                                    bw.write("Content-Length:" + file.length());bw.newLine();
                                    bw.newLine();
                                    fis = new FileInputStream(file);
                                    int ch = fis.read(bytes, 0, BUFFER_SIZE);
                                    while (ch != -1) {
                                        writer.write(bytes, 0, ch);
                                        ch = fis.read(bytes, 0, BUFFER_SIZE);
                                    }
                                    errorFlag = false;
                                }
                                if (fis != null) {
                                    fis.close();
                                }
                            }else{//not download but password
                                System.out.println("12.7 not download but password");
                                String[] one = uriString.split("/");
                                String two = one[one.length-1];
                                String[] three = two.split("/");
                                String fileName = three[three.length-1];
                                String[] array = fileName.split("[.]");
                                String fileType = array[array.length-1].toLowerCase();
                                FileInputStream fis = null;
                                File file = new File(HttpServer.secret_ROOT, fileName);

                                if (file.exists()) {

                                    writer.println("HTTP/1.0 200 OK");// return info
                                    bw.write("HTTP/1.0 200 OK");bw.newLine();
                                    if("jpg,jepg,gif,png".contains(fileType)){//img type
                                        writer.println("Content-Type:image/"+fileType);
                                        bw.write("Content-Type:image/"+fileType);bw.newLine();
                                    }else if("pdf".contains(fileType)){
                                        writer.println("Content-Type:application/pdf");
                                        bw.write("Content-Type:application/pdf");bw.newLine();
                                    }else if("txt".contains(fileType)){
                                        writer.println("Content-Type:text/html");
                                        bw.write("Content-Type:text/html");bw.newLine();
                                    }else if("mp4".contains(fileType)){
                                        writer.println("Content-Type:video/mpeg4");
                                        bw.write("Content-Type:video/mpeg4");bw.newLine();
//                                "Content-Type:video/mpeg4"
//                                .mpeg    video/mpg
//                                .mpg    video/mpg
                                    }
                                    writer.println("Content-Length:" + file.length());
                                    bw.write("Content-Length:" + file.length());bw.newLine();
                                    writer.println();// over, finish the header
                                    bw.newLine();
                                    fis = new FileInputStream(file);
                                    int ch = fis.read(bytes, 0, BUFFER_SIZE);
                                    while (ch != -1) {
                                        writer.write(bytes, 0, ch);
                                        ch = fis.read(bytes, 0, BUFFER_SIZE);
                                    }
                                    errorFlag = false; //
                                }

                                if (fis != null) {

                                    fis.close();

                                }
                            }
                        }else{//not password
                            if(count == 2 && judge == 1){//download
                                String[] one = uriString.split("/");
                                String two = one[one.length-1];
                                String[] three = two.split("/");
                                String fileName = three[three.length-1];
                                String[] array = fileName.split("[.]");
                                String fileType = array[array.length-1].toLowerCase();
                                FileInputStream fis = null;
                                File file = new File(HttpServer.WEB_ROOT, fileName);
                                if (file.exists()) {
                                    writer.println("HTTP/1.0 200 OK");
                                    writer.println("Content-Type:application/binary");
                                    writer.println("Content-Length:" + file.length());
                                    writer.println();// finish header
                                    bw.write("HTTP/1.0 200 OK");bw.newLine();
                                    bw.write("Content-Type:application/binary");bw.newLine();
                                    bw.write("Content-Length:" + file.length());bw.newLine();
                                    bw.newLine();
                                    fis = new FileInputStream(file);
                                    int ch = fis.read(bytes, 0, BUFFER_SIZE);
                                    while (ch != -1) {
                                        writer.write(bytes, 0, ch);
                                        ch = fis.read(bytes, 0, BUFFER_SIZE);
                                    }
                                    errorFlag = false;
                                }
                                if (fis != null) {
                                    fis.close();
                                }
                            }else{//normal
                                FileInputStream fis = null;

                                File file = new File(HttpServer.WEB_ROOT, request.getUri());

                                if (file.exists()) {

                                    writer.println("HTTP/1.0 200 OK");// return info
                                    bw.write("HTTP/1.0 200 OK");bw.newLine();
                                    // determine the file
                                    String[] dir = uriString.split("/");
                                    String fileName = dir[dir.length-1];
                                    String[] array = fileName.split("[.]");
                                    String fileType = array[array.length-1].toLowerCase();
                                    if("jpg,jepg,gif,png".contains(fileType)){//img type
                                        writer.println("Content-Type:image/"+fileType);
                                        bw.write("Content-Type:image/"+fileType);bw.newLine();
                                    }else if("pdf".contains(fileType)){
                                        writer.println("Content-Type:application/pdf");
                                        bw.write("Content-Type:application/pdf");bw.newLine();
                                    }else if("txt".contains(fileType)){
                                        writer.println("Content-Type:text/html");
                                        bw.write("Content-Type:text/html");bw.newLine();
                                    }else if("mp4".contains(fileType)){
                                        writer.println("Content-Type:video/mpeg4");
                                        bw.write("Content-Type:video/mpeg4");bw.newLine();
//                                "Content-Type:video/mpeg4"
//                                .mpeg    video/mpg
//                                .mpg    video/mpg
                                    }
                                    writer.println("Content-Length:" + file.length());
                                    bw.write("Content-Length:" + file.length());bw.newLine();
                                    writer.println();// finish header
                                    bw.newLine();
                                    fis = new FileInputStream(file);
                                    int ch = fis.read(bytes, 0, BUFFER_SIZE);
                                    while (ch != -1) {
                                        writer.write(bytes, 0, ch);
                                        ch = fis.read(bytes, 0, BUFFER_SIZE);
                                    }
                                    errorFlag = false;
                                }

                                if (fis != null) {

                                    fis.close();

                                }
                            }
                        }



                    }
//                    bw.write(writer.toString());
//                    bw.newLine();
                }
                if (errorFlag) {

                    File file = new File(WEB_ROOT, "404.html");

                    writer.println("HTTP/1.0 404 File Not Found");// return
                    bw.write("HTTP/1.0 404 File Not Found");bw.newLine();
                    writer.println("Content-Type:text/html");
                    bw.write("Content-Type:text/html");bw.newLine();
                    writer.println("Content-Length:" + file.length());
                    bw.write("Content-Length:" + file.length());bw.newLine();
                    writer.println();// finish header
                    bw.newLine();
                    if (file.exists()) {

                        FileInputStream fisE = new FileInputStream(file);

                        int ch = fisE.read(bytes, 0, BUFFER_SIZE);

                        while (ch != -1) {

                            writer.write(bytes, 0, ch);

                            ch = fisE.read(bytes, 0, BUFFER_SIZE);

                        }
                    }
                }

// create Response object

//                Response response = new Response(output);
//
//                response.setRequest(request);
//
//                response.sendStaticResource();

// Close the socket;

                socket.close();

// check if the revious URI is a shutdown command
                if(request.getUri()!=null){
                    shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
                    bw.flush();
                    bw.close();
                    osw.close();
                    fos.close();
                }


            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

}

