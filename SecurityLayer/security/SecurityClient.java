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
  
        String response;
        do {
            // Wait for authentication request
            response = input.nextLine();
            switch (response) {
                case "TOKEN":
                    System.out.println("CLIENT:>" + response); 
                    giveToken();
                    break;
                case "PASSWORD":
                    System.out.println("CLIENT:>" + response);
                    givePassword();
                    break;
                case "AUTHENTICATED":
                    authenticated = true;
                    System.out.println("CLIENT:>" + response); 
                    break;
                default:
                    System.out.println("CLIENT:>" + response); 
                    break;
            }
        } while (!response.equals("EXIT") && !response.equals("AUTHENTICATED"));
        System.out.println("CLIENT:>Last response was: " + response); 
        

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
        String response; 
        output.println("This is my password");
        response = input.nextLine();
        System.out.println("CLIENT:>" + response);
        if (response.equals("AUTHENTICATED")){
            response = input.nextLine();
            System.out.println("CLIENT:>" + response);
        }
    } //end of give password

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
    } // end of method updateDatabase()
    
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
