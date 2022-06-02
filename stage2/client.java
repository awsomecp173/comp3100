//COMP3100 Stage 2 By Andrew Tjandra
//Student ID: 46451242

import java.io.*;
import java.net.*;

public class client {
	public static Socket socket;
	public static DataOutputStream dout;
	public static BufferedReader din;
	public static String[][] servers;
	public static String[] job;


	public static void main(String[] args) {
		try {
			socket = new Socket("localhost", 50000);

			dout = new DataOutputStream(socket.getOutputStream()); // Must write newline to every dataoutputstream
			din = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			send("HELO"); // Three-way handshake
			read();
			send("AUTH " + System.getProperty("user.name"));
			read();
			
			while (true) {
				send("REDY");

				job = read().split(" "); // JOBN submitTime jobID estRuntime core memory disk

				while(job[0].equals("JCPL")){ // Ignore JCPL until next JOBN or NONE
					send("REDY");
					job = read().split(" ");
				}

				if (job[0].equals("NONE")) { //If NONE is received then Quit
					break;
				}

				send("GETS Avail "+ job[4] + " " + job[5] + " " + job[6]);
				String serverN = read().split(" ")[1];

				if (serverN.equals("0")) {
					send("OK");
					read();
					send("GETS Capable "+ job[4] + " " + job[5] + " " + job[6]);

					serverN = read().split(" ")[1];
				}

				System.out.println("Number of Servers from GETS: " +serverN);

				send("OK");

				servers = new String[Integer.parseInt(serverN)][9]; //Values: serverType serverID state curStartTime core memory disk #wJobs #rJobs

				for (int i = 0; i < Integer.parseInt(serverN); i++) {
					servers[i] = read().split(" ");
				}

				send("OK");
				read();
				send("SCHD "+job[2]+ " " +servers[0][0] + " " + servers[0][1]);
				read();				

			}

			send("QUIT"); // Quitting process
			read();

            dout.flush();
            din.close();
            socket.close();

		} catch (Exception e) {
			System.out.println("Exception catched: " + e);

		}

	}

	
	public static String read() throws IOException {
		String str = (String) din.readLine();
        System.out.println("RCVD : " + str);

        return str;
	}

	public static void send(String message) throws IOException {
		dout.write((message + "\n").getBytes());
		dout.flush();
		System.out.println("SENT : " + (message + "\n"));
		
		return;
	
	}

}