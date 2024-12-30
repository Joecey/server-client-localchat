package localchat.client;

import localchat.utils.LogLevels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    private Socket clientSocket;
    private PrintWriter out;    // use for sending out messages to the server client via socket connection
    private static BufferedReader in;  // use for receiving messages and chats echoed from the server client
    private static String ip = "127.0.0.1";


    public void connectToServer(String ip, int port) {
        try {
            System.out.println(LogLevels.INFO.getMessage() +
                    String.format("Attempting to connect to server %s:%2d", ip, port));

            // Create new socket connection, assign out stream and assign in stream
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println(LogLevels.INFO.getMessage() + "Connected!");

        } catch (Exception err) {
            throw new Error(err.getMessage());
        }
    }

    /**
     * Send message to the serverClient that is currently connected via the socketConnection. If there is an issue,
     * return a string with the corresponding error. Here, we will just send the full string to the server, and we let
     * the server handle what to do based on the string sent -> let's us send commands aswell as messages!
     *
     * @param msg        - message to send to server
     * @param clientName - name of socketClient given on startup
     * @return Returns message received or error encountered
     */
    public void sendMessage(String msg, String clientName) {
        try {
            // TODO: add clientName in message payload

            out.println(msg);


        } catch (Exception err) {
            System.out.println(LogLevels.WARN.getMessage() + "Unable to send message...: " + err.getMessage());
        }
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) {
        Scanner clientInputs = new Scanner(System.in);
        System.out.printf("What ip would you like to connect to? (Default %s)\n", ip);
        String newIp = clientInputs.nextLine();
        ip = !newIp.replaceAll("\\s+", "").isEmpty() ? newIp : ip; // check if input ip is valid

        System.out.println("What port would you like to connect to?");
        int newPort = clientInputs.nextInt();

        // need this to consume any left over new line from nextInt
        clientInputs.nextLine();

        // TODO: ignore special characters if inputted
        System.out.println("What name would you like to give your client (No special characters are allowed)?");
        String clientName = clientInputs.nextLine().replaceAll("\\s+", "");

        try {
            // Connect to the server here by creating a new instance of the chatClient
            SocketClient chatClient = new SocketClient();
            chatClient.connectToServer(ip, newPort);

            // If connection is successful...
            // start a new thread which will be used to read incoming messages from in BufferedReader
            // perform a while loop that takes messages from terminal

            Thread incomingMsgThread = new Thread(() -> {
                String incomingMessage; // incoming messages from server
                String msgToOutput = "";
                boolean outputMsgFlag = false;
                while (true) {
                    try {
                        if (in.ready()) {
                            if ((incomingMessage = in.readLine()) != null) {
                                if (!msgToOutput.equals(incomingMessage)) {
                                    msgToOutput = incomingMessage;
                                    outputMsgFlag = true;
                                }
                            }
                        }

                        if (outputMsgFlag) {
                            System.out.println(msgToOutput);
                            outputMsgFlag = false;
                        }

                    } catch (IOException e) {
                        System.out.println("Thread shutting down...");
                        break;

                    }
                }
            });
            incomingMsgThread.start();

            String msgToSend;
            boolean clientActive = true;
            Scanner clientCLIInputs = new Scanner(System.in);
            while (clientActive) {
                msgToSend = clientCLIInputs.nextLine();
                chatClient.sendMessage(msgToSend, clientName);
                if (".q".equals(msgToSend)){
                    clientActive = false;
                }
            }

            // once we get to here, stop the connection
            incomingMsgThread.join();   // stop thread when connection is done
            chatClient.stopConnection();


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception err) {
            System.out.printf("%sThere was an issue connecting to the server: %s \n", LogLevels.ERROR.getMessage(), err.getMessage());
        }

    }
}
