package server_code;

import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.*;

public class Outgoing_Thread extends Thread
{
	BlockingQueue<Message> outgoingQueue = null;
	HashMap<String,User> onlineUsers = null;
	HashMap<String, Semaphore> userSemaphores = null;
	
	public Outgoing_Thread(BlockingQueue<Message> outQueue, HashMap<String,User> userTable, HashMap<String,Semaphore> userSemMap)
	{
		outgoingQueue = outQueue;
		onlineUsers = userTable;
		userSemaphores = userSemMap;
	}
	
	public void run()
	{
		while(true)
		{
			Message currentMess = null;
			try { currentMess = outgoingQueue.take();} catch (InterruptedException e) {System.out.println("In Outgoing_Thread:Could not retrieve message from outgoing queue");e.printStackTrace();}
			
			if(onlineUsers.containsKey(currentMess.getRecieveingUser()))
			{
				//Acquire the semaphore for the user that is about to be retrieved
				try { userSemaphores.get(currentMess.getRecieveingUser()).acquire();} catch (InterruptedException e) {System.out.println("in Outgoing_Thread: Could not acquire User\"" + currentMess.getRecieveingUser()+"\"'s mutex");e.printStackTrace();}
				
				User recieveUser = onlineUsers.get(currentMess.getRecieveingUser());
			
				recieveUser.getOut().println(currentMess.getData());
				
				userSemaphores.get(currentMess.getRecieveingUser()).release();
			}
		}
	}
}

