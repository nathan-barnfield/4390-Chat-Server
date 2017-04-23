package server_code;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.io.*;
import java.net.DatagramPacket;

import server_code.*;
import java.security.*;
import javax.crypto.*;

public class Server {
	private static Hashtable userDB = new Hashtable<>();
	private static String userDBfile = "DB.txt";
	public static HashMap activeUsers = new HashMap<>();
	private KeyGenerator keygenerator;
	private static SecretKey myDesKey;
	private static Cipher dCipher;
	public static TCP_Welcome_Thread TCPWelcome;
	//The queue for outgoing messages.
	private static BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();
	//Hashmap that stores each users mutex so that no thread contentions occur
	private static HashMap<String, Semaphore> userSemaphores = new HashMap<String, Semaphore>();
	private static String currentChatSess = null;
	private static Semaphore sessIDSem	  = new Semaphore(1);

	
	public Server() throws IOException{
		loadDB(userDBfile);
		printDB();
		UDP_Handshake handshake = new UDP_Handshake("test?");
		handshake.start();
		TCPWelcome = new TCP_Welcome_Thread(new HashMap<Integer,User>());
		TCPWelcome.start();
	}


	public void loadDB(String file){
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				String userID = sCurrentLine;
				int secretKey = Integer.parseInt(br.readLine());
				
				userDB.put(userID, secretKey);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	
	public static void printDB()
	{
		Set<String> keys = userDB.keySet();
		 System.out.println("User Hashtable");
	        for(String key: keys){
	            System.out.println("User "+key+" secret key is: "+userDB.get(key));
	        }
	}


	
	/**
	 * 
	 * @param username, the username to verify
	 * @return secret key if username exists, else -1
	 */
	public static int verifyUser(String username)
	{
		Object response = userDB.get(username);
		if (response != null)
			return (int) response;
		else
			return -1;
				
	}
	

	public static void main(String[] args) throws Exception
	{
		Server a = new Server ();
		
	        

	}

}

