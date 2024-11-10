package Donor;

import java.util.ArrayList;

import DistCenter.AidsRecords;
import User.User;

import java.io.IOException;

/**
 * Donation used by donors when submitting new donation
 */
public class Donations extends User{
    private String ngo;
    private String aid;
    private String amount;
    private String manPowerCount;
    private String status;
    private String needAmount;
    private String name;
    private String donorPhone;

    /**
     * Construct a new donation object with null values
     */
    public Donations() {}

    /**
     * Construct a new donation object with donor's name, donor's phone, aid, 
     * amount of aid, required amount of aid, NGO name, man power and status
     * @param name
     * @param donorPhone
     * @param aid
     * @param amount
     * @param needAmount
     * @param ngo
     * @param manPowerCount
     * @param status
     */
    public Donations(String name, String donorPhone, String aid, String amount, String needAmount, String ngo, String manPowerCount, String status) {
        this.aid = aid;
        this.amount = amount;
        this.donorPhone = donorPhone;
        this.name = name;
        this.needAmount = needAmount;
        this.status = status;
        this.ngo = ngo;
        this.manPowerCount = manPowerCount;
    }

    /**
     * Return the donor's phone
     * @return donor's phone
     */
    public String getDonorPhone() {
        return donorPhone;
    }

    /**
     * Return the name of aid
     * @return name of aid
     */
    public String getAid() {
        return aid;
    }

    /**
     * Return the amount od aid donated 
     * @return amount of aid donated
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Return name of donor 
     * @return name of donor
     */
    public String getName() {
        return name;
    }

    /**
     * return ngo, aid, amount and name separated by commas.
     * @return ngo, aid, amount and name
     */
    public String toCSVString() {
        return ngo + "," + aid + "," + amount + "," + name;
    }
    public String toString() {
        return ngo + "," + aid + "," + amount + "," + name;
    }

    /**
     * create new donations object
     * save new donations object into allAidsRecords.csv
     * @param name
     * @param donorPhone
     * @param aid
     * @param amount
     * @throws IOException
     */
    protected static void NewDonations (String name, String donorPhone, String aid, String amount) throws IOException{
        ArrayList<Donations> donated = DonationLoader();
        Donations d2 = new Donations (name, donorPhone, aid, amount, "-", "-", "-", "Available");
        donated.add (d2);

        saveDonationsToCSV(donated);
    }

    /**
     * The format to write in allAidsRecords.csv for donor
     * @param indexCounter
     * @return all data fields of donation records combine in a string
     */
    public String formatToWriteD(int indexCounter) {
        String dRecords;
        String strIndex = String.valueOf(indexCounter);

        dRecords = strIndex + "," + name + "," + donorPhone + "," + aid + "," + amount + "," + needAmount + "," + ngo + "," + manPowerCount + "," + status;
        return dRecords;
    }


    /**
     * View the donor's past donations in a tabular format
     * @param name
     * @throws IOException
     */
    protected static void DonationsHistory(String name) throws IOException {
        ArrayList<AidsRecords> donated = PastDonations(name);
        System.out.println(
                "|------+------------+--------------+------------+------------------+------------------+------------+------------+------------|");
        System.out.println(
                "| No   | Donor      | Phone        | Aids       | Quantity Given   | Quantity Needed  | NG0        | Manpower   | Status     |");
        System.out.println(
                "|------+------------+--------------+------------+------------------+------------------+------------+------------|------------|");
        for(int i = 0; i < donated.size(); i++) {
                    System.out.printf("|%5s |", i + 1);
                    System.out.printf("%11s |", donated.get(i).getDonorName());
                    System.out.printf("%13s |", donated.get(i).getDonorPhone());
                    System.out.printf("%11s |", donated.get(i).getItemName());
                    System.out.printf("%17s |", donated.get(i).getDonorQuantityGiven());
                    System.out.printf("%17s |", donated.get(i).getNgoQuantityNeeded());
                    System.out.printf("%11s |", donated.get(i).getNgoName());
                    System.out.printf("%11s |", donated.get(i).getNgoManpower());
                    System.out.printf("%11s |\n", donated.get(i).getStatus());
        }
        System.out.println(
            "|------+------------+--------------+------------+------------------+------------------+------------+------------+------------|");
    }
    
}