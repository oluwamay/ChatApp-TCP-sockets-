package Socket2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class DatagramA {
    public static void main(String[] args) throws IOException {
       //will contain data to be sent
        byte[] data = new byte[700000];
        //Create datagram sockets
        DatagramSocket receiver = new DatagramSocket(8080);//receives from port 8080
        DatagramSocket sender = new DatagramSocket();

        //create datagram packets that serve as data containers
        DatagramPacket senderPacket;
        DatagramPacket receiverPacket;

        //take input from the console
        Scanner scanner = new Scanner(System.in);

        while(true){
            //instantiate a receiver packet
            receiverPacket = new DatagramPacket(data, data.length);
            receiver.receive(receiverPacket);

            //Read data and pass into a stringbuilder instance
            StringBuilder message = new StringBuilder();
            int i = 0;

            while(data[i] != 0){
                message.append((char) data[i]);
                i++;
            }
            data = new byte[700000];
            System.out.println("Received: "+ message);

            //Send data
            String messageToSend = scanner.nextLine();

            senderPacket = new DatagramPacket(messageToSend.getBytes(),
                    messageToSend.length(), InetAddress.getLocalHost(), 8081);
            //sends to port 8081
            sender.send(senderPacket);

            data = new byte[700000];
        }

    }
}
