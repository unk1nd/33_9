package v13;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class MessageClient extends JFrame {
  private JTextField jtfName = new JTextField(12);
  private JTextArea jtaServer = new JTextArea();
  private JTextArea jtaClient = new JTextArea();

  // Button for sending a student to the server
  private JButton jbtRegister = new JButton("Send");

  // Indicate if it runs as application
  private boolean isStandAlone = false;

  // Host name or ip
  String host = "localhost";
  // Establish connection with the server
  Socket socket;

  // Create an output stream to the server
  ObjectOutputStream toServer ;
  ObjectInputStream inputFromServer;

  public MessageClient() {

    setLayout(new GridLayout(2, 1));
    JScrollPane jScrollPane1 = new JScrollPane(jtaServer);
    JScrollPane jScrollPane2 = new JScrollPane(jtaClient);
    jScrollPane1.setBorder(new TitledBorder("Server"));
    jScrollPane2.setBorder(new TitledBorder("Client"));
    add(jScrollPane2, BorderLayout.CENTER);
    add(jScrollPane1, BorderLayout.CENTER);

    jtaServer.setWrapStyleWord(true);
    jtaServer.setLineWrap(true);
    jtaClient.setWrapStyleWord(true);
    jtaClient.setLineWrap(true);
    jtaServer.setEditable(false);

    setTitle("Chat Client");
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null); // Center the frame
    setVisible(true); // It is necessary to show the frame here!
    
   
    try {
        // Establish connection with the server
         socket = new Socket(host, 8000);

        // Create an output stream to the server
         toServer =
          new ObjectOutputStream(socket.getOutputStream());


      }
      catch (IOException ex) {
        System.err.println(ex);
      }
      catch (Exception ex) {
          System.err.println(ex);
        }
    // Create a new thread for reading from the server
    
    readFromServer task1 = new readFromServer(socket);

    // Start the threads
    new Thread(task1).start();
    
    //Handle input
    jtaClient.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == e.VK_ENTER) { 
        	Message t1 = new Message(jtaClient.getText());
        	try
        	{
        		toServer.writeObject(t1);
        	}
    		 
	    	  	catch(Exception ex) {
	    	  		System.err.println(ex);
	    	  	}
        	
          }
        }
      });

  }
 
  private class readFromServer implements Runnable {
      private Socket socket; // A connected socket
      private int number;

      /** Construct a thread */
      public readFromServer(Socket socket) {
        this.socket = socket;
     }

      /** Run a thread */
      public void run() {
        try {
	
	          // Create an input stream from the socket
	          inputFromServer=
	                  new ObjectInputStream(socket.getInputStream());
	
	          // Continuously serve the client      
	   	      while (true) {
	
	   	    	  try {
	
	   	    			Message message = (Message)inputFromServer.readObject();
	      	    		jtaServer.setText(message.getMessage());
	   	    		 
	   	    	  }
	   	    	  	catch(Exception e) {
	   	          System.err.println(e);
	   	          }
	
	   		   }
    
        }
        catch(IOException e) {
          System.err.println(e);
        }
      }
    }
  
  public static void main(String[] args) {

	  new MessageClient();

  }
}
