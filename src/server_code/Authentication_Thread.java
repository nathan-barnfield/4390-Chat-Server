package server_code;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.sun.corba.se.impl.ior.ByteBuffer;

import java.util.concurrent.ThreadLocalRandom;
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
	int rand;
	int secretKey = -1;
	byte[] challenge = new byte [1024];
	
	public Authentication_Thread(InetAddress address, int port, BlockingQueue<DatagramPacket> queue, DatagramSocket socket)
	{
		this.IpAddress = address;
		this.packetQueue = queue;
		this.socket = socket;
		this.port = port;
		this.rand = ThreadLocalRandom.current().nextInt(0, 1025);
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
		int secretKey = Server.verifyUser(helloUsername); //Grab the users secret key
		
		if (secretKey != -1) //If user exists
		{
			
			DatagramPacket cpacket = null;
			//Generate a challenge hash and get the challenge packet storing the rand to send to user
			try {
				cpacket = challengePacket(secretKey);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
			
			//Send the challenge packet tryblock
			try { 
				socket.send(cpacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//End tryblock
			
			
			//Wait 20 seconds for the user to send a response
			try {
				//packet = packetQueue.poll(20, TimeUnit.SECONDS);
				packet = null;
				packet = packetQueue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String response = Packet_Helpers.unpack(packet);

			
			//Did we receive a packet?
			if (response != null)
			{
				byte[] cresponse = new byte[packet.getLength()];
				cresponse = packet.getData();
				cresponse = Arrays.copyOfRange(cresponse, 0, packet.getLength());
				if (Arrays.equals(challenge, cresponse))
				{
					//Signal that we're ready to establish a TCP connection to the server
					//Create a new user, set the encryptor, put it in the activeUsers
					User newuser = new User(helloUsername, null);
					
					try {
						newuser.encryptor = new BouncyEncryption(rand, secretKey);
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					Server.activeUsers.put(helloUsername, newuser); //Store in activeUsers
					TCP_Welcome_Thread.cookieToUserMap.put(32, newuser); //Store in cookieToUserMap
					packet = Packet_Helpers.stringToPacket("AUTH_SUCC", IpAddress, port);
					try {
						socket.send(packet);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					packet = Packet_Helpers.stringToPacket("BUTH_FAIL", IpAddress, port);
					try {
						socket.send(packet);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			else
			{
				packet = Packet_Helpers.stringToPacket("User does not exist!", IpAddress, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		else{
			//Send a "User does not exist" message back to client
		}
		

	}
	
	
	private DatagramPacket challengePacket(int sk) throws NoSuchAlgorithmException 
	{
		//First, generate the expected challenge and store the result in challenge
		int key = rand+sk;
		StringBuilder sb = new StringBuilder();
		sb.append(rand);
		sb.append(sk);
		key = Integer.parseInt(sb.toString());
		byte[] password = BigInteger.valueOf(key).toByteArray();
		
		MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password);	
        challenge = md.digest();
        
       
        byte[] randPayload = new byte[1024];
        randPayload = (Integer.toString(rand)).getBytes();
        
		
	    DatagramPacket packet = new DatagramPacket(
	            randPayload, randPayload.length, IpAddress, port
	            );

	    return packet;

	}
	
	
	
}
