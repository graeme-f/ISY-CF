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
package securityclient;

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
                    // get Token
                    break;
                case "PASSWORD":
                    System.out.println(response); 
                    output.println("This is my password");
                    response = input.nextLine();
                    System.out.println(response);
                    if (response.equals("AUTHENTICATED")){
                        response = input.nextLine();
                        System.out.println(response);
                    }
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
        
//        // string to read message from input 
//        do
//        {
//            System.out.print("Enter message: ");
//            message =  userEntry.nextLine();
//            output.println(message);        //Step 3.
//            response = input.nextLine();    //Step 3.
//            System.out.println("\nSERVER> " + response);
//        }while (!message.equals("***CLOSE***")); 
  
        // close the connection 
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
        // Expect the server to send the IP address
        String IPAddr = input.nextLine();
        output.println(MacAddress(IPAddr));
    } // end of giveToken
    
    String MacAddress(String IPAddr) {
        String macAddr = "";
        try {
            // InetAddress address = InetAddress.getLocalHost();
            System.out.println(IPAddr);
            InetAddress address = InetAddress.getByName(IPAddr);

            /*
             * Get NetworkInterface for the current host and then read
             * the hardware address.
             */
            NetworkInterface ni =  NetworkInterface.getByInetAddress(address);
            if (ni != null) {
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    /*
                     * Extract each array of mac address and convert it
                     * to hexadecimal with the following format
                     * 08-00-27-DC-4A-9E.
                     */
                    for (int i = 0; i < mac.length; i++) {
                        macAddr += String.format("%02X%s",
                            mac[i], (i < mac.length - 1) ? "-" : "");
                    }
                } else {
                    System.out.println("Address doesn't exist or is not " +
                        "accessible.");
                }
            } else {
                System.out.println("Network Interface for the specified " +
                    "address is not found.");
            }
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        return macAddr;
    }

    
    public static void main(String args[]) 
    { 
        // TODO IP address & port number needs to be stored in a config file
        SecurityClient client = new SecurityClient("172.168.101.213", 5000); 
    } 
    
}
