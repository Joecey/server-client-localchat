package localchat.client;

import localchat.utils.LogLevels;

import java.util.Scanner;

public class SocketClient {
    private static String ip  = "127.0.0.1";

    public void connectToServer(String ip, int port, String clientName){
        System.out.println(LogLevels.INFO.getMessage() + "Attempting to connect to server...");
        System.out.printf("%s, %2d, %s \n", ip, port, clientName);
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

        System.out.println("What name would you like to give your client?");
        String clientName = clientInputs.nextLine().replaceAll("\\s+", "");

        // TODO: add name system which will be passed to Server
        SocketClient chatClient = new SocketClient();
        chatClient.connectToServer(ip, newPort, clientName);

    }
}
