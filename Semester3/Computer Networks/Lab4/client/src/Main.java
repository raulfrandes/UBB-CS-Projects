import java.net.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        try {
            Socket c = new Socket("100.120.248.167", 80);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            short a, b, s;

            System.out.print("a = ");
            a = Short.parseShort(reader.readLine());

            System.out.print("b = ");
            b = Short.parseShort(reader.readLine());

            DataInputStream socketIn = new DataInputStream(c.getInputStream());
            DataOutputStream socketOut = new DataOutputStream(c.getOutputStream());

            socketOut.writeShort(a);
            //socketOut.writeShort(b);
            socketOut.flush(); // Ensure data is sent immediately

            Thread.sleep(100);

            s = socketIn.readShort();
            System.out.println("s = " + s);

            socketOut.close(); // Close the output stream gracefully
            reader.close();
            c.close();
        } catch (IOException e) {
            System.err.println("Error in client: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}