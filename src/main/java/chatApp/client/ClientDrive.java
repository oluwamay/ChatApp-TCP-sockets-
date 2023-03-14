package chatApp.client;

import chatApp.server.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientDrive {
    public static void main(String[] args) throws IOException {
        //Takes input of the client's username
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        //An instance of client's socket
        Socket socket = new Socket("localhost", Server.getPORT());
        //An instance of client class.
        Client client = new Client(socket, username);
        //The client has two basic functionalities; Listen for messages from other clients, and send messages to other clients
        client.receiveMessage();
        client.sendMessage();

    }
}
