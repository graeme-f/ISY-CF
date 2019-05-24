package PowerUsage;

import utility.DataCollector;
import utility.ErrorMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class PowerUsageDataCollector extends DataCollector {

    class Electricity {
        int id;
        LocalDate startDate;
        LocalDate endDate;
        int meterUnits;
    } // end of inner class Electricity

    private LocalDate lastDate;

    class Generator {
        int id;
        LocalDate startDate;
        LocalDate endDate;
        int amount;
    } // end of inner class Generator

    class ACType {
        int id;
        //TODO
        // LocalDate startDate;
        // Months and stuff in the database, the code supports it
        LocalDate startDate;
        LocalDate endDate;
        String description;
        int number;
    }

    private static PowerUsageDataCollector singleInstance = null;
    private HashMap<Integer, Electricity> electricityDetails;
    private HashMap<Integer, Generator> generatorDetails;
    private HashMap<Integer, ACType> acTypeDetails;

    // Singleton
    public static PowerUsageDataCollector getInstance()
    {
        if (singleInstance == null)
            singleInstance = new PowerUsageDataCollector();
        return singleInstance;
    } // end of method getInstance

    // This runs when the instance is created.
    private PowerUsageDataCollector() {
        super();
        lastDate = null;
        getAllElectricity();
        getAllGenerator();
        getAllAcType();
    } // end of method PowerUsageDataCollector

    private void getAllElectricity() {
        electricityDetails = new HashMap<>();
        String query = "SELECT * FROM Electricity WHERE Start_Date " + getBetweenSchoolYear();
        ResultSet rec = doQuery(query);
        Electricity electricity = new Electricity();
        try {
            while (rec.next()) {
                electricity.id = rec.getInt("Electricity_ID");
                electricity.startDate = rec.getDate("Start_Date").toLocalDate();
                electricity.endDate = rec.getDate("End_Date").toLocalDate();
                electricity.meterUnits = rec.getInt("Meter_Units");
                electricityDetails.put(electricity.id, electricity);
                if (lastDate == null) {
                    lastDate = electricity.endDate;
                } else if (electricity.endDate.compareTo(lastDate) > 0){
                    lastDate = electricity.endDate;
                }
            }
        } catch(SQLException error){
                ErrorMessage.display(error.getMessage());
        }
    } // end of method getAllElectricity

    private void getAllGenerator() {
        generatorDetails = new HashMap<>();
        String query = "SELECT * FROM Generator WHERE Start_Date " + getBetweenSchoolYear();
        ResultSet rec = doQuery(query);
        Generator generator = new Generator();
        try {
            while (rec.next()) {
                generator.id = rec.getInt("Generator_ID");
                generator.startDate = rec.getDate("Start_Date").toLocalDate();
                generator.endDate = rec.getDate("End_Date").toLocalDate();
                generator.amount = rec.getInt("Amount");
                generatorDetails.put(generator.id, generator);
                if (lastDate == null) {
                    lastDate = generator.endDate;
                } else if (generator.endDate.compareTo(lastDate) > 0) {
                    lastDate = generator.endDate;
                }
            }
        } catch (SQLException error) {
            ErrorMessage.display(error.getMessage());
        }
    } // end of method getAllGenerator

    private void getAllAcType() {
        generatorDetails = new HashMap<>();
        String query = "SELECT * FROM AC_Type WHERE Start_Date " + getBetweenSchoolYear();
        ResultSet rec = doQuery(query);
        ACType acType = new ACType();
        try {
            while (rec.next()) {
                acType.id = rec.getInt("AC_Type_ID");
                acType.startDate = rec.getDate("Start_Date").toLocalDate();
                acType.endDate = rec.getDate("End_Date").toLocalDate();
                acType.description = rec.getString("Description");
                acType.number = rec.getInt("Number");
                acTypeDetails.put(acType.id, acType);
                if (lastDate == null) {
                    lastDate = acType.endDate;
                } else if (acType.endDate.compareTo(lastDate) > 0) {
                    lastDate = acType.endDate;
                }
            }
        } catch (SQLException error) {
            ErrorMessage.display(error.getMessage());
        }
    } // end of method getAllAcType

    public ArrayList<Integer> getElectricityList(){
        ArrayList<Integer> electricity = new ArrayList<>();
        Set< HashMap.Entry< Integer, PowerUsageDataCollector.Electricity> > st = electricityDetails.entrySet();

        for (HashMap.Entry< Integer, PowerUsageDataCollector.Electricity> me:st)
        {
            electricity.add(me.getKey());
        }
        return electricity;
    } // end of method getElectricityList

    public ArrayList<Integer> getGeneratorList(){
        ArrayList<Integer> generator = new ArrayList<>();
        Set< HashMap.Entry< Integer, PowerUsageDataCollector.Generator> > st = generatorDetails.entrySet();

        for (HashMap.Entry< Integer, PowerUsageDataCollector.Generator> me:st)
        {
            generator.add(me.getKey());
        }
        return generator;
    } // end of method getGeneratorList

    public ArrayList<Integer> getAcTypeList(){
        ArrayList<Integer> acType = new ArrayList<>();
        Set< HashMap.Entry< Integer, PowerUsageDataCollector.ACType> > st = acTypeDetails.entrySet();

        for (HashMap.Entry< Integer, PowerUsageDataCollector.ACType> me:st)
        {
            acType.add(me.getKey());
        }
        return acType;
    } // end of method getAcTypeList


    public HashMap<String, Integer> getElectricityMonthMeterUnits()  {
        HashMap<String, Integer> monthMeterUnits = new HashMap<>();
        for (Electricity e : electricityDetails.values()) {
            String month = getMonth(e.startDate);
            if (monthMeterUnits.containsKey(month)) {
                int subtotal = monthMeterUnits.get(month);
                monthMeterUnits.put(month, subtotal + e.meterUnits);
            } else {
                monthMeterUnits.put(month, e.meterUnits);
            }
        }
        return monthMeterUnits;
    } // end of method getElectricityMonthMeterUnits

    public HashMap<String, Integer> getGeneratorMonthAmount()  {
        HashMap<String, Integer> monthAmount = new HashMap<>();
        for (Generator g : generatorDetails.values()) {
            String month = getMonth(g.startDate);
            if (monthAmount.containsKey(month)) {
                int subtotal = monthAmount.get(month);
                monthAmount.put(month, subtotal + g.amount);
            } else {
                monthAmount.put(month, g.amount);
            }
        }
        return monthAmount;
    } // end of method getGeneratorMonthAmount

    public HashMap<String, Integer> getAcTypeNumber()  {
        HashMap<String, Integer> monthNumber = new HashMap<>();
        for (ACType a : acTypeDetails.values()) {
            String month = getMonth(a.startDate);
            if (monthNumber.containsKey(month)) {
                int subtotal = monthNumber.get(month);
                monthNumber.put(month, subtotal + a.number);
            } else {
                monthNumber.put(month, a.number);
            }
        }
        return monthNumber;
    } // end of method getAcTypeNumber


    public String getMonth(LocalDate date) {
        return date.getMonth().toString();
    } // end of method getMonth

    public LocalDate getLastDate() {
        return lastDate.plusDays(1);
    } // end of method getLastDate


    public String insertElectricityData(String startDate, String endDate, String meterUnits) {
        return insertDatabase("INSERT into Electricity (Start_Date, End_Date, Meter_Units) VALUES(\'" + startDate + "\', " +
                "\'" + endDate + "\', \'" + meterUnits + ")");
    } // end of method insertElectricityData

    public String insertGeneratorData(String startDate, String endDate, String amount) {
        return insertDatabase("INSERT into Generator (Start_Date, End_Date, Amount) VALUES(\'" + startDate + "\', " +
                "\'" + endDate + "\', \'" + amount + ")");
    } // end of method insertGeneratorData

    public String insertAcTypeData(String startDate, String endDate, String description, int number) {
        return insertDatabase("INSERT into AC_Type (Start_Date, End_Date, Description, Number) VALUES(\'" + startDate + "\', " +
                "\'" + endDate + "\', \'" + description + "\', \'" + number + "')");
    } // end of method insertAcTypeData
}
