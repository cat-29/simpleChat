// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
	
	ChatIF serverUI;
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  
  public void handleMessageFromServerUI(String message)
  {
    	if (message.startsWith("#")) {
    		handleCommand(message);
    	}
    	else {
    		sendToAllClients(message);
    	}
    }
  
  private void handleCommand (String cmd) {
	  if (cmd.equals("#quit")) {
		  serverUI.display("The server will quit");
		  quit();
	  }
	  else if (cmd.equals("#stop")) {
			 if(this.isListening()) {
				this.stopListening();
			 }
	  }
	  else if (cmd.equals("#close")) {
		  stopListening();
		  quit();
	  }
	  else if (cmd.equals("#start")) {
		  if (!this.isListening()) {
			  this.run();
		  }
	  }
	  else if (cmd.equals("#getport")) {
		  serverUI.display(Integer.toString(this.getPort()));
	  }
	  else if (cmd.contains("#setport")) {
		  int port = Integer.parseInt(cmd.split(" ")[-1]);
		  if (!this.isListening()) {
			 this.setPort(port); 
		  }
		  else {
			  serverUI.display("ERREUR - Le serveur est toujours connecté.");
		  }
	  }
  }
  
  public void quit()
  {
    try
    {
      close();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  
  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("Client s'est connecté.");
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("Client s'est déconnecté.");
  }
}
//End of EchoServer class
