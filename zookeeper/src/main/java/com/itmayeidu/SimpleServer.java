package com.itmayeidu;
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.net.ServerSocket;  
import java.net.Socket;  
  
public class SimpleServer implements Runnable {  
  
    public static void main(String[] args) throws IOException {  
        int port = 18080;  
        SimpleServer server = new SimpleServer(port);  
        Thread thread = new Thread(server);  
        thread.start();  
    }  
  
    private int port;  
  
    public SimpleServer(int port) {  
        this.port = port;  
    }  
  

    public void run() {  
        ServerSocket server = null;  
        try {  
            server = new ServerSocket(port);  
            System.out.println("Server started");  
            Socket socket = null;  
            while (true) {  
                socket = server.accept();  
                new Thread(new SimpleServerHandler(socket)).start();  
            }  
        } catch(IOException ex) {  
            ex.printStackTrace();  
        } finally {  
            if (server != null) {  
                try {  
                    server.close();  
                } catch (IOException e) {}  
            }  
        }  
    }  
}  