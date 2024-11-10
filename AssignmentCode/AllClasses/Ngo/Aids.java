package Ngo;

import java.io.IOException;
import java.util.ArrayList;

import DistCenter.AidsRecords;
import User.User;

/**
 * Aids to be received used by NGOs when submitting new request
 */
public class Aids extends User{
    private String aid;
    private String amount;
    private String needAmount;
    private String ngo;
    private String name;
    private String status;
    private String manPowerCount;
    private String donorPhone;
    
    /**
     * Construct aids object with null values
     */
    public Aids() {}

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
    public Aids(String name, String donorPhone, String aid, String amount, String needAmount,String ngo, String manPowerCount, String status) {
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
     * Return the name of aid
     * @return name of aid
     */
    public String getAid() {
        return aid;
    }
    
    /**
     * Return the amount of aid needed
     * @return amount of aid needed
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Return the name of NGO
     * @return NGO's name
     */
    public String getNgo() {
        return ngo;
    }

    /**
     * Return the manpower count
     * @return manpower count
     */
    public String manPowerCount() {
        return manPowerCount;
    }

    /**
     * return aid, amount and ngo name separated by commas.
     * @return
     */
    public String toCSVString() {
        return aid + "," + amount + "," + ngo;
    }
    public String toString() {
        return aid + "," + amount + "," + ngo;
    }


    /**
     *create new aids request object
     *save new request object into allAidsRecords.csv
     * @param aid
     * @param amountNeeded
     * @param ngo
     * @param manPowerCount
     * @param status
     * @throws IOException
     */
    protected static void RequestsAids (String aid, String amountNeeded, String ngo, String manPowerCount, String status ) throws IOException{
        ArrayList<Aids> request = RequestLoader();
        Aids a2 = new Aids("-", "-", aid, "-", amountNeeded, ngo, manPowerCount, status);
        request.add(a2);

        saveRequestToCSV(request);
    }

    /**
     * The format to write in allAidsRecords.csv for NGO
     * @param indexCounter
     * @return all data fields of aids needed record combine in a string
     */
    public String formatToWriteA(int indexCounter) {
        String aRecords;
        String strIndex = String.valueOf(indexCounter);

        aRecords = strIndex + "," + name + "," + donorPhone + "," + aid + "," + amount + "," + needAmount + "," + ngo + "," + manPowerCount + "," + status;
        return aRecords;
    }

    /**
     * View the NGOs aids received in a tabular format
     * @param name
     * @throws IOException
     */
    protected static void AidsHistory(String name) throws IOException {
        ArrayList<AidsRecords> aided = PastAids(name);
        System.out.println(
                "|------+------------+--------------+------------+------------------+------------------+------------+------------+------------|");
        System.out.println(
                "| No   | Donor      | Phone        | Aids       | Quantity Given   | Quantity Needed  | NG0        | Manpower   | Status     |");
        System.out.println(
                "|------+------------+--------------+------------+------------------+------------------+------------+------------|------------|");
        for(int i = 0; i < aided.size(); i++) {
                    
                    System.out.printf("|%5s |", i + 1);
                    System.out.printf("%11s |", aided.get(i).getDonorName());
                    System.out.printf("%13s |", aided.get(i).getDonorPhone());
                    System.out.printf("%11s |", aided.get(i).getItemName());
                    System.out.printf("%17s |", aided.get(i).getDonorQuantityGiven());
                    System.out.printf("%17s |", aided.get(i).getNgoQuantityNeeded());
                    System.out.printf("%11s |", aided.get(i).getNgoName());
                    System.out.printf("%11s |", aided.get(i).getNgoManpower());
                    System.out.printf("%11s |\n", aided.get(i).getStatus());
        }
        System.out.println(
            "|------+------------+--------------+------------+------------------+------------------+------------+------------+------------|");
    }

}