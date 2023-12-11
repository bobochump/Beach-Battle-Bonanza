package com.beachbb.game;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server extends Thread implements NetworkEntity{
    private ServerSocket sSocket;
    private PrintWriter output;
    private BufferedReader input;
    private int portNum;

    ConcurrentLinkedQueue<String> queue;

    public Server(int portNumber, ConcurrentLinkedQueue<String> servQueue) {
        queue = servQueue;
        portNum = portNumber;
    }

    public void sendMoveCommand(int direction){
        output.println("01"+ direction);
    }

    public void sendCharCommand(int charNum){
        output.println("03"+charNum);
    }

    public void sendAttackCommand(int effectID){
        output.println("02"+effectID);
    }
    public void sendOpponentHP(float p1hpPercent) { output.println("04"+p1hpPercent); }

    public void run(){
        try{
            sSocket = new ServerSocket(portNum);
            System.out.println("Server is listening on port " + portNum);
            // Accepts connections
            while(true){
                try(Socket clientSocket = sSocket.accept()){
                    input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    output = new PrintWriter(clientSocket.getOutputStream(), true);

                    System.out.println("Connection successful.");

                    // Reads lines from client
                    String clientLine;
                    while ((clientLine = input.readLine()) != null){
                        // Puts lines from the client into the queue
                        queue.add(clientLine);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
