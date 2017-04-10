
package server_code;

import java.util.*;
import java.io.*;
import server_code.*;
public class Server {
	private static User [] userDB = new User [5];
	private String fileName;

	public Server(){

	}


	public void loadDB(String file){
		User [] temp = new User [5];
		int i = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				String [] entries = sCurrentLine.split(",");
				System.out.println(entries[1]);
				userDB[i] = new User(entries[0],Integer.valueOf(entries[1]));
				i++;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	public static void main(String[] args) 
	{
		Server a = new Server ();
		a.loadDB("DB.txt");

	}

}
