package socket;

import java.io.*;

public class Request {
    public static final String secret_ROOT = "D:\\-------UIC-------\\Year 4\\Course\\WWW\\Assignment\\secret";
    private InputStream input;

    private String uri;

    public Request(InputStream input) {

        this.input = input;

    }

    public void parse() throws IOException {

// Read a set of characters from the socket

        StringBuffer request = new StringBuffer(2018);

        int i;

        byte[] buffer = new byte[2048];

        try {

            i = input.read(buffer);

        } catch (IOException e) {

            e.printStackTrace();

            i = -1;

        }

        for (int j = 0; j < i; j ++) {

            request.append((char)buffer[j]);

        }

        System.out.println("-----------------request----------------");

        System.out.print(String.valueOf(request));

        uri = parseUri(String.valueOf(request));
        File logger = new File(secret_ROOT, "logger.txt");
        if(!logger.exists()) {
            logger.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(logger,true);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(request.toString());
        bw.newLine();
        bw.flush();
        bw.close();
        osw.close();
        fos.close();
    }

    private String parseUri(String requestString) {

        int index1, index2;

        System.out.println("req string is"+requestString);

        index1 = requestString.indexOf(' ');

        System.out.println("index1 is "+index1);

        if (index1 != -1) {

            index2 = requestString.indexOf(' ', index1 + 1);

            System.out.println("index2 is "+index2);
            System.out.println(requestString.substring(index1 + 1, index2));
            return requestString.substring(index1 + 1, index2);

        }

        return null;

    }

    public String getUri() {

        return uri;

    }

}
