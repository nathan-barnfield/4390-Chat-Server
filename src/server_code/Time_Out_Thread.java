package server_code;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Semaphore;


//This thread times out users that have been idle for a certain amount of time. The thread also keeps track of the current session ID and store it on the disk
public class Time_Out_Thread extends Thread
{
	private HashMap<String,User> 				activeUsers 			= null;
	private String								lastKnownSessID			= null;
	private static HashMap<String, Semaphore> 	userSemaphores 			= null;
	private static Semaphore 					sessIDSem	  			= null;
	private static Semaphore 					usrSemHashSemaphore 	= null;
	private static Semaphore 					onlineUsersSemaphore 	= null;
	private String								currentSessID			= null;
	
	public Time_Out_Thread(Reciever_Deps recDeps)
	{
		this.activeUsers 			= recDeps.getOnlineUsers();
		this.lastKnownSessID		= new String(recDeps.getCurrentSessID());
		this.userSemaphores			= recDeps.getUserSemaphores();
		this.sessIDSem				= recDeps.getSessIDSemaphore();
		this.usrSemHashSemaphore	= recDeps.getUsrSemHashSemaphore();
		this.onlineUsersSemaphore	= recDeps.getOnlineUsrSemaphore();
		this.currentSessID			= recDeps.getCurrentSessID();
	}
	
	public void run()
	{
		while(true)
		{
			System.out.println("Current availability of system semaphores:");
			System.out.println("sessIDSem permits: " + sessIDSem.availablePermits());
			System.out.println("usrSemHashSemaphore permits: " + usrSemHashSemaphore.availablePermits());
			System.out.println("onlineUsersSemaphore permits: " + onlineUsersSemaphore.availablePermits());

			//Wait one second before checking everything
			try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
			
			try {sessIDSem.acquire();} catch (InterruptedException e) {e.printStackTrace();}
			String temp = new String(currentSessID);
			sessIDSem.release();
			
			if(!lastKnownSessID.equals(temp))
			{
				PrintWriter writer = null;
				try {writer = new PrintWriter("sessID.txt","UTF-8");} catch (FileNotFoundException | UnsupportedEncodingException e) {e.printStackTrace();}
				writer.println(temp);
				lastKnownSessID = new String(temp);
			}
			try {usrSemHashSemaphore.acquire();} catch (InterruptedException e) {e.printStackTrace();}
			try {onlineUsersSemaphore.acquire();} catch (InterruptedException e1) {e1.printStackTrace();}
		
			Set<String> usersSet = new HashSet<String>(activeUsers.keySet());
			
			Iterator<String> itr = usersSet.iterator();
			
			while(itr.hasNext())
			{
				String userName = itr.next();
				//try {usrSemHashSemaphore.acquire();} catch (InterruptedException e) {e.printStackTrace();}
				try {userSemaphores.get(userName).acquire();} catch (InterruptedException e) {e.printStackTrace();}
				//usrSemHashSemaphore.release();
				
				try {onlineUsersSemaphore.acquire();} catch (InterruptedException e) {e.printStackTrace();}
				User tempUser = activeUsers.get(userName);
				onlineUsersSemaphore.release();
				
				//If the user has been idle for 15 seconds or more, close their socket which will force the graceful removal of the user from the system via the receiver thread's IO exception handler
				System.out.println("Time user \"" + userName + "\" has been idle: " + (System.currentTimeMillis() - tempUser.getLastActive()));
				
				if(System.currentTimeMillis() - tempUser.getLastActive() >= 15000)
				{
					try {tempUser.getRecieveThread().getSocket().close();} catch (IOException e) {e.printStackTrace();}
				}
				
				try {usrSemHashSemaphore.acquire();} catch (InterruptedException e) {e.printStackTrace();}
				userSemaphores.get(userName).release();
				usrSemHashSemaphore.release();
			}
			onlineUsersSemaphore.release();
			usrSemHashSemaphore.release();
		}
	}
}
