package protocol;

import javafx.application.Application;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Deque;
import java.util.logging.Logger;


public class Client implements Runnable {
    public static final Logger log = Logger.getLogger(Client.class.getName());

    public static int port;
    public static String host;
    public static PrintWriter outgoing;
    public static BufferedReader incoming;
    private static Deque<String> responses;
    public static Thread thread;
    public static Client client;
    public Browser browser;
    //public static Application browser;

    public Client(BufferedReader incoming, PrintWriter outgoing){
        //terminal();
        this.incoming=incoming;
        this.outgoing=outgoing;
        client = this;
        thread = new Thread(this);
        thread.start(); log.info("thread started listening");
        Application.launch(Browser.class); log.info("interface provided");
        System.out.println(Platform.isAccessibilityActive());
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) { System.err.println("Usage: java protocol.Client <host> <port>"); System.exit(1); }
        host = args[0];
        port = Integer.parseInt(args[1]); log.info("address specified");
        try {
            Socket socket = new Socket(host, port);
            outgoing = new PrintWriter(socket.getOutputStream(), true);
            incoming = new BufferedReader(new InputStreamReader(socket.getInputStream())); log.info(host + " picked up");
            new Client(incoming,outgoing);
        } catch (UnknownHostException exception) { System.err.println(host + " is not online."); System.exit(1);
        } catch (IOException exception) { System.err.println(host + " didn't hear You clearly."); System.exit(1);
        } log.info("connection established");
    }

    public void run() {
        String response = null;
        try {
            while(true) {
                response = incoming.readLine();
                //responses.offer(response);
                if (response != null) log.info("message received: " + response);
                browser.bubble(response,true);
            }
        } catch (IOException e) {e.printStackTrace();};
        //return responses.poll();
    }

/*  private String terminal() throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String request = input.readLine();
        return contact(request);
    }
*/
    public void contact(String request) {
        if (request != null) {
            outgoing.println(request); log.info("message sent");
            if (request.equals("Bye")) System.exit(1);
        }
    }

    public void ping(Object packet){
    }

    public void trace(Object packet){
    }
}
