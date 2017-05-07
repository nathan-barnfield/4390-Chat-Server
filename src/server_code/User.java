package server_code;

import java.io.OutputStream;
import java.io.PrintWriter;

public class User 
{
	private String 				userID;
	private int 				keyValue;
	private PrintWriter 		out 			= 	null;
	public Jasypt_Encryptor 	encryptor		= 	null;
	private boolean 			isReachable 	= 	false;
	private String				currentSessID	= 	null;
	private String				chatPartner		=	null;
	private OutputStream		outStream		=	null;
	
	


	
	public User(String name, PrintWriter printer)
	{
		//Read file for userID and key
		userID = name;
		out = printer;
		//Enter offline state
	}
	
	public OutputStream getOutStream() {
		return outStream;
	}

	public void setOutStream(OutputStream outStream) {
		this.outStream = outStream;
	}

	
	public PrintWriter getOut() {
		return out;
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
	
	public Jasypt_Encryptor getEncryptor() {
		return encryptor;
	}

	public void setEncryptor(Jasypt_Encryptor encryptor) {
		this.encryptor = encryptor;
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


