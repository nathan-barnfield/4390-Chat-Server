package server_code;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.*;

public class Outgoing_Thread extends Thread
{
	BlockingQueue<Message> outgoingQueue = null;
	Hashtable<String,User> onlineUsers = null;
	HashMap<String, Semaphore> userSemaphores = null;
	
	public Outgoing_Thread(BlockingQueue<Message> outQueue, Hashtable<String,User> userTable, HashMap<String,Semaphore> userSemMap)
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
				userSemaphores.get(key)
				User recieveUser = onlineUsers.get(currentMess.getRecieveingUser());
			
				switch(currentMess.getMessageType())
				{
				case "":
				}
			}
		}
	}
}
