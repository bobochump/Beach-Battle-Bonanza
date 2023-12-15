package com.beachbb.game;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server extends Thread implements NetworkEntity, AutoCloseable{
    private ServerSocket sSocket;
    private PrintWriter output;
    private BufferedReader input;
    private int portNum;
    private String servAddr;
    ConcurrentLinkedQueue<String> queue;
    private boolean ended;
    private boolean connected;
    public Server(String address, int portNumber, ConcurrentLinkedQueue<String> servQueue) {
        queue = servQueue;
        portNum = portNumber;
        servAddr = address;
        ended = false;
        connected = false;
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
    public void sendOpponentBehavior(int behavior) { output.println("05"+behavior); }
    public void sendEndConnection() {output.println("99");}
    public void sendRematch() {output.println("98");}


    public void run(){
        try{
            sSocket = new ServerSocket(portNum, 1, InetAddress.getByName(servAddr));
            System.out.println("Server is listening on port " + portNum);
            // Accepts connections
            while(!ended){
                try(Socket clientSocket = sSocket.accept()){
                    input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    output = new PrintWriter(clientSocket.getOutputStream(), true);

                    System.out.println("Connection successful.");
                    connected = true;

                    // Reads lines from client
                    String clientLine;
                    while ((clientLine = input.readLine()) != null){
                        // Puts lines from the client into the queue
                        if(clientLine.equals("99")){ // Checks if other player initiated a disconnect
                            System.out.println("Client initiated disconnection.");
                            stopNetwork();
                            ended = true;
                            queue.add(clientLine);
                            break;
                        }
                        queue.add(clientLine);
                    }
                } catch (IOException e){
                    if(e instanceof SocketException && e.getMessage().equals("Socket closed")){
                        System.out.println("Client player disconnected.");
                    }
                    else{
                        e.printStackTrace();
                    }
                } finally {
                    stopNetwork();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Function that ends connection
    public void stopNetwork(){
        if(!ended) {
            ended = true;
            interrupt();
            close();
            System.out.println("Connection ended!");
            connected = false;
        }
    }

    // Function that safely closes sockets
    public void close() {
        try {
            if (sSocket != null && !sSocket.isClosed()) {
                sSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return connected;
    }
}
