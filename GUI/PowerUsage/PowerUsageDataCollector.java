package PowerUsage;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

public class PowerUsageDataCollector {

    class Electricity {
        int id;
        LocalDate startDate;
        LocalDate endDate;
        int meterUnits;
    } // end of inner class Electricity



    class Generator {
        int id;
        LocalDate startDate;
        LocalDate endDate;
        int amount;
    } // end of inner class Generator

    private static PowerUsageDataCollector singleInstance = null;
    private Connection conn = null;
    private HashMap<Integer, Electricity> electricityDetails;
    private int currentYear;
    private int previousYear;

    // Singleton
    public static PowerUsageDataCollector getInstance()
    {
        if (singleInstance == null)
            singleInstance = new PowerUsageDataCollector();

        return singleInstance;
    } // end of method getInstance

    // This runs when the instance is created.
    private PowerUsageDataCollector() {
        initialize();
    } // end of method PowerUsageDataCollector

    // Initialize
    private void initialize() {
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        previousYear = currentYear-1;
    } // end of method initialize

    private void getAllElecrity() {
        electricityDetails = new HashMap<>();
        try {
            Statement st = conn.createStatement();
            String query = "SELECT Electricity_ID, Start_Date, End_Date, Meter_Units FROM Electricity WHERE StartDate BETWEEN \"" + previousYear + "-07-01%\" AND \"" + currentYear + "-06-30%\"";
            ResultSet rec = st.executeQuery(query);

        } catch (SQLException s) {
            System.out.println("SQL error: "
                    + s.toString() + " "
                    + s.getErrorCode() + " "
                    + s.getSQLState());

        } catch (Exception e) {
            System.out.println("Error: " + e.toString() + e.getMessage());
        } //hello
    } // end of method getAllElectricity

    private void connect() {
        try(FileInputStream f = new FileInputStream("db.properties")) {
            // load the properties file
            Properties prop = new Properties();
            prop.load(f);

            // assign db parameters
            String url       = prop.getProperty("url");
            String user      = prop.getProperty("user");
            String password  = prop.getProperty("password");
            // create a connection to the database
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to the database has been established.");
        } catch(SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    } // end connect method
}
