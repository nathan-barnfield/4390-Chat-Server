package server_code;

import java.io.*;

import java.net.*;import java.nio.ByteBuffer;import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;import javax.crypto.BadPaddingException;import javax.crypto.IllegalBlockSizeException;import javax.crypto.ShortBufferException;import org.omg.CORBA_2_3.portable.InputStream;

public class Reciever_Thread extends Thread 
{
		private User						thisThreadsUser		= null;
		private Socket 						socket 				= null;
		private BufferedReader 				in 					= null;
		private PrintWriter 				out 				= null;
		private HashMap<Integer, User> 		cookieToUserMap		= null;
		private HashMap<String, User> 		onlineUsers 		= null;
		private HashMap<String, Semaphore> 	userSemaphores 		= null;
		private User 						user 				= null;
		private BlockingQueue<Message> 		outMessages			= null;
		private String						currentSessID		= null;
		private Semaphore					sessIDSema			= null;
		private Semaphore					usrSemHashSemaphore = null;
		private Semaphore					onlineUsrSemaphore	= null;
		private Semaphore					ckToUsrSemaphore	= null;
		
		
		public Reciever_Thread	(	Socket connection, Reciever_Deps deps
				
					/*
									HashMap<Integer, User> 		ckToUsr,
									HashMap<String,User> 		activeUsers,
									HashMap<String,Semaphore> 	usrSem,
									BlockingQueue<Message> 		outMess,
									String 						sessID,
									Semaphore					sessIDSem,
									Semaphore					usrSemHashSema,
									Semaphore					onlineUsrSema,
									Semaphore					ckToUsrSema,
									BlockingQueue<Message>		messArchQueue
									*/
								)
		{
			socket 				= 	connection;
			cookieToUserMap	 	=	TCP_Welcome_Thread.cookieToUserMap;
			onlineUsers 		= 	deps.getOnlineUsers();
			userSemaphores 		= 	deps.getUserSemaphores();
			outMessages 		= 	deps.getOutMessages();
			currentSessID		=	deps.getCurrentSessID();
			sessIDSema			=	deps.getSessIDSemaphore();
			usrSemHashSemaphore = 	deps.getUsrSemHashSemaphore();
			onlineUsrSemaphore 	= 	deps.getOnlineUsrSemaphore();
			ckToUsrSemaphore	=	deps.getCkToUsrSemaphore();
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
			String[] connMessParts = connMess.split("\u001e");
			
			//If the message is a CONNECT request and the cookie passed is in  the cookie-user hashmap, then create a new user and send a connected message to the client
			if(connMessParts[0].equals("CONNECT") && cookieToUserMap.containsKey(Integer.parseInt(connMessParts[1])))
				{
					try {ckToUsrSemaphore.acquire();} catch (InterruptedException e) {e.printStackTrace();}
					
					//Retrieve the User by using the cookie as the index into the hasmap. Remove hte User once they are retrieved
					User thisUser = cookieToUserMap	.get(Integer.parseInt(connMessParts[1]));
					cookieToUserMap					.remove(connMessParts[1]);
					ckToUsrSemaphore				.release();
					
					//initiate the User with prerequisite information
					thisThreadsUser = thisUser;
					thisUser.setOut(out);					try {thisUser.setOutStream(socket.getOutputStream());} catch (IOException e2) {System.out.println("In Reciever_Thread: Could not retrieve socket's OutputStream for user \"" + thisUser.getUserID() + "\"");e2.printStackTrace();}
					thisUser.setReachable(true);
					
					try {usrSemHashSemaphore		.acquire();} catch (InterruptedException e1) {e1.printStackTrace();}
					userSemaphores					.put(thisUser.getUserID(), new Semaphore(1));
					usrSemHashSemaphore				.release();
					
					//Place the User into the online Users hashmap
					try {onlineUsrSemaphore			.acquire();} catch (InterruptedException e) {e.printStackTrace();}
					onlineUsers						.put(thisUser.getUserID(), thisUser);
					onlineUsrSemaphore				.release();
					
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
			
			byte[] inMess = null;
			
			//while connected parse messages as they are sent
			while(true)
			{
				try {inMess = readFromStream(socket.getInputStream());} catch (IOException e) {System.out.println("In Reciever_thread: unable to recieve inMess transmission from: " + socket.getInetAddress().toString()); e.printStackTrace();}				String decryptedMess = null;
				try {decryptedMess = thisThreadsUser.getEncryptor().Decrypt(inMess);} catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException e2) {e2.printStackTrace();}
				String[] mess = decryptedMess.split("\u001e");
				System.out.println("Recieved Message: " + decryptedMess);
								
				switch(mess[0])
				{				
				case "CHAT_REQUEST":	try {onlineUsrSemaphore.acquire();} catch (InterruptedException e1) {e1.printStackTrace();}	
				
										if(onlineUsers.containsKey(mess[1]))
											{
												try {usrSemHashSemaphore.acquire();} catch (InterruptedException e1) {e1.printStackTrace();}
												try { userSemaphores.get(mess[1]).acquire();} catch (InterruptedException e) {e.printStackTrace();}
												usrSemHashSemaphore.release();
												
												if(onlineUsers.get(mess[1]).isReachable())
												{
													User tempClientB = onlineUsers.get(mess[1]);
													onlineUsrSemaphore	.release();
													
													try {usrSemHashSemaphore.acquire();} catch (InterruptedException e1) {e1.printStackTrace();}
													try {userSemaphores.get(thisThreadsUser.getUserID()).acquire();} catch (InterruptedException e) {e.printStackTrace();}
													usrSemHashSemaphore.release();
													
													//Set both users to unreachable while they are in a chat session
													tempClientB			.setReachable(false);
													thisThreadsUser		.setReachable(false);
													//Set both user's current session ID to the available session ID, then increment the session ID
													try {sessIDSema.acquire();} catch (InterruptedException e) {e.printStackTrace();}
													
													tempClientB			.setCurrentSessID(currentSessID);
													thisThreadsUser		.setCurrentSessID(currentSessID);
													tempClientB			.setChatPartner(thisThreadsUser.getChatPartner());
													thisThreadsUser		.setChatPartner(tempClientB.getUserID());
													
													incrementSessID();
													sessIDSema			.release();
													
													
													//Send the CHAT_STARTED messages to both clients
													try {outMessages.put(new Message(	"CHAT_STARTED",
																						tempClientB.getUserID(),
																						thisThreadsUser.getUserID(),
																						"CHAT_STARTED\u001e" 
																						+ thisThreadsUser.getCurrentSessID() 
																						+ "\u001e" 
																						+ thisThreadsUser.getUserID(),
																						null
																					)
																		);} catch (InterruptedException e) {e.printStackTrace();}
													
													try {outMessages.put(new Message(	"CHAT_STARTED",
																						thisThreadsUser.getUserID(),
																						tempClientB.getUserID(),
																						"CHAT_STARTED\u001e" 
																						+ thisThreadsUser.getCurrentSessID() 
																						+ "\u001e" 
																						+ tempClientB.getUserID(),
																						null
																					)
																		);} catch (InterruptedException e) {e.printStackTrace();}
													
													try {usrSemHashSemaphore.acquire();} catch (InterruptedException e1) {e1.printStackTrace();}
													userSemaphores		.get(tempClientB.getUserID())			.release();
													userSemaphores		.get(thisThreadsUser.getUserID())		.release();
													usrSemHashSemaphore	.release();
												}
												else
												{
													//Notify the client that the client they are trying to reach is unavailable
													try {usrSemHashSemaphore.acquire();} catch (InterruptedException e1) {e1.printStackTrace();}
													userSemaphores		.get(mess[1])	.release();
													usrSemHashSemaphore	.release();
													onlineUsrSemaphore	.release();
													try {
														outMessages.put(new Message(	"UNREACHABLE",
																						thisThreadsUser.getUserID(),
																						"SERVER",
																						"UNREACHABLE\u001e" + mess[1],
																						null
																					)
																		);
													} catch (InterruptedException e) {e.printStackTrace();}												
												}
											}
											else
											{
												onlineUsrSemaphore.release();
	
												//Notify the client that the client they are trying to reach is not online
												try {outMessages.put(new Message(	"UNREACHABLE",
																					thisThreadsUser.getUserID(),
																					"SERVER",
																					"UNREACHABLE\u001e" + mess[1],
																					null
																				)
																	);
												} catch (InterruptedException e) {e.printStackTrace();}
											}
										break;
										
				case "END_REQUEST":		try {usrSemHashSemaphore.acquire();} catch (InterruptedException e1) {e1.printStackTrace();}
										try { userSemaphores.get(thisThreadsUser.getUserID()).acquire();} catch (InterruptedException e) {e.printStackTrace();}
										try { userSemaphores.get(thisThreadsUser.getChatPartner()).acquire();} catch (InterruptedException e) {e.printStackTrace();}
										usrSemHashSemaphore.release();
										
										try { outMessages.put(new Message("END_NOTIF",
																			thisThreadsUser.getChatPartner(),
																			thisThreadsUser.getUserID(),
																			"END_NOTIF\u001e" + thisThreadsUser.getCurrentSessID(),
																			null
																			));} catch (InterruptedException e) {e.printStackTrace();}
										try {onlineUsrSemaphore.acquire();} catch (InterruptedException e1) {e1.printStackTrace();}	
										User tempPartner = onlineUsers.get(thisThreadsUser.getChatPartner());
										onlineUsrSemaphore.release();
										
										tempPartner		.setChatPartner(null);
										tempPartner		.setCurrentSessID(null);
										thisThreadsUser	.setChatPartner(null);
										thisThreadsUser	.setCurrentSessID(null);
										tempPartner		.setReachable(true);
										thisThreadsUser	.setReachable(true);
										
										try {usrSemHashSemaphore.acquire();} catch (InterruptedException e) {e.printStackTrace();}
										userSemaphores		.get(thisThreadsUser.getUserID())	.release();
										userSemaphores		.get(tempPartner.getUserID())		.release();
										usrSemHashSemaphore	.release();
										
										break;
										
				case "CHAT":			if(mess[1].equals(thisThreadsUser.getCurrentSessID()))
											{
												try {outMessages.put(new Message("CHAT",
																				thisThreadsUser.getChatPartner(),
																				thisThreadsUser.getUserID(),
																				"CHAT\u001e" + thisThreadsUser.getCurrentSessID() + "\u001e" + mess[2],
																				null));} catch (InterruptedException e) {e.printStackTrace();}	
												
												try {Archival_Thread.archive(new Message("CHAT",
																					thisThreadsUser.getChatPartner(),
																					thisThreadsUser.getUserID(),
																					mess[2],
																					thisThreadsUser.getCurrentSessID()));} catch (InterruptedException e) {e.printStackTrace();}
											}
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
		}		//This function is designed to read a message sent over the stream		//It will first read an integer off the stream, then read that integers length off of the stream. The second read will contain the message
		private byte[] readFromStream(java.io.InputStream inputStream)		{			byte[] messLength = new byte[4];						try {inputStream.read(messLength);} catch (IOException e) {System.out.println("In Reciever Thread: Could not read message length from user \"" + thisThreadsUser.getUserID() + "'s\" stream");e.printStackTrace();}						byte[] message = new byte[ByteBuffer.wrap(messLength).getInt()];						try {inputStream.read(message);} catch (IOException e) {System.out.println("In Reciever Thread: Could not read message from user \"" + thisThreadsUser.getUserID() + "'s\" stream");e.printStackTrace();}						return message;					}				
}
