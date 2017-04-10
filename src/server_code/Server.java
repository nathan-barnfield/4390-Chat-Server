
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
	

	public static void main(String[] args) 
	{
		Server a = new Server ();
		a.loadDB(userDBfile);
		 Set<String> keys = userDB.keySet();
	        for(String key: keys){
	            System.out.println("Value of "+key+" is: "+userDB.get(key));
	        }


	}

}
