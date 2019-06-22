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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import utility.DatabaseConnector;
import utility.Encrypt;
import utility.LogFile;

/**
 *
 * @author gfoster
 */

public class SecurityServer {

    static final LogFile logger = new LogFile(SecurityServer.class.getName());
    private ServerSocket server;
    private String salt;
    private InetAddress hostIP;
    
    // constructor with port 
    public SecurityServer(int port, String salt) throws IOException 
    { 
        server = new ServerSocket(port); 
        System.out.println("Server started"); 
    } // end of constructor
    
    private InetAddress getHostIPAddr(){
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            logger.log(Level.SEVERE,ex.getLocalizedMessage());
            return null;
        }
    }
    public void run(){
        while (true) {
            Socket socket;
            hostIP = getHostIPAddr();
            // starts server and waits for a connection 
            try
            { 
                if (null == hostIP){
                    System.out.println("Waiting for a client ...");
                } else {
                    System.out.println("Waiting for a client on  " + hostIP.getHostAddress() + " ...");
                }

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
           String workingDir = "Current working directory: " + System.getProperty("user.dir");
           logger.log(Level.SEVERE, workingDir);
           logger.log(Level.SEVERE, e.getMessage());
        }
    } // end of method main()

} // end of class SecurityServer
            
// SecurityHandler class 
class SecurityHandler extends Thread {
    
    static final LogFile logger = SecurityServer.logger;
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
    
    private DatabaseConnector dc;
    
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
        dc = DatabaseConnector.getInstance();
    } // end of SecurityHandler constructor()

    

    
    private boolean tokenMatch(String token){
        String hashToken = Encrypt.getMD5Hash(Encrypt.salt(token, this.salt), DEBUG);
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
        } catch (FileNotFoundException e) {
            String workingDir = "Current working directory: " + System.getProperty("user.dir");
            logger.log(Level.SEVERE, workingDir);
            logger.log(Level.SEVERE, e.getMessage());
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
        } catch (FileNotFoundException e) {
            String workingDir = "Current working directory: " + System.getProperty("user.dir");
            logger.log(Level.SEVERE, workingDir);
            logger.log(Level.SEVERE, e.getMessage());
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
            } catch (IOException e) {
                String workingDir = "Current working directory: " + System.getProperty("user.dir");
                logger.log(Level.SEVERE, workingDir);
                logger.log(Level.SEVERE, e.getMessage());
            }
        } // end match was found so remove it from the file
        
        return match;
    } // end of method passwordMatch()
    
    private String tokenGenerate(String password){
        String hashToken = Encrypt.getMD5Hash(Encrypt.salt(password, this.remoteIP.toString()),DEBUG);
        tokenWrite(hashToken, this.remoteIP.toString());
        return hashToken;
    } // end of method generateToken()
    
    private void tokenWrite(String newToken, String IPAddr){
    	String hashToken = Encrypt.getMD5Hash(Encrypt.salt(newToken, this.salt), DEBUG);

        // Read the newToken file and delete any line that matches with the IP address
    	String fileName = "data/knownTokens.txt";
        File data = new File(fileName);
        Scanner scanner;
        try {
            scanner = new Scanner(data);
        } catch (FileNotFoundException e) {
            String workingDir = "Current working directory: " + System.getProperty("user.dir");
            logger.log(Level.SEVERE, workingDir);
            logger.log(Level.SEVERE, e.getMessage());
            return;
        }
        String hashData = "";
        while (scanner.hasNext()){
            String IP = scanner.next();
            String token = scanner.next();
            
            if (!IP.equals(IPAddr)) {
                hashData += IP + "\t" + token +'\n';
            }
        }
        scanner.close();
        
    	System.out.println(IPAddr + ": " + newToken + " - " + hashToken);
        // write the new newToken to the end of the knownTokens file
        FileWriter fw;
        try {
            fw = new FileWriter(fileName,true);
            fw.write(hashData);
            fw.write(IPAddr + "\t"+ hashToken + "\n");
            fw.close();
        } catch (IOException e) {
            String workingDir = "Current working directory: " + System.getProperty("user.dir");
            logger.log(Level.SEVERE, workingDir);
            logger.log(Level.SEVERE, e.getMessage());
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
        String cmd;
        cmd = in.nextLine();
        System.out.println(cmd);
        KnownCommands command = KnownCommands.getCommand(cmd);
        switch (command) {
            case EXIT:
                return true;
            // INSERT data into the table
            case INSERT:
                String sql = in.nextLine();
                System.out.println(sql);
                String error = dc.connect();
                if (error.isEmpty()){
                    logger.log(Level.INFO, "Connection to the database has been established.");
                }else {
                    logger.log(Level.SEVERE, error);
                }
                // Check that the sql is an insert statement
                // Log the statement to the file logger
                logger.log(Level.INFO, "{0}:{1}", new Object[]{remoteIP, sql});
                // Perform the statement
                Statement st = dc.getStatement();
                int rec;
                try {
                    rec = st.executeUpdate(sql);
                }catch (SQLException e) {
                   logger.log(Level.INFO,e.getMessage());
                   return false;
                }
                if (rec == 1){
                    logger.log(Level.INFO, "One record inserted.");                        
                } else {
                    logger.log(Level.INFO, "{0} records inserted.", rec);
                }
                out.println("Record inserted. ");
                dc.close();
                return true;
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
