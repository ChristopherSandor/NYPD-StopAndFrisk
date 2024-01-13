import java.util.ArrayList;

/**
 * The StopAndFrisk class represents stop-and-frisk data, provided by
 * the New York Police Department (NYPD), that is used to compare
 * during when the policy was put in place and after the policy ended.
 * 
 * @author Tanvi Yamarthy
 * @author Vidushi Jindal
 */
public class StopAndFrisk {

    /*
     * The ArrayList keeps track of years that are loaded from CSV data file.
     * Each SFYear corresponds to 1 year of SFRecords. 
     * Each SFRecord corresponds to one stop and frisk occurrence.
     */ 
    private ArrayList<SFYear> database; 

    /*
     * Constructor creates and initializes the @database array
     * 
     * DO NOT update nor remove this constructor
     */
    public StopAndFrisk () {
        database = new ArrayList<>();
    }

    /*
     * Getter method for the database.
     * *** DO NOT REMOVE nor update this method ****
     */
    public ArrayList<SFYear> getDatabase() {
        return database;
    }

    /**
     * This method reads the records information from an input csv file and populates 
     * the database.
     * 
     * Each stop and frisk record is a line in the input csv file.
     * 
     * 1. Open file utilizing StdIn.setFile(csvFile)
     * 2. While the input still contains lines:
     *    - Read a record line (see assignment description on how to do this)
     *    - Create an object of type SFRecord containing the record information
     *    - If the record's year has already is present in the database:
     *        - Add the SFRecord to the year's records
     *    - If the record's year is not present in the database:
     *        - Create a new SFYear 
     *        - Add the SFRecord to the new SFYear
     *        - Add the new SFYear to the database ArrayList
     * 
     * @param csvFile
     */
    public void readFile ( String csvFile ) {

        // DO NOT remove these two lines
        StdIn.setFile(csvFile); // Opens the file
        StdIn.readLine();       // Reads and discards the header line

        while(StdIn.hasNextLine()){
        
            String[] recordEntries = StdIn.readLine().split(",");
            int year = Integer.parseInt(recordEntries[0]);
            String description = recordEntries[2];
            String gender = recordEntries[52];
            String race = recordEntries[66];
            String location = recordEntries[71];
            boolean arrested = recordEntries[13].equals("Y");
            boolean frisked = recordEntries[16].equals("Y");

            SFRecord newRecord = new SFRecord(description, arrested, frisked, gender, race, location);

            boolean yearExists = false;
            for (SFYear x : database) {
                if (x.getcurrentYear() == year) {
                    x.addRecord(newRecord);
                    yearExists = true;
                    break;
                }
            }

            if (yearExists == false) {
                SFYear newYear = new SFYear(year);
                newYear.addRecord(newRecord);
                database.add(newYear);
            }

        }
    }

    /**
     * This method returns the stop and frisk records of a given year where 
     * the people that was stopped was of the specified race.
     * 
     * @param year we are only interested in the records of year.
     * @param race we are only interested in the records of stops of people of race. 
     * @return an ArrayList containing all stop and frisk records for people of the 
     * parameters race and year.
     */

