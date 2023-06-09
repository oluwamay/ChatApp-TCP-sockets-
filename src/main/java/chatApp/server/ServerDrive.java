package chatApp.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerDrive {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Server.getPORT());
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
