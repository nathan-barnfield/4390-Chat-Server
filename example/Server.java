import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.lang.Object;
import java.nio.charset.StandardCharsets;

public class Server implements Runnable{
	
	public Server() 
	{
		Thread t = new Thread(this);
		t.run();
	}

	@Override
	public void run() {
		try {
			byte[] recvbuffer = new byte[1024]; 
			DatagramPacket received = new DatagramPacket(recvbuffer, 512);
			DatagramSocket socket = new DatagramSocket(8888);
			System.out.println("Server started awaiting data on socket");
			socket.receive(received);
			//System.out.println(received.toString());
			String a = new  String( received.getData(),
    received.getOffset(),
    received.getLength(),
    StandardCharsets.UTF_8); // or some other charset)  
			System.out.println(a);
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	public static void main(String[] args){
		Server a = new Server();
	}

}