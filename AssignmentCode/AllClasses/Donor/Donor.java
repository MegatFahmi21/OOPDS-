package Donor;
import java.util.ArrayList;

import User.User;

import java.io.IOException;

/**
 * Represents a donor
 */
public class Donor extends User {
    private String name;
    private String phone;

    /**
     * Construct new donor data 
     */
    public Donor() {}

    /**
     * Construct new donor data with specified values
     * @param name
     * @param phone
     */
    public Donor (String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    /**
     * Returns the name of donor
     * @return donor's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the phone of donor
     * @return donor's phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 
     * @return donor's first name
     */
    public String getFirstName() {
        String[] fname = name.split(" ");
        return fname[0];
    }

    /**
     * 
     * @return donor's ID, name; separated by commas.
     */
    public String toCSVString() {
        return name + "," + phone;
    }



    /**
     * This method is used to verify login for donor's name and phone
     * @param name
     * @param phone
     * @return True if the name and phone is found; False otherwise
     * @throws IOException
     */
    protected static boolean login (String name, String phone) throws IOException {
        ArrayList<Donor> donors = DataLoader(); // Load data from DonorData.csv

        for (int i = 0; i < donors.size(); i++)
            if (donors.get(i).name.equals(name) && donors.get(i).phone.equals(phone))
                return true;
        return false;
    }



    /** 
     * This method is used to save donor's name and phone information into DonorData.csv.
     * @param name
     * @param phone
     * @throws IOException
     */
    protected static void register (String name, String phone) throws IOException {
        ArrayList<Donor> donors = DataLoader(); // Load data from DonorData.csv
        Donor d1 = new Donor (name, phone);
        donors.add (d1);

        saveDonorToCSV(donors);
        //CreateNewDonationsFile(d1.getFirstName());
    }
}