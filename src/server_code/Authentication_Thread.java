package server_code;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Authentication_Thread extends Thread
{
	InetAddress IpAddress = null;
	BlockingQueue<DatagramPacket> packetQueue = null;
	DatagramSocket socket = null;
	
	public Authentication_Thread(InetAddress address, BlockingQueue<DatagramPacket> queue, DatagramSocket socket)
	{
		this.IpAddress = address;
		this.packetQueue = queue;
		this.socket = socket;
	}
	
	
	public void run()
	{
		
	}
}
