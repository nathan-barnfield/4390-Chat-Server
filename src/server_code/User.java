package server_code;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class User 
{
	private String 				userID;
	private int 				keyValue;
	private PrintWriter 		out 			= 	null;
	private boolean 			isReachable 	= 	false;
	private String				currentSessID	= 	null;
	private String				chatPartner		=	null;
	private byte[] key = null;
	private int rand;
	private Cipher cipher = null;
	private Cipher dcipher = null;
	
	
	


	public User(String name, PrintWriter printer)
	{
		//Read file for userID and key
		userID = name;
		out = printer;
		//Enter offline state
	}
	
	public void genKey() throws NoSuchAlgorithmException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(rand);
		sb.append(keyValue);
		String passwordToHash = sb.toString();			
        String generatedPassword = null;
 
        
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes 
            byte[] bytes = md.digest();
            this.key = bytes;

            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb2 = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb2.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            generatedPassword = sb2.toString();
            System.out.println(generatedPassword);
		
            initCiphers();
		
	}
	
	public void initCiphers()
	{
		 try
	        {
			 
	            this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	            final SecretKeySpec secretKey = new SecretKeySpec(this.key, "AES");
	            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	            
	            this.dcipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	            dcipher.init(Cipher.DECRYPT_MODE, secretKey);
	            
	            
	            
	        }
	        catch (Exception e)
	        {
	           e.printStackTrace();
	        }
	}
	
	public String encrypt(String strToEncrypt) throws IllegalBlockSizeException, BadPaddingException
	{
		byte[] plainTextByte = strToEncrypt.getBytes();
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		String encryptedText = Base64.encode(encryptedByte);
		return encryptedText;
	}
	
	public String decrypt(String strToDecrypt) throws IllegalBlockSizeException, BadPaddingException
	{
		byte[] encryptedTextByte = Base64.decode(strToDecrypt);
		byte[] decryptedByte = dcipher.doFinal(encryptedTextByte);
		String decryptedText = new String(decryptedByte);
		return decryptedText;
	}
	
	public PrintWriter getOut() {
		return out;
	}

	public void setRand(int randy)
	{
		this.rand = randy;
	}
	
	public void setKey(int secry)
	{
		this.keyValue = secry;
	}
	
	public void setOut(PrintWriter out) {
		this.out = out;
	}
	
	public boolean isReachable() {
		return isReachable;
	}

	public void setReachable(boolean isReachable) {
		this.isReachable = isReachable;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getCurrentSessID() {
		return currentSessID;
	}

	public void setCurrentSessID(String currentSessID) {
		this.currentSessID = currentSessID;
	}
	

	public String getChatPartner() {
		return chatPartner;
	}

	public void setChatPartner(String chatPartner) {
		this.chatPartner = chatPartner;
	}
	
	private void Offline()
	{
		//User input loop
		//On "log on" input, send a chat request
		
		
	}
	
	private void ChallengeWait()
	{
		//Listen for challenge, if nothing received after x time, 
		//return to Offline
		//When challenge received, send response
		//Enter Wait_Auth_Success
	}
	
	private void Wait_AUTH_SUCCESS()
	{
		//Listen for response, 
		//	if Auth_Fail or timeout, return to Offline
		//	else GenerateCK, Establish TCP, Send Connect
		//Transition to Connecting
		
	}
	
	private void Connecting()
	{
		//Await CONNECTED message
		//Display Connected
		//Transition to Connected state
		
	}
	
	private void Connected()
	{
		//
		
	}
}


