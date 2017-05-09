package server_code;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.sun.jmx.remote.internal.ArrayQueue;

public class Archive_Retriever 
{
	 
	 public Archive_Retriever()
	{
			
	}
	
	 public static Queue<String> retrieveHistory(String firstUser, String secondUser) throws FileNotFoundException, IOException
	 {

		 Queue<String> myQueue = new LinkedList<String>();

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

		 try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			    String line;
			    while ((line = br.readLine()) != null) 
			    {
			       myQueue.add(line);
			    }
			}
		 
		 return myQueue;
		 
	 }
		
	 
	 
}


