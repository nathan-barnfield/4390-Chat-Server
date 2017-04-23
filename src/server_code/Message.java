package server_code;

public class Message 
{	
	//What type of message is being sent and what actions need to be taken
	String messageType 		= null;
	//The intended recipient of the message
	String recieveingUser 	= null;
	//The User.Entity that created the message
	String sendingUser		= null;
	//The data needed to be sent
	String data = null;
	
	String sessionID = null;
	
	public Message(String messType, String recieveUser, String sendUser, String messData, String sessID)
	{
		messageType = messType;
		recieveingUser = recieveUser;
		sendingUser = sendUser;
		data = messData;
		sessionID = sessID;
	}
	
	
	//Auto-generated getters and setters
	public String getRecieveingUser() {
		return recieveingUser;
	}

	public void setRecieveingUser(String recieveingUser) {
		this.recieveingUser = recieveingUser;
	}

	public String getSendingUser() {
		return sendingUser;
	}

	public void setSendingUser(String sendingUser) {
		this.sendingUser = sendingUser;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	
	public String getMessageType()
	{
		return this.messageType;
	}
}
