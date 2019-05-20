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

package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author gfoster
 */
public class DatabaseConnector {
    static DatabaseConnector instance = null;
    
    protected Connection conn = null;
    protected Statement st = null;

    public static DatabaseConnector getInstance(){
        if (instance == null){
            instance = new DatabaseConnector();
        }
        return instance;
    }
    
    protected DatabaseConnector() {
        connect();
    } // end of constructor
    
    public final String connect() {
        Properties prop;
        try{
            FileInputStream f = new FileInputStream("db.properties");
            // load the properties file
            prop = new Properties();
            prop.load(f);
        } catch(IOException e) {
            String error = "Unable to open the file called db.properties."
                    + "This file should be stored in the following directory:\n"
                    + System.getProperty("user.dir")
                    + "\n\n"
                    + e.getMessage();
            return error;
        }

        // assign db parameters
        String url       = prop.getProperty("url");
        String user      = prop.getProperty("user");
        String password  = prop.getProperty("password");
        // create a connection to the database
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to the database has been established.");
        } catch(SQLException e) {
            String error = "Unable to make a connection with the database. "
                    + "The connection details are stored in the file:\n"
                    + System.getProperty("user.dir")+"\\db.properties"
                    + "\n\n"
                    + e.getMessage();
            return error;
        }
        try {
            st = conn.createStatement();
        } catch(SQLException e) {
            String error = "Connection with the database established but "
                    + "the database doesn't appear to be configured correctly."
                    + "\n\n"
                    + e.getMessage();
            return error;
        }
        return "";
    } // end connect method
    
    public Statement getStatement(){
        return st;
    }
    public void close(){
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }       
    } // end of close method

} // end of class DatabaseConnector
