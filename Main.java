package com.company;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		String directory = args[1];

		new Server(port, directory).start();

		String s = "C:\\Users\\Alex\\HTTP_server";

		Path p =  Path.of(s);

		System.out.println(p);

        /*String s = "aasasdadada sdsv sdcscs svsdsfs" + "\r\n" + "sadadasd" + "\r\n" + "asdasdvsf";

        System.out.println(s);

        var reader = new Scanner(s).useDelimiter("\r\n");

        var line = reader.next();

        System.out.println(line.split(" ")[1]);

        /*Форматированный вывод
        int statusCode = 200;
        String statusText = "OK";

        var ps = new PrintStream(System.out);

        ps.printf("HTTP/1.1 %d %s", statusCode, statusText);*
    }
         */
	}
}