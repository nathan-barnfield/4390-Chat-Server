package server_code;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCP_Welcome_Thread extends Thread
{
	
	public static HashMap<Integer, User> cookieToUserMap = null;
	ServerSocket listener = null;
	Reciever_Deps rec_deps = null;
	
	
	public TCP_Welcome_Thread(HashMap<Integer, User> ckToUsr, Reciever_Deps deps)
	{
		cookieToUserMap = ckToUsr;
		rec_deps = deps;
	}
	
	
	
	public void run()
	{
		try 
		{
			listener = new ServerSocket(8888);
			
			while(true)
			{
				Socket temp = new Socket();
				
				try {temp = listener.accept();} catch (IOException e) {System.out.println("In TCP_Welcome_Thread: Failed to accept connection from open socket");e.printStackTrace();}
				
				new Reciever_Thread(temp, rec_deps).start();
				
			}
		} catch (IOException e) {System.out.println("Failed to Initiate TCP Welcome Socket");e.printStackTrace();}
		
			finally
				{
					try {listener.close();} catch (IOException e) {System.out.println("In TCP_Welcome_Thread: Listener failed to close");e.printStackTrace();}
				}
		
		
			
		
	}
	
	
}
