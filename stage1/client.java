import java.net.*;
import java.io.*;

public class client {
public static void main (String args[]) {
// arguments supply message and hostname of destination
try{
	Socket s = new Socket("127.0.0.1", 50000); 

	DataOutputStream dout = new DataOutputStream(s.getOutputStream());
	BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));

	dout.write(("HELO\n").getBytes());
	System.out.println("SENT: HELO");

	String str = (String) din.readLine();
	System.out.println("RCVD: " +str);

	dout.write(("AUTH wharever \n").getBytes()); 
	System.out.println("SENT: AUTH "+ System.getProperty("user.name"));

	str = (String) din.readLine(); 
	System.out.println("RCVD: " +str);

	dout.write(("REDY\n").getBytes());
	System.out.println("SENT: REDY");

	str = (String) din.readLine();
	System.out.println("RCVD: " +str); // End of Three-way handshake, receiving the job


	String[] jobInfo = str.split(" ");

	
	int jobID = Integer.parseInt(jobInfo[2]);

	dout.write(("GETS All\n").getBytes());
	System.out.println("SENT: GETS All");

	str = (String) din.readLine();
	System.out.println("RCVD: " +str);

	int serverNRecs = Integer.parseInt(str.split(" ")[1]);


	dout.write(("OK\n").getBytes());
	System.out.println("SENT: OK");

	String biggestServerName = "";
	int biggestServerCores = 0;

	String[] currentServer;

	int nServerCount = 0;

	for (int i = 0; i < serverNRecs; i++) {
		str = (String) din.readLine();
		System.out.println("RCVD: "+ str);

		currentServer = str.split(" ");

		if (Integer.parseInt(currentServer[4]) > biggestServerCores) {
			biggestServerName = currentServer[0];
			biggestServerCores = Integer.parseInt(currentServer[4]);


			nServerCount = 0;
		} else {
			nServerCount++;
		}
		


	}


	dout.write(("OK\n").getBytes());
	System.out.println("SENT:  OK");

	str = (String) din.readLine();
	System.out.println("RCVD: "+ str);

	

	System.out.println("biggestServerName: " + biggestServerName);
	System.out.println("biggestServerCores: " + biggestServerCores);
	System.out.println("nServerCount: " + nServerCount); // includes the 0th server


	String[] jobOutput;
	String redyJobStatus = ""; // Either JOBN, JCPL or NONE
	int serverCounter = 0;

	while (jobInfo[0].equals("NONE") == false) {
		if (jobInfo[0].equals("JOBN")) {
			dout.write(("SCHD "+jobInfo[2]+ " "+biggestServerName+" "+serverCounter+"\n").getBytes()); // receive job
			System.out.println("SENT:  SCHD "+jobInfo[2]+ " "+biggestServerName+" "+serverCounter);
			str = (String) din.readLine();
			System.out.println("RCVD: "+ str);
		}

		dout.write(("REDY\n").getBytes());
		System.out.println("SENT: REDY");

		str = (String) din.readLine();
		System.out.println("RCVD: " +str);


		jobInfo = str.split(" ");

		if (serverCounter == nServerCount) {
			serverCounter = 0;
		} else {
			serverCounter++;
		}
	}

	dout.write(("QUIT\n").getBytes());
	System.out.println("SENT: QUIT");

	str = (String) din.readLine();
	System.out.println("RCVD: " +str);

	din.close();
	dout.close();
	s.close();


	
} catch (Exception e) { 
	System.out.println(e);
}
}
}
