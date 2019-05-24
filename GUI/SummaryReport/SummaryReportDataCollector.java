package SummaryReport;

import utility.DataCollector;

import java.sql.*;
import java.util.Calendar;

public class SummaryReportDataCollector extends DataCollector {

    private  SummaryReportDataCollector() {
        super();
    }




    public double[][] getElectricityCF(int month) {

        double total[][] = new double[3][13];

        /*first array is 0:Electricity, 1:Generator, 2:Air Conditioners
        second array is months, 0:July, 11: June, 12: Total for Year*/

        int schoolyear = Calendar.getInstance().get(Calendar.YEAR);
        // If it is between January and June subtract a year
        if (Calendar.getInstance().get(Calendar.MONTH) < 7) {
            schoolyear--;
        }
        String monthSql[] = new String[13];
        for (int i = 0; i < 13; i++) {
            if (i<3) {
                monthSql[i] = "Start_Date Like " + schoolyear + "-0" + (i+7) + "%%%";
            } else if (i<6) {
                monthSql[i] = "Start_Date Like " + schoolyear + "-" + (i + 7) + "%%%";
            } else {
                monthSql[i] = "Start_Date Like " + schoolyear + "-" + (i - 5) + "%%%";;
            }
        }

        try {
            // The SQL query to be used
            ResultSet results;
            for (int i = 0; i < 13; i++) {

                if (i != 12){
                    results = doQuery("select Meter_Units where " + monthSql[i] + " From Electricity");

                    //query for kwh from database
                    while (results.next()) {
                        total[0][i] = ((double) results.getInt("Meter_Units") * 0.075716);
                    }
                } else {
                    results = doQuery("select Meter_Units where " + getBetweenSchoolYear() + " From Electricity");
                    while (results.next()) {
                        total[0][i] = ((double) results.getInt("Meter_Units") * 0.075716);
                    }
                }

                if (i != 12) {
                    results = doQuery("select Amount where " + monthSql[i] + " From Generator");
                    //query for liters from database
                    while (results.next()) {
                        total[1][i] = ((double) results.getInt("Amount")*2.72);
                    }
                } else {
                    results = doQuery("select Amount where " + getBetweenSchoolYear() + " From Generator");
                    while (results.next()) {
                        total[1][i] = ((double) results.getInt("Amount") * 2.72);
                    }
                }

                if (i != 12) {
                    results = doQuery("select Number*Multiplier ACCF where " + monthSql[i] + " From AC_Type" +
                            "inner join AC_CO2 using(AC_Type_ID)");
                    //query for ac info from database
                    while (results.next()) {
                        total[2][i] = (double) results.getInt("ACCF");
                    }
                } else {
                    results = doQuery("select Number*Multiplier ACCF where " + getBetweenSchoolYear() + " From AC_Type" +
                            "inner join AC_CO2 using(AC_Type_ID)");
                    while (results.next()) {
                        total[2][i] = (double) results.getInt("Amount");
                    }
                }
            }
        } catch (SQLException s){
            System.out.println("SQL error: "
                    + s.toString() + " "
                    + s.getErrorCode() + " "
                    + s.getSQLState());
        } catch (Exception e){
            System.out.println("Error: " + e.toString() + e.getMessage());
        }
        return total;
    } //end of method getElectricityCF

    public double getConsumableCF() {
        double total = 0;
        double meat[] = new double[4];
        ResultSet meatResults = doQuery("select Amount, Type where " + getBetweenSchoolYear()
                + " From Consumables ");
        try {
            while (meatResults.next()){
                if (meatResults.getString("Type").equals("Chicken")){
                    meat[0] = meatResults.getInt("Amount");
                } else if(meatResults.getString("Type").equals("Beef")){
                    meat[1] = meatResults.getInt("Amount");
                }
                else if(meatResults.getString("Type").equals("Pork")){
                    meat[2] = meatResults.getInt("Amount");
                }
                else if(meatResults.getString("Type").equals("Fish")){
                    meat[3] = meatResults.getInt("Amount");
                }
            }
        } catch (SQLException s){
            System.out.println("SQL error: "
                    + s.toString() + " "
                    + s.getErrorCode() + " "
                    + s.getSQLState());
        } catch (Exception e){
            System.out.println("Error: " + e.toString() + e.getMessage());
        }
        return total;
    }


}
