import java.io.*;
import java.net.*;

public class MultiEchoClient {
    private static final int PORT = 1234;
    private static Socket link;
    private static BufferedReader in;
    private static PrintWriter out;
    private static BufferedReader kbd;

    public static void main(String[] args) {
        try {
            link = new Socket("127.0.0.1", PORT); // socket connection
            in = new BufferedReader(new InputStreamReader(link.getInputStream())); // get from server
            out = new PrintWriter(link.getOutputStream(), true); // send to server
            kbd = new BufferedReader(new InputStreamReader(System.in));

            // Get client's name first
            System.out.print("Enter name: ");
            out.println(kbd.readLine());

            // Thread to read the keyboard and send it to server
            Thread readThread = new Thread(() -> {
                String message;
                do {
                    try {
                        System.out.print("Enter message (BYE to quit): ");
                        message = kbd.readLine();
                        out.println(message); // send message to server
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } while (!message.equals("BYE"));
            });

            // Thread to get response from server and write it to the console
            Thread writeThread = new Thread(() -> {
                try {
                    String response = in.readLine();
                    while (response != null) {
                        System.out.println("\n" + response);
                        System.out.print("Enter message (BYE to quit): ");
                        response = in.readLine();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // Starting threads
            readThread.start();
            writeThread.start();

            // Joining threads
            readThread.join();
            writeThread.join();
        } catch (IOException e) {
            System.exit(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (link != null) {
                    System.out.println("Connection shutdown");
                    link.close();
                }
            } catch (IOException e) {
                System.exit(1);
            }
        }
    }//end main
}//end class MultiEchoClient
	
	
