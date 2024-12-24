package localchat.server;
import localchat.utils.LogLevels;

import java.net.*;  // This will be the main package that we need for this project
import java.io.*;   // Reading and writing strings from and to our clientsockets
import java.util.Map;


public class SocketServer {
    private ServerSocket serverSocket;
    private static Map<String, String> clientNames;    // Using a map to associate ClientSockets with names

    /**
     * Start up a server chat client using the specified port passed by user
     * @param portNum
     */
    public void start(int portNum){
        System.out.printf("%s Starting new chat server on localhost at port %s \n", LogLevels.INFO.getMessage(), portNum);
    }

    /**
     * Used to stop the chat server
     * @throws IOException
     */
    public void stop() throws IOException {
        serverSocket.close();
    }

    /**
     * Used to handle incoming new clients that want to connect to the server
     */
    private static class SocketClientHandler extends Thread {

    }

    public static void main(String[] args) {
        // Using args, we pass our port number
        // Create new instance of the class then start the SocketServer
        if (args.length == 0){
            System.out.println(LogLevels.ERROR.getMessage() + "Port number not recognised. Please try again");
        } else{
            SocketServer chatServer = new SocketServer();
            chatServer.start(Integer.parseInt(args[0]));
        }


    }
}
