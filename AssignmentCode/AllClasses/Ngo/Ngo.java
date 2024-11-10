package Ngo;
import java.util.ArrayList;

import User.User;

import java.io.IOException;

/**
 * Represents a NGO
 */
public class Ngo extends User {
    private String ngo;
    private String phone;
    private String manPowerCount;

    /**
     * Construct new NGO data
     */
    public Ngo() {}

    /**
     * Construct new NGO data with specified values
     * @param ngo
     * @param phone
     * @param manPowerCount
     */
    public Ngo (String ngo, String phone, String manPowerCount) {
        this.ngo = ngo;
        this.phone = phone;
        this.manPowerCount = manPowerCount;
    }

    /**
     * Returns the name of NGO
     * @return NGO name
     */
    public String getNgo() {
        return ngo;
    }

    /**
     * Returns the phone of NGO
     * @return NGO phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Return manpower count of NGO
     * @return NGO's manpower count
     */
    public String getManPowerCount() {
        return manPowerCount;
    }

    /**
     * 
     * @return NGO's first name
     */
    public String getFirstName() {
        String[] fname = ngo.split(" ");
        return fname[0];
    }


    /**
     * Returns ID, name; separated by commas.
     * @return
     */
    public String toCSVString() {
        return ngo+ "," + phone + "," + manPowerCount;
    }


    /**
     * This method is used to verify login for NGO's name and phone
     * @param name
     * @param phone
     * @return True if name and phone is found; False otherwise
     * @throws IOException
     */
    protected static boolean login (String name, String phone) throws IOException {
        ArrayList<Ngo> ngos = NgoDataLoader(); // Load data from NgoData.csv

        for (int i = 0; i < ngos.size(); i++)
            if (ngos.get(i).ngo.equals(name) && ngos.get(i).phone.equals(phone))
                return true;
        return false;
    }

    

    /**
     * This method is used to save donor's name and phone information into NgoData.csv.
     * @param ngo
     * @param phone
     * @param manPowerCount
     * @throws IOException
     */
    protected static void register (String ngo, String phone, String manPowerCount) throws IOException {
        ArrayList<Ngo> ngos = NgoDataLoader(); // Load data from NgoData.csv

        ngos.add (new Ngo (ngo, phone, manPowerCount));

        saveNgoToCSV(ngos);
    }
}



