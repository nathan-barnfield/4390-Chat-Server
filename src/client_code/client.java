public class Client{

	private String name;
	private String clientID;
	private int key;

	Client (){
	}

	public void sendLogin(){
		// send "Hello" msg to SERVER
	}

	public void chatRequest(String client){
		// request connection with 2nd chat client

		System.out.println ("Chat Started with " + client);
	}

	public void sendMSG(String msg){
		// send message to
	}

	public void rcvMSG(Datagram msg){
		// switch (msg)??
			//case 1: 

			//case 2: 

			//case 3: ENDMSG
				// endChat();

			//case 4: EXIT
				// sendQuit();
	}

	public void endChat(){
			// end chat with 2nd client, but still maintain connection with UDP.
	}

	public void sendQuit(){
		// disconnect from server when user types "Log off"
	}



}