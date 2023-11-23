package com.beachbb.game;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Client extends Thread implements NetworkEntity{
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;

    private String address;
    private int portNum;

    ConcurrentLinkedQueue<String> queue;

    public Client(String servAddress, int portNumber, ConcurrentLinkedQueue<String> clientQueue){
        queue = clientQueue;
        address = servAddress;
        portNum = portNumber;
    }

    public void sendMoveCommand(int direction){
        output.println("01"+direction);
    }

    public void sendCharCommand(int charNum){
        output.println("03"+charNum);
    }

    /*
    public void sendAttackCommand(){
        output.println("02");
        output.println();
    }
    */

    public void run(){

        // Attempt to establish a connection
        try{
            socket = new Socket(address, portNum);
            System.out.println("Connection successful.");

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);


            // Reads lines from server
            String serverResponse;
            while ((serverResponse = input.readLine()) != null){
                // Puts lines from server into the queue
                queue.add(serverResponse);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
