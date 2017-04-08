package server_code;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UDP_Handshake extends Thread
{
	Map<InetAddress,BlockingQueue<DatagramPacket>> currentConnections = new HashMap<InetAddress,BlockingQueue<DatagramPacket>>();
	
	DatagramSocket socket = null;
	
	public UDP_Handshake(String name) throws IOException
	{
		socket = new DatagramSocket(4445);
	}
	
	
	public void run()
	{
		BlockingQueue<DatagramPacket> tempQueue = null;
			
		while(true)
		{
			byte[] buf = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			
			try 
			{
				socket.receive(packet);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		
			if(currentConnections.containsKey(packet.getAddress()))
			{
				tempQueue = currentConnections.get(packet.getAddress());
			
				try {
					tempQueue.put(packet);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				BlockingQueue<DatagramPacket> newQueue = new LinkedBlockingQueue<DatagramPacket>();
				
				currentConnections.put(packet.getAddress(), newQueue);
				new Authentication_Thread(packet.getAddress(), newQueue, socket).start();
				
				try {
					newQueue.put(packet);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
	}
}