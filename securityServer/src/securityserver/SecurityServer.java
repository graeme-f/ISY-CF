/*
 * The MIT License
 *
 * Copyright 2019 gfoster.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package securityserver;

import java.net.*; 
import java.io.*; 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author gfoster
 */
public class SecurityServer {

    // constructor with port 
    public SecurityServer(int port) throws IOException 
    { 
        ServerSocket server = new ServerSocket(port); 
        System.out.println("Server started"); 
        while (true) {
            Socket socket = null;
            // starts server and waits for a connection 
            try
            { 

                System.out.println("Waiting for a client ..."); 

                socket = server.accept(); 
                System.out.println("Client accepted"); 

                // takes input from the client socket
                Scanner input = new Scanner(socket.getInputStream());
                PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

                // Create a new thread for the connection
                Thread t = new SecurityHandler(socket, input, output);
                // start the thread
                t.start();
            }
            catch (Exception e){
                if (socket != null)
                    socket.close(); 
                e.printStackTrace(); 
            }
        } // end infinite loop
    } // end of constructor
    
      
    public static void main(String args[]) throws IOException 
    { 
        SecurityServer server = new SecurityServer(5000); 
    } 

} // end of class SecurityServer
            
// SecurityHandler class 
class SecurityHandler extends Thread {
    
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd"); 
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    
    final Scanner in; 
    final PrintWriter out; 
    final Socket s;
    boolean authenticated;
    int authenticateCount;
    InetAddress remoteIP;
    
    // Constructor 
    public SecurityHandler(Socket s, Scanner input, PrintWriter output)  
    { 
        this.s = s; 
        this.in = input; 
        this.out = output;
        this.authenticated = false;
        this.remoteIP = s.getInetAddress();
        this.authenticateCount = 0;
    }

    
    private boolean tokenMatch(String token){
        out.println(remoteIP);
        String MAC = in.nextLine();
        System.out.println(MAC);
        return false;
    }
    
    private boolean passwordMatch(String password){
        
        return false;
    }
    
    private String generateToken(String password){
        return "";
    }
    
    private void authenticate(){
        out.println("TOKEN");
        String token = in.nextLine();
        System.out.println(token);
        if (tokenMatch(token)){
            out.println("AUTHENTICATED");
            this.authenticated = true;
        } else {
            out.println("PASSWORD");
            String password = in.nextLine();
            System.out.println(password);
            if (passwordMatch(password)){
                token = generateToken(password);
                out.println("AUTHENTICATED");
                out.println(token);
                this.authenticated = true;
            } else {
                out.println("FAILED");
                authenticateCount++;
            }
        }
    }

    private boolean getCommand(){
        out.println("COMMAND");
        String command = in.nextLine();
        switch (command) {
            case "EXIT":
                return true;
            // INSERT data onto the table
            case "INSERT":
                break;
            // UPDATE data already on the table
            case "UPDATE":
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void run()  
    {
        while (true)  
        {
            if (!authenticated){
                if (authenticateCount>3){
                    out.println("EXIT");
                    break;
                }
                authenticate();
            } else {
                if (getCommand()){
                    break;
                }
            }
        } 
        this.in.close(); 
        this.out.close(); 
    } // end of method run 
    
} // end of class SecurityHandler
