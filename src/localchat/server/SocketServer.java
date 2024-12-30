package localchat.server;

import localchat.utils.LogLevels;

import java.net.*;  // This will be the main package that we need for this project
import java.io.*;   // Reading and writing strings from and to our client sockets
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;


public class SocketServer {
    private ServerSocket serverSocket;
    private static int MAX_MESSAGES = 20;    // Max messages that will be stored in log

    // Used to store our messages. Queue works on FIFO, so we can delete older messages as needed fairly quickly
    // Also, LinkedBlockingQueue allows for concurrency in our thread based solution
    private static LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();

    /**
     * Start up a server chat client using the specified port passed by user
     *
     * @param portNum port number for specified ip address
     */
    public void start(int portNum) {
        try {
            System.out.printf("%s Starting new chat server on localhost at port %s \n", LogLevels.INFO.getMessage(), portNum);
            serverSocket = new ServerSocket(portNum);

            System.out.printf("%s Currently running chat room at port: %s \n", LogLevels.INFO.getMessage(), portNum);

            // manage server inputs from terminal using another BufferedReader isolated in its own thread
            Thread serverInputs = new Thread(() -> {
                try {
                    Scanner serverCLIInputs = new Scanner(System.in);
                    boolean serverActive = true;
                    while (serverActive) {
                        String msgToSend = serverCLIInputs.nextLine();
                        if (".q".equals(msgToSend)) {
                            serverActive = false;

                        } else {
                            String formattedMessage = String.format("[SERVER-ADMIN]: %s (%s)",
                                    msgToSend,
                                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:m d MMMM y")));
                            addMessageToQueue(formattedMessage);
                            System.out.println(formattedMessage);

                        }

                    }
                    serverSocket.close();
                    Thread.currentThread().join();


                } catch (Exception ThreadError) {
                    System.out.println(LogLevels.ERROR.getMessage() + "An issue has occurred in serverInput thread.");
                }
            });

            serverInputs.start();

            /* if serverClient connection is successful, we want to have a while loop that constantly looks
            for socketClients. Note that .start() does not run until .accept() is successfully run - thus, this while
            loop is held until needed
            */

            while (true) {
                new SocketClientHandler(serverSocket.accept()).start();
                System.out.println(LogLevels.INFO.getMessage() + "New client has connected!");
            }

        } catch (IOException endError) {
            System.out.println("Closing server. Press CTRL+C to exit...");

        } catch (Exception err) {
            System.out.printf("There was an issue with the serverClient...%s\n", err.getMessage());
        }

    }

    /**
     * Send messages to the message queue checking if MAX_MESSAGES has been reached
     *
     * @param msg
     */
    public synchronized static void addMessageToQueue(String msg) {
        // remove older messages if MAX_MESSAGES is exceeded
        if (messageQueue.size() >= MAX_MESSAGES) messageQueue.poll();
        messageQueue.add(msg);
    }

    /**
     * Used to handle incoming new clients that want to connect to the server
     */
    private static class SocketClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private Pattern userNamePattern = Pattern.compile("^\\[.*?\\]:\\s*");

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

                // On startup, we want to send every message in the Queue to the newly connected Client
                for (Object oldMessage : messageQueue.toArray()) {
                    out.println(oldMessage);
                }

                String inputLine;
                String messageToBroadcast = "";

                while (true) {
                    // Handle incoming inputs from the client
                    if (in.ready()) {
                        if ((inputLine = in.readLine()) != null) {
                            if (".q".equals(userNamePattern.matcher(inputLine).replaceFirst(""))) {
                                break;
                            }
                            String messageWithTime = inputLine + String.format(" (%s)", LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:m d MMMM y")));
                            System.out.println(messageWithTime);
                            addMessageToQueue(messageWithTime);
                        }
                    }
                    // Handle reading of new messages from client here
                    // only send if tail of queue is different to message to Broadcast

                    if (!messageQueue.isEmpty() && !messageToBroadcast.equals(messageQueue.toArray()[messageQueue.size() - 1])) {
                        messageToBroadcast = (String) messageQueue.toArray()[messageQueue.size() - 1];  // downcast to String
                        out.println(messageToBroadcast);

                    }
                }

                System.out.println(LogLevels.INFO.getMessage() + "Disconnecting client...Goodbye!");
                out.println("Thanks for chatting. Goodbye! You are free to press Ctrl+C to end session...");
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
            if (args.length == 2)
                MAX_MESSAGES = Integer.parseInt(args[1]);   // User can change the max message history if needed
            chatServer.start(Integer.parseInt(args[0]));
        }
    }
}
