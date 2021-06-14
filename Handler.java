package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Handler extends Thread{
    private static final Map <String, String> CONTENT_TYPES = new HashMap<>(){{
        put("html", "text/html");
        put("jpg", "image/jpeg");
        put("json", "application/json");
        put("txt", "text/plain");
        put("", "text/plain");

    }};

    private static final String NOT_FOUND_MESSAGE = "NOT FOUND";

    private Socket socket;
    private String directory;

    public Handler(Socket socket, String directory){
        this.socket = socket;
        this.directory = directory;
    }

    @Override
    public void run(){
        try(var input = socket.getInputStream(); var output = socket.getOutputStream()){
            var URL = getRequestURL(input);
            var filePath = Path.of(directory, URL);

            if(Files.exists(filePath) && !Files.isDirectory(filePath)){
                var fileExtension = getFileExtension(filePath);
                var contentType = CONTENT_TYPES.get(fileExtension);
                var fileBytes = Files.readAllBytes(filePath);

                sendHeader(output, 200, "OK", contentType, fileBytes.length);
                output.write(fileBytes);
            }
            else{
                var contentType = CONTENT_TYPES.get("text");
                sendHeader(output, 404, "Not Found", contentType, NOT_FOUND_MESSAGE.length());

                output.write(NOT_FOUND_MESSAGE.getBytes());
            }
        }catch(IOException ex){
            ex.printStackTrace();

        }
    }

    public String getRequestURL(InputStream input){
        var reader = new Scanner(input).useDelimiter("\r\n");
        var line = reader.next();

        return line.split(" ")[1];
    }

    public String getFileExtension(Path path){
        var fileName = path.getFileName().toString();
        var fileExtensionStart = fileName.lastIndexOf(".");

        return fileExtensionStart == -1 ? "": fileName.substring(fileExtensionStart + 1);
    }

    public void sendHeader(OutputStream output, int statusCode, String statusText, String contentType, int contentLength){
        var ps = new PrintStream(output);

        ps.printf("HTTP/1.1 %d %s%n", statusCode, statusText); //HTTP/1.1 200 OK;
        ps.printf("Content-Type: %s%n", contentType);
        ps.printf("Content-Length: %s%n%n", contentLength);
    }
}