    public ArrayList<SFRecord> populationStopped ( int year, String race ) {

        ArrayList<SFRecord> list = new ArrayList<SFRecord>();

        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++){
                    if(database.get(i).getRecordsForYear().get(j).getRace().equals(race)){
                        list.add(database.get(i).getRecordsForYear().get(j));
                    }
                }
            }
        }
        return list;
    }

    /**
     * This method computes the percentage of records where the person was frisked and the
     * percentage of records where the person was arrested.
     * 
     * @param year we are only interested in the records of year.
     * @return the percent of the population that were frisked and the percent that
     *         were arrested.
     */
    public double[] friskedVSArrested ( int year ) {
        
        // Total Cases and return Array;
        double[] percent = new double[2];
        int totalCases = 0;

        //percent[0]
        double frisked = 0; // defining %
        int friskedCount = 0; // count

        //percent[1]
        double arrested = 0; // defining %
        int arrestedCount = 0; // count
        

        // Finding total friskedCount and arrestedCount
        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){
                totalCases = database.get(i).getRecordsForYear().size();
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++){
                    if(database.get(i).getRecordsForYear().get(j).getFrisked() == true){
                        friskedCount++;
                    }
                    if(database.get(i).getRecordsForYear().get(j).getArrested() == true){
                        arrestedCount++;
                    }
                }
            }
        }

        // Percantages
        frisked = (double) friskedCount/totalCases * 100;
        arrested = (double) arrestedCount/totalCases * 100;

        percent[0] = frisked;
        percent[1] = arrested;

        return percent; // update the return value
    }

    /**
     * This method keeps track of the fraction of Black females, Black males,
     * White females and White males that were stopped for any reason.
     * Drawing out the exact table helps visualize the gender bias.
     * 
     * @param year we are only interested in the records of year.
     * @return a 2D array of percent of number of White and Black females
     *         versus the number of White and Black males.
     */
    public double[][] genderBias ( int year ) {

        double[][] stats = new double[2][3]; // array with stats

        int blackWomen = 0;
        int blackMen = 0;
        int totalBlack = 0;

        int whiteWomen = 0;
        int whiteMen = 0;
        int totalWhite = 0;

        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++){

                    // checking race (White or Black)
                    if(database.get(i).getRecordsForYear().get(j).getRace().equals("W")){
                        // Race: White
                        totalWhite++;

                        // Checking Gender (Male or Female)
                        if(database.get(i).getRecordsForYear().get(j).getGender().equals("M")){
                            // White Men
                            whiteMen++;
                        } else if (database.get(i).getRecordsForYear().get(j).getGender().equals("F")) {
                            // White Women
                            whiteWomen++;
                        }
                    } else if (database.get(i).getRecordsForYear().get(j).getRace().equals("B")) {
                        // Race: Black
                        totalBlack++;

                        // Checking Gender (Male or Female)
                        if(database.get(i).getRecordsForYear().get(j).getGender().equals("M")){
                            // Black Men
                            blackMen++;
                        } else if (database.get(i).getRecordsForYear().get(j).getGender().equals("F")){
                            // Black Women
                            blackWomen++;
                        }
                    }

                }
            }
        }

        // Black Female %
        stats[0][0] = (double) blackWomen/totalBlack * 0.5 * 100;

        // Black Men %
        stats[1][0] = (double) blackMen/totalBlack * 0.5 * 100;

        // White Female %
        stats[0][1] = (double) whiteWomen/totalWhite * 0.5 * 100;

        // White Men %
        stats[1][1] = (double) whiteMen/totalWhite * 0.5 * 100;

        // Total Female %
        stats[0][2] = stats[0][0] + stats[0][1];

        // Total Male %
        stats[1][2] = stats[1][0] + stats[1][1];

        return stats; // update the return value
    }

    /**
     * This method checks to see if there has been increase or decrease 
     * in a certain crime from year 1 to year 2.
     * 
     * Expect year1 to preceed year2 or be equal.
     * 
     * @param crimeDescription
     * @param year1 first year to compare.
     * @param year2 second year to compare.
     * @return 
     */

    public double crimeIncrease ( String crimeDescription, int year1, int year2 ) {
        
        // Year One Stats
        int yearOneCrimeCount = 0; // count of the crime
        int yearOneTotalCount = 0; // count of the total crimes (aka cases/records)
        double yearOneCrimePercent; // Percentage of the crime in that year

        // Year Two Stats
        int yearTwoCrimeCount = 0;
        int yearTwoTotalCount = 0;
        double yearTwoCrimePercent;

        // Year One Checker
         for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year1){
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++){
                    if(database.get(i).getRecordsForYear().get(j).getDescription().indexOf(crimeDescription) != -1){
                        yearOneCrimeCount++;
                    }
                    yearOneTotalCount++;
                }
            }
        }

        // Year Two Checker
         for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year2){
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++){
                    if(database.get(i).getRecordsForYear().get(j).getDescription().indexOf(crimeDescription) != -1){
                        yearTwoCrimeCount++;
                    }
                    yearTwoTotalCount++;
                }
            }
        }

        // Final Percent Calculations
        yearOneCrimePercent = (double) yearOneCrimeCount/yearOneTotalCount * 100;
        yearTwoCrimePercent = (double) yearTwoCrimeCount/yearTwoTotalCount * 100;
        double percentDifference =  yearTwoCrimePercent - yearOneCrimePercent;

	return percentDifference;
    }

    /**
     * This method outputs the NYC borough where the most amount of stops 
     * occurred in a given year. This method will mainly analyze the five 
     * following boroughs in New York City: Brooklyn, Manhattan, Bronx, 
     * Queens, and Staten Island.
     * 
     * @param year we are only interested in the records of year.
     * @return the borough with the greatest number of stops
     */
    public String mostCommonBorough ( int year ) {

        // Crime count for each Borough
        // Brooklyn = 0;
        // Manhattan = 1;
        // Bronx = 2;
        // Queens = 3;
        // StatenIsland = 4;
        int[] boroughList = new int[5];

        // Counting Values
        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++){
                    String borough = database.get(i).getRecordsForYear().get(j).getLocation();
                    switch (borough) {
                        case "BROOKLYN":
                            boroughList[0]++;
                            break;
                        case "MANHATTAN":
                            boroughList[1]++;
                            break;
                        case "BRONX":
                            boroughList[2]++;
                            break;
                        case "QUEENS":
                            boroughList[3]++;
                            break;
                        case "STATENISLAND":
                            boroughList[4]++;
                            break;
                        default:;
                            break;
                    }
                }
            }
        }
        
        // Finding the highest Value
        int indexOfHigh = 0;
        int valueOfHigh = 0;
        String nameOfHigh = "";

        for(int i = 0; i < boroughList.length; i++){
            if(boroughList[i] > valueOfHigh){
                indexOfHigh = i;
                valueOfHigh = boroughList[i];
            }
        }

        // Assigning Name after finding highest Value
        switch (indexOfHigh) {
            case 0:
                nameOfHigh = "Brooklyn";
                break;
            case 1:
                nameOfHigh = "Manhattan";
                break;
            case 2:
                nameOfHigh = "Bronx";
                break;
            case 3:
                nameOfHigh = "Queens";
                break;
            case 4:
                nameOfHigh = "Staten Island";
                break;
            default:;
                break;
        
            }


        return nameOfHigh; // update the return value
    }

}
