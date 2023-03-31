package chatApp.client;

import chatApp.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private String userName;

    public Client(Socket socket, String Username) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
            this.userName = Username;
        } catch (IOException e) {
            close(socket, bufferedReader, printWriter);
        }
    }

    public void sendMessage(){
        try {
            printWriter.println(userName); // This writes the username to the clienthandler constructor
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));//takes input from the console
            while (socket.isConnected()) {
                String messageToSend = clientInput.readLine();
                printWriter.println(userName + ": " + messageToSend);
            }
        }catch(IOException e) {
            close(socket, bufferedReader, printWriter);
        }
    }

    public void receiveMessage(){
        //A thread is used so it can run concurrently with sending a message,
        //You don't have to wait to receive a message before you send one.
        new Thread(() -> {
            String messageFromChat;
            while(socket.isConnected()){
                try {
                    //reads the broadcast message from client handler

                    messageFromChat = bufferedReader.readLine();
                    if(messageFromChat == null){
                        System.out.println("Server downtime !!!");
                        break;
                    }
                    System.out.println(messageFromChat);
                } catch (IOException e) {
                   try{
                       if(bufferedReader != null){
                       bufferedReader.close();}
                      if(socket != null){socket.close();}
                   }catch(IOException ex){
                       ex.printStackTrace();
                   }
                    break;
                }
            }
        }).start();
    }

    private void close(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter){
        //this method is used to close the socket and streams of a disconnected client handler
        try{
            if(socket !=  null){ socket.close();}
            if(bufferedReader != null){ bufferedReader.close();}
            if(printWriter != null){printWriter.close();}
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
