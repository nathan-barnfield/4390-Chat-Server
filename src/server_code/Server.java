
package server_code;

import java.util.*;
import java.io.*;
import server_code.*;
public class Server {
	private static Hashtable userDB = new Hashtable<>();
	private static String userDBfile = "DB.txt";

	public Server(){

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
	public static int VerifyUser(String username)
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
		a.loadDB(userDBfile);
		printDB();
		 


	}

}
