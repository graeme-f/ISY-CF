package SummaryReport;

import java.sql.*;

public class SummaryReportDataCollector {

    public static Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "database.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    } // end of method connect

    private static void close(Connection conn){
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    } // end of method close

    public static double getElectricityCF(Connection conn) {
        double kilowattHours = 0;
        double generatorCF = 0;
        double[] acUsage = new double[4]; //AC Types: 0:yorkMulti, 1:Mitsubishi, 2:Panasonic, 3:YorkUni


        try {
            Statement st = conn.createStatement();
            // The SQL query to be used
            String query = "select Meter_Units where startDate between something or something From Electricity"; //or something not real at all
            ResultSet results = st.executeQuery(query);
            //query for kwh from database
            while (results.next()){
                kilowattHours += (double) results.getInt("Meter_Units");
            }

            String query1 = "select Amount where startDate between something or something From Generator"; //or something not real at all
            ResultSet genResults = st.executeQuery(query1);
            //query for liters from database
            while (genResults.next()){
                generatorCF += (double) genResults.getInt("Amount");
            }

            String query2 = "select AC_Type_ID where startDate between something or something From AC_Type"; //or something not real at all
            ResultSet acResults = st.executeQuery(query2);
            //query for ac info from database
            while (acResults.next()){
                acUsage[0] += (double) acResults.getInt("Amount");
            }
            close(conn);

        } catch (SQLException s){
            System.out.println("SQL error: "
                    + s.toString() + " "
                    + s.getErrorCode() + " "
                    + s.getSQLState());
        } catch (Exception e){
            System.out.println("Error: " + e.toString() + e.getMessage());
        }


        //equations to calculate carbon footprint in kg co2e
        kilowattHours =  (kilowattHours * 0.075716);
        generatorCF = (generatorCF *2.72);
        double total = kilowattHours + generatorCF + acUsage[0] + acUsage[1] + acUsage[2] + acUsage[3];
        return total;
    }
}
