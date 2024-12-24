package localchat.client;

import localchat.utils.LogLevels;

public class SocketClient {
    private static String ip  = "127.0.0.1";

    public void connectToServer(){
        System.out.println(LogLevels.INFO.getMessage() + "Attempting to connect to server...");
    }

    public static void main(String[] args) {
        System.out.printf("What ip would you like to connect to? (Default %s)\n", ip);
        System.out.println("What port would you like to connect to?");
        System.out.println("What name would you like to give your client?");

        // TODO: add name system which will be passed to Server
        SocketClient chatClient = new SocketClient();
        chatClient.connectToServer();

    }
}
