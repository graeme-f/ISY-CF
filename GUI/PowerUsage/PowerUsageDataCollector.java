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

    private static PowerUsageDataCollector singleInstance = null;
    private HashMap<Integer, Electricity> electricityDetails;
    private HashMap<Integer, Generator> generatorDetails;

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
    } // end of method PowerUsageDataCollector

    private void getAllElectricity() {
        electricityDetails = new HashMap<>();
        String query = "SELECT Start_Date, End_Date FROM Electricity WHERE StartDate " + getBetweenSchoolYear() + "FROM Electricity";
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
    }

    private void getAllGenerator() {
        generatorDetails = new HashMap<>();
        String query = "SELECT Start_Date, End_Date FROM Generator WHERE StartDate " + getBetweenSchoolYear() + "FROM Generator";
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
    }

    public ArrayList<Integer> getElectricityList(){
        ArrayList<Integer> electricity = new ArrayList<>();
        Set< HashMap.Entry< Integer, PowerUsageDataCollector.Electricity> > st = electricityDetails.entrySet();

        for (HashMap.Entry< Integer, PowerUsageDataCollector.Electricity> me:st)
        {
            electricity.add(me.getKey());
        }
        return electricity;
    } // end method getElectricityList()

    public ArrayList<Integer> getGeneratorList(){
        ArrayList<Integer> generator = new ArrayList<>();
        Set< HashMap.Entry< Integer, PowerUsageDataCollector.Generator> > st = generatorDetails.entrySet();

        for (HashMap.Entry< Integer, PowerUsageDataCollector.Generator> me:st)
        {
            generator.add(me.getKey());
        }
        return generator;
    } // end method getGeneratorList()


    public HashMap electricityGetMonthMeterUnits()  {
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
    } // end method getMonthMeterUnits

    public HashMap generatorGetMonthAmount()  {
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
    } // end method getMonthMeterUnits

    public String getMonth(LocalDate date) {
        return date.getMonth().toString();
    }

    public LocalDate getLastDate() {
        return lastDate.plusDays(1);
    } // end method getStartDate()


    public String insertElectricityData(String startDate, String endDate, String meterUnits) {
        return insertDatabase("INSERT into electricity (Start_Date, End_Date, Meter_Units) VALUES(\'" + startDate + "\', " +
                "\'" + endDate + "\', \'" + meterUnits + ")");
    }

    public String insertGeneratorData(String startDate, String endDate, String amount) {
        return insertDatabase("INSERT into electricity (Start_Date, End_Date, Amount) VALUES(\'" + startDate + "\', " +
                "\'" + endDate + "\', \'" + amount + ")");
    }
}
