package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {
    private final int port;
    private final ClientHandler client;
    private volatile boolean stopServer;
    private Thread thOfServer;

    private ServerSocket server;

    public MyServer(int port, ClientHandler client) {
        this.port = port;
        this.client = client;
        stopServer = false;
    }


    private void runSer() {
        try {
            server = new ServerSocket(port);
            server.setSoTimeout(1000); // בכל שניה השרת ימתין מחדש לחיבור של לקוח

            while (!stopServer) {
                Socket aClient = null;
                InputStream inFromClient = null;
                OutputStream outToClient = null;
                try {
                    aClient = server.accept();
                    inFromClient = aClient.getInputStream();
                    outToClient = aClient.getOutputStream();
                    // טיפול בלקוח על ידי handleClient
                    client.handleClient(inFromClient, outToClient);
                } catch (SocketTimeoutException e) {
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // סגירת כל המשאבים ללקוח עם סיום החיבור
                    try {
                        if (inFromClient != null)
                            inFromClient.close();
                        if (outToClient != null)
                            outToClient.close();
                        if (aClient != null)
                            aClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(); // מעבר למתודת סגירה לסגירת השרת
        }
    }


    public void start() {
        thOfServer = new Thread(this::runSer);
        thOfServer.start();
    }


    void close() {
        try {
            stopServer = true; 

            if (thOfServer != null) {
                thOfServer.interrupt();
                thOfServer.join(); 
            }

            if (server != null)
                server.close();

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally { 
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
