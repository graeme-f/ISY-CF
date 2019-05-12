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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author gfoster
 */
public class SecurityServer {

    private ServerSocket server;
    private String salt;
    
    // constructor with port 
    public SecurityServer(int port, String salt) throws IOException 
    { 
        server = new ServerSocket(port); 
        System.out.println("Server started"); 
    } // end of constructor
    
    public void run(){
        while (true) {
            Socket socket;
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
                Thread t = new SecurityHandler(socket, input, output, salt);
                // start the thread
                t.start();
            }
            catch (IOException e){
                e.printStackTrace(); 
            }
        } // end infinite loop
    } // end of method run
    
      
    public static void main(String args[]) throws IOException 
    { 
        try(FileInputStream f = new FileInputStream("db.properties")) {
            // load the properties file
            Properties prop = new Properties();
            prop.load(f);

            // assign db parameters
            int port     = Integer.parseInt(prop.getProperty("port"));
            String salt  = prop.getProperty("salt");
            SecurityServer server = new SecurityServer(port, salt);
            server.run();
        } catch(IOException e) {
           System.out.println(e.getMessage());
        }
    } // end of method main()

} // end of class SecurityServer
            
// SecurityHandler class 
class SecurityHandler extends Thread {
    
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd"); 
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    
    String salt;
    final Scanner in; 
    final PrintWriter out; 
    final Socket s;
    final boolean DEBUG = true;
    boolean authenticated;
    int authenticateCount;
    InetAddress remoteIP;
    
    // Constructor 
    public SecurityHandler(Socket s,
                           Scanner input,
                           PrintWriter output,
                           String salt)  
    { 
        this.s = s; 
        this.in = input; 
        this.out = output;
        this.authenticated = false;
        this.remoteIP = s.getInetAddress();
        this.authenticateCount = 0;
        this.salt = salt;
    } // end of SecurityHandler constructor()

    
    /**
     * Returns a hexadecimal encoded MD5 hash for the input String.
     * @param data
     * @return a MD5 hash
     */
    private String getMD5Hash(String data) {
        String result = null;
        String algorithm = "MD5";
        String encoding = "UTF-8";
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hash = digest.digest(data.getBytes(encoding));
            return DatatypeConverter.printHexBinary(hash); // make it printable
        }catch(NoSuchAlgorithmException  ex) {
            System.out.println("Unknown algorithm " + algorithm);
            if (DEBUG) ex.printStackTrace();
        }catch (UnsupportedEncodingException ex) {
            System.out.println("Unknown Encoding " + encoding);
            if (DEBUG) ex.printStackTrace();
        }
        return result;
    } // end of method getMD5Hash()
    
    private String salt(String token, String salt){
        return salt + token + salt;
    } // end of method salt()
    
    private boolean tokenMatch(String token){
        String hashToken = getMD5Hash(salt(token, this.salt));
        // Get token from file
        String fileToken = tokenRead(this.remoteIP.toString());

        System.out.println(remoteIP);
        System.out.println(hashToken);
        System.out.println(fileToken);

        // compare fileToken with hashToken
        if (hashToken.equals(fileToken)){
            return true;
        }
        return false;
    } // end of method tokenMatch()
    
    private String tokenRead(String IPAddr){
        // Read in the data from the text file
        File data = new File("data/knownTokens.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(data);
        } catch (FileNotFoundException ex) {
            String workingDir = "Current working directory: " + System.getProperty("user.dir");
            Logger.getLogger("JavaApp01Average").log(Level.SEVERE, workingDir, ex);
            return "";
        }
        
        while (scanner.hasNext()){
            String IP = scanner.next();
            String token = scanner.next();
            if (IP.equals(IPAddr))
                return token;
        }
        scanner.close();
        return null;
    } // end of method readToken()
    
    private boolean passwordMatch(String password){
        // Read the password file
        String fileName = "data/OneUsePasswords.txt";
        File data = new File(fileName);
        Scanner scanner;
        boolean match = false;
        try {
            scanner = new Scanner(data);
        } catch (FileNotFoundException ex) {
            String workingDir = "Current working directory: " + System.getProperty("user.dir");
            Logger.getLogger(SecurityHandler.class.getName()).log(Level.SEVERE, workingDir, ex);
            return false;
        }
        
        String passwordData = "";
        while (scanner.hasNext()){
            String line = scanner.next();
            System.out.println(line + " - " + password);
            if (line.equals(password)){
                match = true;
            } else {
                passwordData += line +'\n';
            }
        }
        scanner.close();
        
        // write the password back to the file except for the matched password
        if (match){
            FileWriter fw;
            try {
                fw = new FileWriter(fileName);
                fw.write(passwordData);
                fw.close();
            } catch (IOException ex) {
                String workingDir = "Current working directory: " + System.getProperty("user.dir");
                Logger.getLogger(SecurityHandler.class.getName()).log(Level.SEVERE, workingDir, ex);
            }
        } // end match was found so remove it from the file
        
        return match;
    } // end of method passwordMatch()
    
    private String tokenGenerate(String password){
        String hashToken = getMD5Hash(salt(password, this.remoteIP.toString()));
        tokenWrite(hashToken, this.remoteIP.toString());
        return hashToken;
    } // end of method generateToken()
    
    private void tokenWrite(String token, String IPAddr){
    	String hashToken = getMD5Hash(salt(token, this.salt));

    	System.out.println(IPAddr + ": " + token + " - " + hashToken);
    	String fileName = "data/knownTokens.txt";
        // write the new token to the end of the knownTokens file
        FileWriter fw;
        try {
            fw = new FileWriter(fileName,true);
            fw.write(IPAddr + "\t"+ hashToken + "\n");
            fw.close();
        } catch (IOException ex) {
            String workingDir = "Current working directory: " + System.getProperty("user.dir");
            Logger.getLogger(SecurityHandler.class.getName()).log(Level.SEVERE, workingDir, ex);
        }
    } // end of method writeToken()
    
    private void authenticate(){
        out.println(KnownCommands.TOKEN);
        String token = in.nextLine();
        System.out.println(token);
        if (tokenMatch(token)){
            out.println(KnownCommands.AUTHENTICATED);
            this.authenticated = true;
        } else {
            out.println(KnownCommands.PASSWORD);
            String password = in.nextLine();
            System.out.println(password);
            if (passwordMatch(password)){
                token = tokenGenerate(password);
                out.println(KnownCommands.AUTHENTICATED);
                out.println(token);
                this.authenticated = true;
            } else {
                out.println(KnownCommands.AUTHENTICATION_FAILED);
                authenticateCount++;
            }
        }
    } // end of method authenticate()

    private boolean getCommand(){
        out.println(KnownCommands.COMMAND);
        KnownCommands command = KnownCommands.getCommand(in.nextLine());
        switch (command) {
            case EXIT:
                return true;
            // INSERT data onto the table
            case INSERT:
                break;
            // UPDATE data already on the table
            case UPDATE:
                break;
            default:
                break;
        }
        return false;
    } // end of method getCommand()

    @Override
    public void run()  
    {
        while (true)  
        {
            if (!authenticated){
                if (authenticateCount>3){
                    out.println(KnownCommands.EXIT);
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
