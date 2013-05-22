package v13;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;


public class MessageServer extends JFrame{
	  // Text area for entering server text
	  private JTextArea jtaServer = new JTextArea();

	  // Text area for displaying client text
	  private JTextArea jtaClient = new JTextArea();
	  
      // Create an input stream from the socket
      ObjectInputStream inputFromClient;
      // Create an input stream from the socket
      ObjectOutputStream outputToClient ;


  public MessageServer() {

      setLayout(new GridLayout(2, 1));
	  JScrollPane jScrollPane1 = new JScrollPane(jtaServer);
	  JScrollPane jScrollPane2 = new JScrollPane(jtaClient);
	  jScrollPane1.setBorder(new TitledBorder("Server"));
	  jScrollPane2.setBorder(new TitledBorder("Client"));
	  add(jScrollPane1, BorderLayout.CENTER);
	  add(jScrollPane2, BorderLayout.CENTER);

	  jtaServer.setWrapStyleWord(true);
	  jtaServer.setLineWrap(true);
	  jtaClient.setWrapStyleWord(true);
	  jtaClient.setLineWrap(true);
	  jtaClient.setEditable(false);
	  setTitle("Chat Server");
	  setSize(500, 300);
	  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  setLocationRelativeTo(null); // Center the frame
	  setVisible(true); // It is necessary to show the frame here!

	  try {
		      // Create a server socket
		      ServerSocket serverSocket = new ServerSocket(8000);
		      System.out.println("Server started ");

		      // Listen for a new connection request
		      Socket socket = serverSocket.accept();

	          // Find the client's host name, and IP address
	          InetAddress inetAddress = socket.getInetAddress();
	          System.out.println("Client connected with IP: " +  inetAddress.getHostAddress() + "\n");
	          // Create a new thread for reading from the client
	          ReadFromClient task1 = new ReadFromClient(socket);

	          // Start the thread
	          new Thread(task1).start();
	          
		      outputToClient = new ObjectOutputStream(socket.getOutputStream());

	  }
	  catch(IOException ex) {
		      ex.printStackTrace();
	  }
	    	
	  jtaServer.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent e) {
	          if (e.getKeyCode() == e.VK_ENTER) {
	        	Message t1 = new Message(jtaServer.getText());
	        	try
	        	{
	        		outputToClient.writeObject(t1);
	        	}
	    		 
 	    	  	catch(Exception ex) {
 	    	  		System.err.println(ex);
 	    	  	}
	        	
	          }
	        }
	      });
   }
	  
    private class ReadFromClient implements Runnable {
        private Socket socket; // A connected socket
        private int number;

        /** Construct a thread */
        public ReadFromClient(Socket socket) {
          this.socket = socket;
       }

        /** Run a thread */
        public void run() {
          try {
            // Create data input and output streams

            // Create an input stream from the socket
            ObjectInputStream inputFromClient;

            inputFromClient =
          	            new ObjectInputStream(socket.getInputStream());

            // Continuously serve the client      
     	      while (true) {

     		        // Read from input
     	    	  try {

     	    		  	Message message = (Message)inputFromClient.readObject();
        	    		jtaClient.setText(message.getMessage());
        	    		Thread.sleep(100);
     	    		 
     	    	  }
     	    	  	catch(Exception e) {
     	          System.err.println(e);
     	          }
     		        //jtfMessagefromClient.(message.getMessage() + '\n');

     		      }
      

          }
          catch(IOException e) {
            System.err.println(e);
          }
        }
      }
    public static void main(String[] args) {
        new MessageServer();
      }

  }







