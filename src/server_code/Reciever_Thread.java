package server_code;

import java.io.*;

import java.net.*;
import java.util.HashMap;

public class Reciever_Thread extends Thread
{
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		HashMap<Integer, User> cookieToUserMap = null;
		User user = null;
		
		public Reciever_Thread(Socket connection,HashMap<Integer, User> ckToUsr)
		{
			socket = connection;
			cookieToUserMap = ckToUsr;
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
					//User is created in handhsake, change this to retrieving from the hashtable instead
					user = new User(name, out);
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
			

			while(true)
			{
				
				try {inMess = in.readLine();} catch (IOException e) {System.out.println("In Reciever_thread: unable to recieve inMess transmission from: " + socket.getInetAddress().toString()); e.printStackTrace();}
				
				String[] mess = inMess.split(",");
				
				
				
				switch(mess[0])
				{
				case "CHAT_REQUEST":	
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
		
}
