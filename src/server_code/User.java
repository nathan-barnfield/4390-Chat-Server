package server_code;

import java.io.PrintWriter;

public class User 
{
	private String 				userID;
	private int 				keyValue;
	private PrintWriter 		out 			= 	null;
	public BouncyEncryption 	encryptor		= 	null;
	private boolean 			isReachable 	= 	false;
	private String				currentSessID	= 	null;
	
	
	

	public User(String name, PrintWriter printer)
	{
		//Read file for userID and key
		userID = name;
		out = printer;
		//Enter offline state
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


