import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MultiEchoServer {
    private static ServerSocket serverSock;

    private static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            serverSock = new ServerSocket(PORT); // server socket
        } catch (IOException e) {
            System.out.println("Can't listen on " + PORT);
            System.exit(1);
        }
        do {
            Socket client;
            System.out.println("Listening for connection...");
            try {
                client = serverSock.accept(); // accept client
                System.out.println("New client accepted");

                ClientHandler handler = new ClientHandler(client);

                // Spawning client handler thread
                handler.start();
            } catch (IOException e) {
                System.out.println("Accept failed");
                System.exit(1);
            }
            System.out.println("Connection successful");
            System.out.println("Listening for input ...");
        } while (true);
    }
}
