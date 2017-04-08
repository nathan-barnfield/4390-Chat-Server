//Client submitting a UDP packet
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TestClient 
{

public static void main(String[] args) throws UnknownHostException, SocketException, IOException  {
    // TODO code application logic here
    String b = new String ("asdasd64444");
    byte[] buffer = b.getBytes();
    InetAddress address = InetAddress.getLocalHost();
    DatagramPacket packet = new DatagramPacket(
            buffer, buffer.length, address, 8888
            );
    DatagramSocket datagramSocket = new DatagramSocket();
    datagramSocket.send(packet);
    System.out.println(InetAddress.getLocalHost().getHostAddress());


}
}