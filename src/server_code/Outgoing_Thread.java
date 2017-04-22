package server_code;

import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;

public class Outgoing_Thread extends Thread
{
	BlockingQueue<Message> outgoingQueue = null;
	Hashtable<String,User> onlineUsers = null;
	
	public Outgoing_Thread(BlockingQueue<Message> outQueue, Hashtable<String,User> userTable)
	{
		outgoingQueue = outQueue;
		onlineUsers = userTable;
	}
	
	public void run()
	{
		while(true)
		{
			Message currentMess = null;
			try { currentMess = outgoingQueue.take();} catch (InterruptedException e) {System.out.println("In Outgoing_Thread:Could not retrieve message from outgoing queue");e.printStackTrace();}
			
			if(onlineUsers.contains(currentMess.getRecieveingUser()))
			{
				User recieveUser = onlineUsers.get(currentMess.getRecieveingUser());
			
				recieveUser.getOut().println(currentMess.getData());
			}
		}
	}
}
