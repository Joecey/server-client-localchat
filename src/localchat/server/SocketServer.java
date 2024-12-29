package localchat.server;

import localchat.utils.LogLevels;

import java.net.*;  // This will be the main package that we need for this project
import java.io.*;   // Reading and writing strings from and to our clientsockets
import java.util.LinkedList;
import java.util.Queue;


public class SocketServer {
    private ServerSocket serverSocket;
    private final int MAX_MESSAGES = 20;    // Max messages that will be stored in log

    // Used to store our messages. Queue works on FIFO so we can delete older messages as needed fairly quickly
    // Also, Queue allows for concurrency in our thread based solution
    private static Queue<String> messageQueue = new LinkedList<String>();

    /**
     * Start up a server chat client using the specified port passed by user
     *
     * @param portNum port number for specified ip address
     */
    public void start(int portNum) {
        try {
            System.out.printf("%s Starting new chat server on localhost at port %s \n", LogLevels.INFO.getMessage(), portNum);
            serverSocket = new ServerSocket(portNum);

            /* if serverClient connection is successful, we want to have a while loop that constantly looks
            for socketClients. Note that .start() does not run until .accept() is successfully run - thus, this while
            loop is held until needed
            */

            // TODO: add a separate thread to include command line functionality

            System.out.printf("%s Currently running chat room at port: %s \n", LogLevels.INFO.getMessage(), portNum);
            while (true) {
                new SocketClientHandler(serverSocket.accept()).start();
                System.out.println(LogLevels.INFO.getMessage() + "New client has connected!");
            }


        } catch (Exception err) {
            System.out.printf("There was an issue with the serverClient...%s\n", err.getMessage());
        }

    }

    /**
     * Used to stop the chat server
     *
     * @throws IOException
     */
    public void stop() throws IOException {
        serverSocket.close();
    }

    // TODO: add ability for server to send messages and close itself
    public void SendMessage(String msg) {
    }

    /**
     * Used to handle incoming new clients that want to connect to the server
     */
    private static class SocketClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        // Constructor for SocketClientHandler thread
        public SocketClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        // remember to always include our run method as required by threads
        public void run() {
            try {
                // message to send out to client
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // messages received from client
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                // TODO: basic example currently - get message from client and echo it
                String inputLine;
                while ((inputLine = in.readLine()) != null) {

                    if (".q".equals(inputLine)) {
                        break;
                    }
                    out.println(inputLine);
                }

                System.out.println(LogLevels.INFO.getMessage() + "Disconnecting client...Goodbye!");
                out.println("Thanks for chatting. Goodbye!");
                in.close();
                out.close();
                clientSocket.close();


            } catch (IOException err) {
                System.out.println(LogLevels.INFO.getMessage() + "A Client has disconnected!");
            } catch (Exception err) {
                System.out.println(LogLevels.ERROR.getMessage() + "An error has occurred with a socketClient " + err.getMessage());
            }

        }

    }

    public static void main(String[] args) {
        // Using args, we pass our port number
        // Create new instance of the class then start the SocketServer
        if (args.length == 0) {
            System.out.println(LogLevels.ERROR.getMessage() + "Port number not recognised. Please try again");
        } else {
            SocketServer chatServer = new SocketServer();
            chatServer.start(Integer.parseInt(args[0]));
        }
    }
}
