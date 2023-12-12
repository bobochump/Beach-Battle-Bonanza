package com.beachbb.game;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Client extends Thread implements NetworkEntity, AutoCloseable{
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private boolean ended;
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

    public void sendAttackCommand(int effectID){
        output.println("02"+effectID);
    }
    public void sendOpponentHP(float p1hpPercent) { output.println("04"+p1hpPercent); }
    public void sendEndConnection() {output.println("99");}
    public void sendRematch() {output.println("98");}


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
                if(serverResponse.equals("99")){
                    System.out.println("Server player initiated disconnection."); // Checks if other player initiated a disconnect
                    stopNetwork();
                    queue.add(serverResponse);
                    break;
                }
                // Puts lines from server into the queue
                queue.add(serverResponse);
                if (Thread.interrupted()) {
                    return;
                }
            }
        } catch (IOException e) { // Checks if other player disconnected
            if(e instanceof SocketException && e.getMessage().equals("Socket closed")){
                System.out.println("Server player disconnected.");
            }
            else{
                e.printStackTrace();
            }
        }
        finally{
            stopNetwork();
        }

    }

    // Function that ends connection
    public void stopNetwork(){
        if(!ended) {
            output.println("99");
            ended = true;
            interrupt();
            close();
            System.out.println("Connection ended");
        }
    }

    // Function that safely closes socket
    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            // Handle the exception or log it
            e.printStackTrace();
        }
    }

}
