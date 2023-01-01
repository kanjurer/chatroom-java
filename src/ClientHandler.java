//This is a support class that extends Thread, runs the client thread
//and sends and receives messages

import java.io.*;
import java.net.*;
import java.util.ArrayList;

class ClientHandler extends Thread {
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();
    private Socket client;
    private String clientName;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        client = socket;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream())); // get from client
            out = new PrintWriter(client.getOutputStream(), true); // print to client
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String received, message;
            do {
                // First, read the client name which is expected before reading messages
                if (clientName == null) {
                    clientName = in.readLine();
                    message = clientName + " has joined";
                    broadcastMessage(this, message);
                    System.out.println(message);

                    // add client to the clients list
                    clients.add(this);
                }
                received = in.readLine(); // receive message from the client
                message = "Message from " + clientName + ": " + received;
                // broadcast the message
                broadcastMessage(this, message);
                System.out.println(message);
            } while (!received.equals("BYE"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (client != null) {
                    System.out.println("Closing down connection...");
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * Method used to broadcast message to the clients
     * @param self: the client initiating the broadcast
     * @param message: message to be broadcasted
     */
    public static void broadcastMessage(ClientHandler self, String message) {
        for (ClientHandler c : clients) {
            // Print only if the client is not itself
            if (!c.equals(self)) {
                c.out.println(message);
            }
        }
    }
}
