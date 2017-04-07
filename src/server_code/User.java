package server_code;

public class User 
{
	String userID;
	
	public User(String filename)
	{
		//Read file for userID and key
		//Enter offline state
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

