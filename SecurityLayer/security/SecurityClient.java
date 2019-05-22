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

package security;

import java.net.*; 
import java.io.*;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author gfoster
 */
public class SecurityClient extends Thread {
    // initialize socket and input output streams 
    private Socket       socket    = null; 
    private Scanner      input     = null; 
    private PrintWriter  output    = null;
    private Scanner      userEntry = null;
    
    private final String address;
    private final int    port;
    private boolean      authenticated = false;
    private boolean      rejected = false;
// constructor to put ip address and port 
    public SecurityClient(String address, int port) 
    {
        this.address = address;
        this.port = port;
    } // end of constructor SecurityClient
    
    public void run(){
        // establish a connection 
        try
        { 
            socket = new Socket(address, port); 
            System.out.println("CLIENT:>Connected");
            // takes input from terminal
            userEntry = new Scanner(System.in);
            // sends output to the socket 
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(),true); 
        } 
        catch(UnknownHostException u) 
        { 
            System.out.println(u); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        }   
        KnownCommands command;
        do {
            // Wait for authentication request
            command = KnownCommands.getCommand(input.nextLine());
            switch (command) {
                case TOKEN:
                    System.out.println("CLIENT:>" + command); 
                    giveToken();
                    break;
                case PASSWORD:
                    System.out.println("CLIENT:>" + command);
                    givePassword();
                    break;
                case AUTHENTICATED:
                    authenticated = true;
                    System.out.println("CLIENT:>" + command);
                    break;
                case EXIT:
                    rejected = true;
                    System.out.println("CLIENT:>" + command); 
                    break;
                default:
                    System.out.println("CLIENT:>" + command); 
                    break;
            }
        } while (command != KnownCommands.EXIT && command != KnownCommands.AUTHENTICATED);
        System.out.println("CLIENT:>Last response was: " + command); 
        

//        try
//        { 
//            input.close(); 
//            output.close(); 
//            socket.close(); 
//        } 
//        catch(IOException i) 
//        { 
//            System.out.println(i); 
//        } 
    } 
  
    public boolean isAuthenticated() {return authenticated;}
    public boolean isRejected() {return rejected;}
    
    void  giveToken(){
        // TODO Get token file and send to server 
        boolean haveToken = false;
        if (haveToken){
            // send token
        } else {
            output.println("NO TOKEN AVAILABLE");
        } // end haveToken
              
    } // end of giveToken
    
    void givePassword(){
        KnownCommands command; 
        output.println(passwordRead());
        command = KnownCommands.getCommand(input.nextLine());
        System.out.println("CLIENT:>" + command);
        if (command == KnownCommands.AUTHENTICATED){
            String token = input.nextLine();
            tokenWrite(token);
            System.out.println("CLIENT:>" + token);
        }
    } //end of give password
    
     private String passwordRead(){
        File data = new File("OneUsePassword.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(data);
        } catch (FileNotFoundException ex) {
            String workingDir = "Current working directory: " + System.getProperty("user.dir");
            Logger.getLogger("Security Server").log(Level.SEVERE, workingDir, ex);
            return "";
        }
        
        while (scanner.hasNext()){
           String password = scanner.next();
           return password;
        }
        scanner.close();
        return null;
    } // end of method readPassword()

    public void exit(){
        output.println(KnownCommands.EXIT);
    } // end of method exit()
    
    public String insertDatabase(String sql){
        String response;
        response = input.next();
        System.out.println("CLIENT:> " + response);
        output.println(KnownCommands.INSERT);
        System.out.println("CLIENT:> " + KnownCommands.INSERT);
        output.println(sql);
        System.out.println("CLIENT:>" + sql);
        response = input.nextLine();
        System.out.println("CLIENT:>" + response);
        return response;
    } // end of method insertDatabase()
    
     private void tokenWrite(String token){
    	System.out.println( "Token received: " + token);
    	String fileName = "data/MyToken.txt";
        // write the new token to the end of the knownTokens file
        FileWriter fw;
        try {
            fw = new FileWriter(fileName,true);
            fw.write(token);
            fw.close();
        } catch (IOException ex) {
            String workingDir = "Current working directory: " + System.getProperty("user.dir");
            Logger.getLogger(SecurityHandler.class.getName()).log(Level.SEVERE, workingDir, ex);
        }
    } // end of method writeToken()
    
    public static void main(String args[]) 
    { 
        // TODO IP address & port number needs to be stored in a config file
        try(FileInputStream f = new FileInputStream("db.properties")) {
            // load the properties file
            Properties prop = new Properties();
            prop.load(f);

            // assign db parameters
            int port     = Integer.parseInt(prop.getProperty("port"));
            String IPAddr  = prop.getProperty("IP");
            Thread client = new SecurityClient(IPAddr, port);
            client.start();
        } catch(IOException e) {
           System.out.println(e.getMessage());
        }
    } 
    
}
