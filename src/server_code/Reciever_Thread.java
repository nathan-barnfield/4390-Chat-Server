package server_code;

import java.io.*;

import java.net.*;
import java.util.HashMap;

public class Reciever_Thread extends Thread
{
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		HashMap<Integer, String> cookieToUserMap = null;
		User user = null;
		
		public Reciever_Thread(Socket connection,HashMap<Integer, String> ckToUsr)
		{
			socket = connection;
			cookieToUserMap = ckToUsr;
		}
		
		public void run()
		{
			try {in = new BufferedReader(new InputStreamReader(socket.getInputStream()));} 	catch (IOException e) {System.out.println("In TCP_Welcome_Thread: Unable to create Buffered Reader");e.printStackTrace();}
			try {out = new PrintWriter(socket.getOutputStream(), true);} 					catch (IOException e) {System.out.println("In TCP_Welcome_Thread: unable to create PrintWriter");e.printStackTrace();}
			 
			String cookie = null;
			 
			try {cookie = in.readLine();} catch (IOException e) {System.out.println("In Reciever Thread: Unable to retrieve transmissions from: " + socket.getInetAddress().toString());e.printStackTrace();}
			 
			if(cookieToUserMap.containsKey(Integer.parseInt(cookie)))
				{
					String name = cookieToUserMap.get(Integer.parseInt(cookie));
					user = new User(name, out);
				}
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
				
				}
				
			}
			
			
		}
		
}
