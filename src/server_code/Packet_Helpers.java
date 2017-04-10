package server_code;

import java.net.DatagramPacket;
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
}