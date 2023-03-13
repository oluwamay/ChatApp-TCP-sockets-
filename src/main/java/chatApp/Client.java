package chatApp;

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

    public Client(Socket socket, String Username){
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
            this.userName = Username;
        }catch(IOException e){
            try {
                if(socket != null){
                socket.close();}
                if(bufferedReader != null){bufferedReader.close();}
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public void sendMessage() throws IOException {
            printWriter.println(userName); // This writes the username to the clienthandler constructor
            Scanner scanner = new Scanner(System.in);//takes input from the console
            while(socket.isConnected()){
                String messageToSend = scanner.nextLine();
                printWriter.println(userName +": "+messageToSend);
            }
            socket.close();
    }

    public void receiveMessage(){
        //A thread is used so it can run concurrently with sending a message,
        //You don't have to wait to receive a message before you send one.
        new Thread(() -> {
            String messageFromChat;
            while(socket.isConnected()){
                try {
                    messageFromChat = bufferedReader.readLine();
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
