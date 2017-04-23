package server_code;

import java.io.*;

import java.net.*;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Reciever_Thread extends Thread
{
		private String						thisThreadsUser	= null;
		private Socket 						socket 			= null;
		private BufferedReader 				in 				= null;
		private PrintWriter 				out 			= null;
		private HashMap<Integer, User> 		cookieToUserMap = null;
		private HashMap<String, User> 		onlineUsers 	= null;
		private HashMap<String, Semaphore> 	userSemaphores 	= null;
		private User 						user 			= null;
		private BlockingQueue<Message> 		outMessages		= null;
		private String						currentSessID	= null;
		private Semaphore					sessIDSema		= null;
		
		public Reciever_Thread	(	Socket 						connection,
									HashMap<Integer, User> 		ckToUsr,
									HashMap<String,User> 		activeUsers,
									HashMap<String,Semaphore> 	usrSem,
									BlockingQueue<Message> 		outMess,
									String 						sessID,
									Semaphore					sessIDSem
								)
		{
			socket 			= 	connection;
			cookieToUserMap = 	ckToUsr;
			onlineUsers 	= 	activeUsers;
			userSemaphores 	= 	usrSem;
			outMessages 	= 	outMess;
			currentSessID	=	sessID;
			sessIDSema		=	sessIDSem;
		}
		
		
		
		public void run()
		{
			//Create the input and output streams so that we can communicate with the client
			try {in = new BufferedReader(new InputStreamReader(socket.getInputStream()));} 	catch (IOException e) {System.out.println("In TCP_Welcome_Thread: Unable to create Buffered Reader");e.printStackTrace();}
			try {out = new PrintWriter(socket.getOutputStream(), true);} 					catch (IOException e) {System.out.println("In TCP_Welcome_Thread: unable to create PrintWriter");e.printStackTrace();}
			 
			String connMess = null;
			 
			//Read in the first message sent by the client
			try {connMess = in.readLine();} catch (IOException e) {System.out.println("In Reciever Thread: Unable to retrieve transmissions from: " + socket.getInetAddress().toString());e.printStackTrace();}
			
			//Parse the first message ( All messages are comma delimeted)
			String[] connMessParts = connMess.split(",");
			
			//If the message is a CONNECT request and the cookie passed is in  the cookie-user hashmap, then create a new user and send a connected message to the client
			if(connMessParts[0].equals("CONNECT") && cookieToUserMap.containsKey(Integer.parseInt(connMessParts[1])))
				{
					User thisUser = cookieToUserMap.get(Integer.parseInt(connMessParts[1]));
					cookieToUserMap.remove(connMessParts[1]);
					thisThreadsUser = thisUser.getUserID();
					//User is created in handhsake, change this to retrieving from the hashtable instead
					out.println("CONNECTED");
					//onlinehashMap.put(user);
				}
			//If the message is not a CONNECT request or has an invalid cookie, then reject the connection and close all streams and end the thread
			else
				{
					out.println("CONNECTION REFUSED");
				
					out.close();
				
					try {in.close();} catch (IOException e) {System.out.println("In Reciever_Thread: Could not close In BufferedReader");e.printStackTrace();}
				
					try {socket.close();} catch (IOException e) {System.out.println("in Reciever_Thread: Could not close socket : " + socket.getInetAddress().toString());e.printStackTrace();}
				
					return;
				}
			
			String inMess = null;
			
			//while connected parse messages as they are sent
			while(true)
			{
				
				try {inMess = in.readLine();} catch (IOException e) {System.out.println("In Reciever_thread: unable to recieve inMess transmission from: " + socket.getInetAddress().toString()); e.printStackTrace();}
				
				String[] mess = inMess.split(",");
				
				
				
				switch(mess[0])
				{
				case "CHAT_REQUEST":	if(onlineUsers.containsKey(mess[1]))
											{
												userSemaphores.get(mess[1]).acquire();
												if(onlineUsers.get(mess[1]).isReachable())
												{
													userSemaphores	.get(thisThreadsUser)	.acquire();
													//Set both users to unreachable while they are in a chat session
													onlineUsers		.get(mess[1])			.setReachable(false);
													onlineUsers		.get(thisThreadsUser)	.setReachable(false);
													//Set both user's current session ID to the available session ID, then increment the session ID
													sessIDSema		.acquire();
													onlineUsers		.get(mess[1])			.setCurrentSessID(currentSessID);
													onlineUsers		.get(thisThreadsUser)	.setCurrentSessID(currentSessID);
													incrementSessID();
													sessIDSema		.release();
													
													//Send the CHAT_STARTED messages to both clients
													outMessages.put(new Message(	"CHAT_STARTED",
																					mess[1],
																					thisThreadsUser,
																					"CHAT_STARTED\u001e" 
																					+ onlineUsers.get(thisThreadsUser).getCurrentSessID() 
																					+ "\u001e" 
																					+ thisThreadsUser 
																				)
																	);
													
													outMessages.put(new Message(	"CHAT_STARTED",
																					thisThreadsUser,
																					mess[1],
																					"CHAT_STARTED\u001e" 
																					+ onlineUsers.get(thisThreadsUser).getCurrentSessID() 
																					+ "\u001e" 
																					+ mess[1] 
																				)
																	);
													
													
													userSemaphores	.get(mess[1])			.release();
													userSemaphores	.get(thisThreadsUser)	.release();
												}
												else
												{
													//Notify the client that the client they are trying to reach is unavailable
													userSemaphores.get(mess[1]).release();
													
													outMessages.put(new Message(	"UNREACHABLE",
																					thisThreadsUser,
																					"SERVER",
																					"UNREACHABLE\u001e" + mess[1] 
																				)
																	);												
												}
											}
											else
											{
												//Notify the client that the client they are trying to reach is not online
												outMessages.put(new Message(	"UNREACHABLE",
																				thisThreadsUser,
																				"SERVER",
																				"UNREACHABLE\u001e" + mess[1] 
																			)
																);
											}
										break;
										
				case "END_REQUEST":		
										break;
				case "CHAT":			
										break;
				case "HISTORY_REQ":		
										break;
				
				}
				
			}
			
			
		}
		
		private void incrementSessID()
		{
			//try {sessIDSema.acquire();} catch (InterruptedException e) {"In Reciever_Thread incrementSessID function: Could not acquire session ID semaphore"); e.printStackTrace();}
			String newSessID = Integer.toString(Integer.parseInt(currentSessID) + 1);
			currentSessID = newSessID;
		}
		
}

