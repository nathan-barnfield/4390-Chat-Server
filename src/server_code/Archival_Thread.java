package server_code;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Archival_Thread extends Thread {
	
	 private static BlockingQueue<Message> queue = new ArrayBlockingQueue(1024);
	 
	 public Archival_Thread()
		{
			
		}
	 
	 public void run()
	 {
		 while (true)
		 {
			 Message archiveMe;
				try {
					archiveMe = (Message) queue.take();
					archiveFunct(archiveMe);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 ;
		 }
		
		 
	 }
	 
	 public static void archive (Message msg) throws InterruptedException
	 {
		 queue.put(msg);
	 }
	 
	 private static void archiveFunct (Message msg) throws IOException
	 {
		 
		 //Extract information from the message	 
		 String firstUser = msg.recieveingUser;
		 String secondUser = msg.sendingUser;
		 String message = msg.data;
		 String sessionId = msg.sessionID; 

		 
		 
		 //Set first user to alphabetical out of first/second
		 int compare = firstUser.compareTo(secondUser);
		 if (compare < 0){
		     
		 }
		 else if (compare > 0) {
		     String tempuser = firstUser;
		     firstUser = secondUser;
		     secondUser = tempuser;
		 }

		 //Check if a session for the message exists already
		 //If so, append the message to the session file
		 String filename = "logs/"+firstUser+"."+secondUser;
		 FileWriter writer = new FileWriter(filename, true);
		 
		 String dataoutput = "<"+sessionId+">" + " " + msg.sendingUser + ": " + message + "\n";
		 
		 writer.write(dataoutput);
		 writer.flush();
		 writer.close(); 
		 
		 
	 }
	 
	
	 
	 

}
