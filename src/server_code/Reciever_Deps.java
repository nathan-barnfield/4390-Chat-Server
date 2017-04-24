package server_code;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Reciever_Deps 
{
	private HashMap<String, User> 		onlineUsers 		= null;
	private HashMap<String, Semaphore> 	userSemaphores 		= null;
	private BlockingQueue<Message> 		outMessages			= null;
	private String						currentSessID		= null;
	private Semaphore					sessIDSemaphore		= null;
	private Semaphore					usrSemHashSemaphore = null;
	private Semaphore					onlineUsrSemaphore	= null;
	private Semaphore					ckToUsrSemaphore	= null;
	
	public Reciever_Deps(	
							HashMap<String, User> 		onlineUsrs, 	
							HashMap<String, Semaphore> 	userSema, 	
							BlockingQueue<Message> 		outMess,		
							String						curSessID,	
							Semaphore					sessIDSema,		
							Semaphore					usrSemHashSema,
							Semaphore					onlineUsrSema,
							Semaphore					ckToUsrSema
						)
	{
		onlineUsers 		=	onlineUsrs;
		userSemaphores 		=	userSema;
		outMessages			=	outMess;
		currentSessID		=	curSessID;
		sessIDSemaphore		=	sessIDSema;
		usrSemHashSemaphore	=	usrSemHashSema;
		onlineUsrSemaphore	=	onlineUsrSema;
		ckToUsrSemaphore	=	ckToUsrSema;
	}

	public HashMap<String, User> getOnlineUsers() {
		return onlineUsers;
	}

	public void setOnlineUsers(HashMap<String, User> onlineUsers) {
		this.onlineUsers = onlineUsers;
	}

	public HashMap<String, Semaphore> getUserSemaphores() {
		return userSemaphores;
	}

	public void setUserSemaphores(HashMap<String, Semaphore> userSemaphores) {
		this.userSemaphores = userSemaphores;
	}

	public BlockingQueue<Message> getOutMessages() {
		return outMessages;
	}

	public void setOutMessages(BlockingQueue<Message> outMessages) {
		this.outMessages = outMessages;
	}

	public String getCurrentSessID() {
		return currentSessID;
	}

	public void setCurrentSessID(String currentSessID) {
		this.currentSessID = currentSessID;
	}

	public Semaphore getSessIDSemaphore() {
		return sessIDSemaphore;
	}

	public void setSessIDSemaphore(Semaphore sessIDSemaphore) {
		this.sessIDSemaphore = sessIDSemaphore;
	}

	public Semaphore getUsrSemHashSemaphore() {
		return usrSemHashSemaphore;
	}

	public void setUsrSemHashSemaphore(Semaphore usrSemHashSemaphore) {
		this.usrSemHashSemaphore = usrSemHashSemaphore;
	}

	public Semaphore getOnlineUsrSemaphore() {
		return onlineUsrSemaphore;
	}

	public void setOnlineUsrSemaphore(Semaphore onlineUsrSemaphore) {
		this.onlineUsrSemaphore = onlineUsrSemaphore;
	}

	public Semaphore getCkToUsrSemaphore() {
		return ckToUsrSemaphore;
	}

	public void setCkToUsrSemaphore(Semaphore ckToUsrSemaphore) {
		this.ckToUsrSemaphore = ckToUsrSemaphore;
	}
	
}
