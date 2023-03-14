package chatApp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Socket socket;
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
            this.clientUsername = bufferedReader.readLine();//waits to read the ouput written from the print writer of the client
            clientHandlers.add(this);
            broadcastMessage(clientUsername + " joined chat");
        } catch (IOException e) {
            close(socket, bufferedReader, printWriter);
        }
    }


    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                //the ".readline()" method will block every other code from executing until it is executed,
                //Hence it has to be on its thread, so it won't block other clients from executing
                messageFromClient = bufferedReader.readLine();
                if(messageFromClient == null){
                    removeClientHandler();
                    break;
                }
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
               //If an exception is thrown, the socket and buffered reader would be closed
               close(socket, bufferedReader, printWriter);
               break;
            }
        }
    }

    public void broadcastMessage(String message) {
        for (ClientHandler client : clientHandlers) {
            if (!client.clientUsername.equals(clientUsername)) {
                client.printWriter.println(message);
           }
        }
    }

    public void removeClientHandler(){
    clientHandlers.remove(this);
    broadcastMessage(this.clientUsername +" has left the chat");
    }
    public void close(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter){
       //this method is used to close the socket and streams of a disconnected client handler
       removeClientHandler();
        try{
           if(socket !=  null){ socket.close();}
           if(bufferedReader != null){ bufferedReader.close();}
           if(printWriter != null){printWriter.close();}
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
