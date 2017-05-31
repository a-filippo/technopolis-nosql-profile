package app.http;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Scanner;

public class AsynHttpServer {

    private ActionController actionController;

//    public static void main(String[] args) throws Throwable {
//        ServerSocket ss = new ServerSocket(8182);
//        while (true) {
//            Socket s = ss.accept();
////            System.err.println("Client accepted");
////            new Thread(new SocketProcessor(s)).start();
//        }
//    }

    public static void main(String[] args) {
        new AsynHttpServer().start();
    }

    public void start(){
        actionController = new ActionController();
        try {
            ServerSocket ss = new ServerSocket(8182);
            while (true) {
                Socket s = ss.accept();
                new SocketProcessor(s).run();
            }
        } catch (Throwable e){
            System.err.println(e.toString());
        }
    }

    private class SocketProcessor {

        private Socket s;
        private InputStream is;
        private OutputStream os;

        private String url = "";
        HashMap<String, String> params;

        private SocketProcessor(Socket s) throws Throwable {
            this.s = s;
            this.is = s.getInputStream();
            this.os = s.getOutputStream();

            params = new HashMap<>();
        }

        public void run() {
            try {
                String headers = readInputHeaders();
                getAction(headers);
                String response = actionController.call(url, params, getPage("page.html"));

//                String fullResponse = insertData(page, response);

                writeResponse(response);
            } catch (Throwable t) {
                /*do nothing*/
            } finally {
                try {
                    s.close();
                } catch (Throwable t) {
                    /*do nothing*/
                }
            }
//            System.err.println("Client processing finished");
        }

        private String getPage(String page){

            StringBuilder result = new StringBuilder("");

            //Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(page).getFile());

            try (Scanner scanner = new Scanner(file)) {

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    result.append(line).append("\n");
                }

                scanner.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        private String insertData(String container, String data){
            String find = "{{{content}}}";
            int index = container.indexOf(find);
            return container.substring(0, index) + data + container.substring(index + find.length());
//            return data;
        }

        private void getAction(String headers){
            String[] cmd = headers.split("\\s+");
//            HashMap<String, String> params = new HashMap<>();
//            String url = "";
            if (cmd[0].equals("GET") || cmd[0].equals("HEAD")) {
                String method = cmd[0];

                int idx = cmd[1].indexOf('?');
                if (idx < 0) url = cmd[1];
                else {
                    url = decodeUri(cmd[1].substring(0, idx));
                    String[] prms = cmd[1].substring(idx + 1).split("&");

                    params = new HashMap<>();
                    for (int i = 0; i < prms.length; i++) {
                        String[] temp = prms[i].split("=");
                        if (temp.length == 2) {
                            // we use ISO-8859-1 as temporary charset and then
                            // String.getBytes("ISO-8859-1") to get the data
                            params.put(decodeUri(temp[0]), decodeUri(temp[1]));
                        } else if (temp.length == 1 && prms[i].indexOf('=') == prms[i].length() - 1) {
                            // handle empty string separatedly
                            params.put(decodeUri(temp[0]), "");
                        }
                    }
                }
            }
//            System.out.println(url);
//            System.out.println(params);
        }

        private String decodeUri(String s){
            String str = "";
            try {
                str = URLDecoder.decode(s, "ISO-8859-1");
            } catch (Exception e){

            }
            return str;
        }

        private void writeResponse(String s) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "\r\n";
            String result = response + s;
            os.write(result.getBytes());

            os.flush();
        }

        private String readInputHeaders() throws Throwable {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer headers = new StringBuffer("");
            while(true) {
                String s = br.readLine();
                headers.append(s).append("\r\n");
                if(s == null || s.trim().length() == 0) {
                    break;
                }
            }
//            System.out.println(headers);
            return headers.toString();
        }
    }
}
