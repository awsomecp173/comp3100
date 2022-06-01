import java.io.*;
import java.net.*;
import java.util.*;

public class client {
	public static Socket socket;
	public static DataOutputStream dout;
	public static BufferedReader din;
	public static String[] servers;
	public static String lastMsg;


	public static void main(String[] args) {
		try {
			connect("127.0.0.1", 50000);
			
			while (!lastMsg.equals("NONE")) {
				
				String[] job = read().split(" ");

				

			}



			quit();



		} catch (Exception e) {
			System.out.println("Exception catched: " + e);

		}

	}

	public static void connect(String host, Integer port) { // connect and perform three-way handshake

		try {
			socket = new Socket(host, port);

			dout = new DataOutputStream(socket.getOutputStream()); // Must write newline to every dataoutputstream
			din = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			send("HELO");
			read();
			send("AUTH " + System.getProperty("user.name"));
			read();
			send("REDY");


		} catch (Exception e) {
			System.out.println(e);
		}


	}

	public static String read() throws IOException {
		String str = (String) din.readLine();

		lastMsg = str;
        System.out.println("RCVD : " + str);
        return str;
	}

	public static void send(String message) throws IOException {
		try {
			String msg = message + "\n";
			dout.write(msg.getBytes());
			dout.flush();
			System.out.println("SENT : " + msg);
			return;
		} catch (Exception e){
			System.out.println("Something Wrong");
		}
	}

	public static void quit() {
		try {
			send("QUIT");
			read();

            dout.flush();
            din.close();
            socket.close();
        } catch (Exception e){
            System.out.println(e);
        }
	}


}