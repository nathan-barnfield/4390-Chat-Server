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
		
	}
}
