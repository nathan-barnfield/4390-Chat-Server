
package server_code;

import java.util.*;
import java.io.*;
import server_code.*;
import java.security.*;
import javax.crypto.*;

public class Server {
	private static Hashtable userDB = new Hashtable<>();
	private static String userDBfile = "DB.txt";
	private KeyGenerator keygenerator;
	private SecretKey myDesKey;
	private Cipher dCipher;
	public Server(){
		try{
			keygenerator = KeyGenerator.getInstance("DES");		// encrpyt using DES
			myDesKey = keygenerator.generateKey();
			dCipher = Cipher.getInstance("DES");
			dCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
			loadDB(userDBfile);
			printDB();

		}
		catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch(NoSuchPaddingException e){
			e.printStackTrace();
		}catch(InvalidKeyException e){
			e.printStackTrace();
		}
	}


	public void loadDB(String file){
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				String userID = sCurrentLine;
				int key = Integer.parseInt(br.readLine());
				byte [] tmp = Integer.toString(key).getBytes();
				byte[] textEncrypted = dCipher.doFinal(tmp); // do encryption
				userDB.put(userID, textEncrypted);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}catch(IllegalBlockSizeException e){
			e.printStackTrace();
		}catch(BadPaddingException e){
			e.printStackTrace();
		}

	}



	public int decryptKey(String user){
		int b = 0;
		try{
		   	 	dCipher.init(Cipher.DECRYPT_MODE, myDesKey);	// use DES key to decrpyt
		    	byte[] textDecrypted = dCipher.doFinal((byte[])userDB.get(user));
		    	String a = new String (textDecrypted);
		    	b = Integer.parseInt(a);
		    	
				}
				catch (Exception e){
				}
				if (b != 0)
		    	return b;
		    	else
		    		return -1;
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
		/*a.loadDB(userDBfile);
		printDB();
		 
		 Set<String> keys = userDB.keySet();
		 System.out.println ("ENCRYPTED KEYS");
	        for(String key: keys){
	            System.out.println("Value of "+key+" is: "+ userDB.get(key));
	        }
	       System.out.println ("DECRYPTED KEYS___");
	       System.out.println();
	        for(String key: keys){
	            System.out.println("Value of "+key+" is: "+ a.decryptKey(key));
	        }
		*/
	        

	}

}
