package server_code;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
/**
 * This thread handles the authentication process. It is passed an IP address the packet is originating from, the queue associated with the address, and the socket on which to communicate

 *
 */
public class Authentication_Thread extends Thread
{
	InetAddress IpAddress = null;
	BlockingQueue<DatagramPacket> packetQueue = null;
	DatagramSocket socket = null;
	int port = -1;
	
	public Authentication_Thread(InetAddress address, int port, BlockingQueue<DatagramPacket> queue, DatagramSocket socket)
	{
		this.IpAddress = address;
		this.packetQueue = queue;
		this.socket = socket;
		this.port = port;
	}
	
	
	public void run()
	{
		DatagramPacket packet = null;
		
		//Get the Hello message
		try {
			packet = packetQueue.take();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String helloUsername = Packet_Helpers.unpack(packet); //Unpack the hello packet for the username
		int secretKey = Server.decryptKey(helloUsername); //Grab the users secret key
		
		if (secretKey != -1) //If user exists
		{
			packet = challengePacket(); //Get the challenge packet
			
			//Send the challenge packet tryblock
			try { 
				socket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//End tryblock
			
			
			//Wait 20 seconds for the user to send a response
			try {
				packet = packetQueue.poll(20, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String response = Packet_Helpers.unpack(packet);
			
			//Did we receive a packet?
			if (response != null)
			{
				//Verify packet is good
				//Signal that we're ready to establish a TCP connection to the server
			}
			else
			{
				//We timed out.
			}
			
			
		}
		else{
			//Send a "User does not exist" message back to client
		}
		

	}
	
	
	
	private DatagramPacket challengePacket()
	{
		byte[] buffer = new byte[1024];		
		String challenge = new String ("CHALLENGE"); //Put our challenge into this string
		buffer = challenge.getBytes();
		
		System.out.println(IpAddress);
		System.out.println(port);
		
	    DatagramPacket packet = new DatagramPacket(
	            buffer, buffer.length, IpAddress, port
	            );

	    return packet;

	}
	
	
	
}
