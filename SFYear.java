import java.util.ArrayList;

/**
 * SFYear class stores all SFRecord objects of a given year.
 * Each SFRecord corresponds to one stop and frisk occurrence.
 * 
 * Note that every record belongs to the same year.
 * 
 * @author Vidushi Jindal
 * @author Tanvi Yamarthy
 * 
 * ****** DO NOT EDIT! *******
 */

public class SFYear {

    // instance variables
    private int                 year;    // all records refer to this year
    private ArrayList<SFRecord> records; // each SFRecord in the array refers to one stop and frisk

    /*
     * Constructor
     * 
     * @param year read from the csv file
     */
    public SFYear ( int year ) {
        this.year = year;
        this.records = new ArrayList<>();
    }

    // getter methods

    /* 
     * Retrieves the year.
     * 
     * @return instance variable year.
     */
    public int getcurrentYear () {
        return year;
    }

    /*
     * Retrieves the records array.
     * 
     * @return instance variable records 
     */
    public ArrayList<SFRecord> getRecordsForYear () {
        return records;
    }

    /*
     * Adds a new record to the @records array.
     * 
     * @param newRecord is the record being inserted into the array.
     */ 
    public void addRecord ( SFRecord newRecord ) {
        records.add(newRecord);
    }
}