package edu.ubi.sc.haf;

import io.undertow.Undertow;
import io.undertow.server.handlers.BlockingHandler;


public class App 
{
    public static void main( String[] args )
    {
        int serverPort = 8080;
        if (args.length > 0 && !"".equals(args[0].trim())) {
        	try {
        	serverPort = Integer.parseInt(args[0].trim());
        	} catch (NumberFormatException nfe) {
        		System.err.println("Failed to parse port <" + args[0].trim() + ">. A numeric argument is required.");
        		System.exit(1);
        	}
        }
        System.err.println( "Starting up on port " + serverPort );
        Undertow server = Undertow.builder()
                .addHttpListener(serverPort, "localhost")
                .setHandler(new BlockingHandler(new ServiceRequests())).build();
        server.start();
        
        System.err.println("Server listening on 127.0.0.1:" + serverPort);
    }
}