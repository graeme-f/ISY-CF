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

import java.net.*; 
import java.io.*; 
import java.util.Scanner;
/**
 *
 * @author gfoster
 */
public class SecurityClient {
    // initialize socket and input output streams 
    private Socket       socket    = null; 
    private Scanner      input     = null; 
    private PrintWriter  output    = null;
    private Scanner      userEntry = null;
// constructor to put ip address and port 
    public SecurityClient(String address, int port) 
    { 
        // establish a connection 
        try
        { 
            socket = new Socket(address, port); 
            System.out.println("Connected"); 
  
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
                    System.out.println(response); 
                    giveToken();
                    break;
                case "PASSWORD":
                    System.out.println(response);
                    givePassword();
                    break;
                case "AUTHENTICATED":
                    System.out.println(response); 
                    break;
                default:
                    System.out.println(response); 
                    break;
            }
        } while (!response.equals("EXIT"));
        System.out.println("Last response was: " + response); 
        

        try
        { 
            input.close(); 
            output.close(); 
            socket.close(); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
    } 
  
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
        System.out.println(response);
        if (response.equals("AUTHENTICATED")){
            response = input.nextLine();
            System.out.println(response);
        }
    } //end of give password

    
    public static void main(String args[]) 
    { 
        // TODO IP address & port number needs to be stored in a config file
        SecurityClient client = new SecurityClient("172.168.101.213", 5050); 
    } 
    
}
