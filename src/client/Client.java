package client;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            Scanner input = new Scanner(System.in);
            ClientReader clientReader = new ClientReader(socket);
            clientReader.start();
            while (true) {
                String query = input.nextLine();
                outputStreamWriter.write(query + '\n');
                outputStreamWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
