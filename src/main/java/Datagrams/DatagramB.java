package Datagrams;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class DatagramB {
    public static void main(String[] args) throws IOException {
        //instantiate your receiver and sender sockets
        DatagramSocket receiver = new DatagramSocket(8081);
        DatagramSocket sender = new DatagramSocket();

        //instantiate your data containers (Packets)
        DatagramPacket senderPacket;
        DatagramPacket receiverPacket;

        //scanner to take input
        Scanner scanner = new Scanner(System.in);

        while(true){
            //Send data from datagram A

            String messageToSend = scanner.nextLine();
            senderPacket = new DatagramPacket(messageToSend.getBytes(),
                    messageToSend.length(), InetAddress.getLocalHost(), 8080);
            sender.send(senderPacket);

            //Reset data
            byte[] data = new byte[700000];

            //receive data
            receiverPacket = new DatagramPacket(data, data.length);
            receiver.receive(receiverPacket);

            StringBuilder message = new StringBuilder();
            int i = 0;

            while(data[i] != 0){
                message.append((char)data[i]);
                i++;
            }
            data = new byte[700000];
            System.out.println("Received: "+ message);
        }
    }
}
