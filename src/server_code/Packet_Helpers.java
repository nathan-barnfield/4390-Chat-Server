package server_code;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class Packet_Helpers {



public static String unpack(DatagramPacket p1)
{
	String str = new String(
		    p1.getData(),
		    p1.getOffset(),
		    p1.getLength(),
		    StandardCharsets.UTF_8 // or some other charset
		);
	return str;
}

public static DatagramPacket stringToPacket (String s1, InetAddress address, int port)
{
	byte[] buffer = s1.getBytes();

	DatagramPacket packet = new DatagramPacket(
	        buffer, buffer.length, address, port
	        );

	return packet;
}



}