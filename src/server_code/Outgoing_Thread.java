package server_code;

import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
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
				
				try {sendMessage(recieveUser.getOutStream(), recieveUser.getEncryptor().Encrypt(currentMess.getData()));} catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {e.printStackTrace();}
//				try {recieveUser.getOut().println(recieveUser.getEncryptor().Encrypt(currentMess.getData()).toString());} catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException | IOException e) {e.printStackTrace();}
				
				userSemaphores.get(currentMess.getRecieveingUser()).release();
			}
		}
	}
	
	//This function's purpose is to take a byte array and push it through the outputStream.
	//It first sends the length of the message being sent so that the reciever knows how many bytes to read from the stream
	private void sendMessage(OutputStream out, byte[] message)
	{
		//Get the length of the message and place it into a byte array we can push through the stream
		byte[] messageLen = ByteBuffer.allocate(Integer.BYTES).putInt(message.length).array();
		
		//Send the length of the message first, then send the actual message
		try {out.write(messageLen);} 	catch (IOException e) {System.out.println("IN OUTGOING_THREAD: Could not push length of message through output stream");e.printStackTrace();}
		try {out.write(message);} 		catch (IOException e) {System.out.println("IN OUTGOING_THREAD: Could not push message through output stream");			e.printStackTrace();}
	}
}

