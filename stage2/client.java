import java.io.*;
import java.net.*;
import java.util.*;

public class client {
	public static Socket socket;
	public static DataOutputStream dout;
	public static BufferedReader din;
	public static String[][] servers;
	public static String[] job;
	public static boolean notQuit;


	public static void main(String[] args) {
		try {
			notQuit = true;

			connect("127.0.0.1", 50000);
			
			while (true) {
				send("REDY");

				String str = read();

		
				job = str.split(" ");


				while(job[0].equals("JCPL")){
					send("REDY");
					job = read().split(" ");
				}

				int jobID = Integer.parseInt(job[2]);
				int jobCore = Integer.parseInt(job[4]);
				int jobMem = Integer.parseInt(job[5]);
				int jobDisk = Integer.parseInt(job[6]);

				send("GETS Avail "+ jobCore + " " + jobMem + " " + jobDisk);
				String serverN = read().split(" ")[1];

				if (serverN.equals("0")) {
					send("OK");
					read();
					send("GETS Capable "+ jobCore + " " + jobMem + " " + jobDisk);

					serverN = read().split(" ")[1];
				}

				System.out.println("Number of Servers from GETS: " +serverN);

				send("OK");

				servers = new String[Integer.parseInt(serverN)][9];

				for (int i = 0; i < Integer.parseInt(serverN); i++) {
					servers[i] = read().split(" ");
				}

				send("OK");

				read();

				send("SCHD "+jobID+ " " +servers[0][0] + " " + servers[0][1]);

				read();
				


			}






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


		} catch (Exception e) {
			System.out.println(e);
		}


	}

	public static String read() throws IOException {
		String str = (String) din.readLine();

        System.out.println("RCVD : " + str);

        if (str.equals("NONE")) {
        	quit();
        }

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
			notQuit = false;

			System.out.println("QUITTING!");
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