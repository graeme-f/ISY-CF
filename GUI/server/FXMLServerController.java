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

package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import utility.DatabaseConnector;
import utility.Encrypt;

/**
 *
 * @author gfoster
 */
public class FXMLServerController {

    @FXML private TextArea details;
    private DatabaseConnector dc;
    private String salt = "ISY";
    
    @FXML private void onCheckDatabase(Event event) {
        details.setText("Attempting to connect to the database, please be patient\n");
        dc = DatabaseConnector.getInstance();
        String error = dc.connect();
        if (error.isEmpty()){
            details.appendText("Connection to the database has been established.\n");
        }else {
            details.setText(error);
        }
    }

    @FXML private void onGeneratePassword(Event event) {
        String base = LocalDateTime.now().toString();
        String password = Encrypt.getMD5Hash(Encrypt.salt(base, this.salt), true) + "\n";
        details.setText(password);
        // Append to the password file
        String fileName = "data/OneUsePasswords.txt";
        FileWriter fw;
        try {
            fw = new FileWriter(fileName, true);
            fw.write(password);
            fw.close();
            details.appendText("Password written to the master file...\n");
        } catch (IOException e) {
            String workingDir = "Current working directory: " + System.getProperty("user.dir");
            details.setText(workingDir + e.getMessage());
        }
        fileName = "OneUsePassword.txt";
        try {
            fw = new FileWriter(fileName);
            fw.write(password);
            fw.close();
            details.appendText("Password written to the user file.\nPlease move this file to a safe place to give to the user, before running this again.\n");
        } catch (IOException e) {
            String workingDir = "Current working directory: " + System.getProperty("user.dir");
            details.setText(workingDir + e.getMessage());
        }
    }

    @FXML private void onViewPassword(Event event) {
        String fileName = "data/OneUsePasswords.txt";
        File data = new File(fileName);
        Scanner scanner;
        try {
            scanner = new Scanner(data);
        } catch (FileNotFoundException e) {
            String workingDir = "Current working directory: " + System.getProperty("user.dir");
            details.setText(workingDir + e.getMessage());
            return;
        }
        String passwordData = "";
        while (scanner.hasNext()){
            String line = scanner.next();
            passwordData += line +'\n';
        }
        scanner.close();       
        details.setText(passwordData);
    }

    @FXML private void onClearPassword(Event event) {
        onViewPassword(event);
        String fileName = "data/OneUsePasswords.txt";
        FileWriter fw;
        try {
            fw = new FileWriter(fileName);
            fw.write("");
            fw.close();
            details.appendText("\nMaster password has now been cleared\n");
        } catch (IOException e) {
            String workingDir = "Current working directory: " + System.getProperty("user.dir");
            details.setText(workingDir + e.getMessage());
        }

    }

    @FXML private void onCheckTokenFile(Event event) {
        
    }

    @FXML private void onDeleteToken(Event event) {
        
    }

    @FXML private void onCheckLogFile(Event event) {
        
    }

    @FXML private void onCheckConnections(Event event) {
        
    }

    @FXML private void onSummaryReport(Event event) {
        
    }

    public void initialize(URL url, ResourceBundle rb) {
        
    }
} // end of class FXMLServerController
