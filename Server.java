package com.company;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private int port;
    private String directory;

    public Server(int port, String directory){
        this.port = port;
        this.directory = directory;
    }

    void start(){
        try(var server = new ServerSocket(port)){

                var socket = server.accept();
                var thread = new Handler(socket, directory);

                thread.start();

                System.out.println("Server listening port: " + port);

        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
