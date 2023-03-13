package chatApp;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket; //Server socket that connects with client sockets(threads)
    private static final int PORT = 9998;
    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
    public void startServer()  {
       try{
           while(!serverSocket.isClosed()){
               //server socket establishes connection with an instance of a client socket
               System.out.println("waiting for client connection...");
               Socket socket = serverSocket.accept();
               System.out.println("A new client has connected");
               //The client socket is passed into a client handler object. Which is started as an independent thread
               ClientHandler clientHandler = new ClientHandler(socket);
               //Multiple clients will run concurrently.
               ExecutorService pool = Executors.newFixedThreadPool(2);
               pool.execute(clientHandler);
               pool.shutdown();
           }
       }catch(IOException e){
            if(serverSocket != null){
                try{
                    serverSocket.close();
                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
       }
    }

    public static int getPORT() {
        return PORT;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Server.PORT);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
