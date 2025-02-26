// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;
import java.util.ArrayList;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if (message.startsWith("#")) {
    		handleCommand(message);
    	}
    	else {
    		sendToServer(message);
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand (String cmd) {
	  if (cmd.equals("#quit")) {
		  clientUI.display("The client will quit");
		  quit();
	  }
	  else if (cmd.equals("#logoff")) {
		 try {
			 if(this.isConnected()) {
				this.closeConnection();
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
	  else if (cmd.equals("#login")) {
		  try {
			  if (!this.isConnected()) {
					 this.openConnection(); 
				  }
			  else {
				  clientUI.display("ERREUR - Le client est déjà connecté.");
				  }
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
	  else if (cmd.equals("#gethost")) {
		  clientUI.display(this.getHost());
	  }
	  else if (cmd.equals("#getport")) {
		  clientUI.display(Integer.toString(this.getPort()));
	  }
	  else if (cmd.contains("#sethost")) {
		  String host = cmd.split(" ")[-1];
		  if (!this.isConnected()) {
			 this.setHost(host); 
		  }
		  else {
			  clientUI.display("ERREUR - Le client est toujours connecté.");
		  }
	  }
	  else if (cmd.contains("#setport")) {
		  int port = Integer.parseInt(cmd.split(" ")[-1]);
		  if (!this.isConnected()) {
			 this.setPort(port); 
		  }
		  else {
			  clientUI.display("ERREUR - Le client est toujours connecté.");
		  }
	  }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  /**
	 * Implementation of the hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  	@Override
	protected void connectionClosed() {
  		clientUI.display("Cette connection s'est arrêté.");
	}

	/**
	 * Implementation of the hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  	@Override
	protected void connectionException(Exception exception) {
  		clientUI.display("Ce serveur s'est arrêté.");
  		System.exit(0);
	}
}
//End of ChatClient class
