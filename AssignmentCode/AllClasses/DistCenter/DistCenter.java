package DistCenter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DistCenter {

    private static String filename = "allAidsRecords.csv";
    private ArrayList<AidsRecords> aidsRecordsArrayList = new ArrayList<>();

    private static final String RESERVED = "Reserved";
    private static final String AVAILABLE = "Available";

    /**
     * A construct of distribution center object that retrieve
     * all the data of aid records in the center
     */
    DistCenter() {

        try {

            String[] aidsFromFile;

            AidsRecords tempAidsRecords = new AidsRecords();
            Scanner sc = new Scanner(new File(filename));
            while (sc.hasNext())
            {

                aidsFromFile = sc.next().split(",");

                aidsRecordsArrayList.add(new AidsRecords(aidsFromFile[0], aidsFromFile[1],
                        aidsFromFile[2], aidsFromFile[3], aidsFromFile[4],
                        aidsFromFile[5], aidsFromFile[6], aidsFromFile[7],
                        aidsFromFile[8]));
            }

            sc.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /**
     * This method sort the donor name in the arraylist of aid records
     */
    public void sortByDonorNameAscending() {

        AidsRecords tempAidsRecords = new AidsRecords();
        int notAscending = 0;

        while (true) {

            for (int i = 0; i < aidsRecordsArrayList.size() - 1; i++) {

                if (aidsRecordsArrayList.get(i).getDonorName()
                        .compareTo(aidsRecordsArrayList.get(i + 1).getDonorName()) > 0) {
                    tempAidsRecords.setAllAttAidsRecords(aidsRecordsArrayList.get(i + 1));
                    aidsRecordsArrayList.get(i + 1).setAllAttAidsRecords(aidsRecordsArrayList.get(i));
                    aidsRecordsArrayList.get(i).setAllAttAidsRecords(tempAidsRecords);
                    notAscending += 1;
                }
            }

            if (notAscending == 0) {
                break;
            }
        }

        updateIndex();
        rearrangeIndex();

    }

    /**
     * This method update the index of aid records by setting
     * the index of invalid records to "-" and remove those 
     * records which its index is "-"
     */
    public void updateIndex() {

        int indexCounter = 1;
        String strIndexCounter = String.valueOf(indexCounter);

        for (AidsRecords aidsRecords : aidsRecordsArrayList) {
            if (aidsRecords.getDonorName().equals("-") && aidsRecords.getNgoName().equals("-")) {
                aidsRecords.setIndex("-");
                aidsRecords.setStatus(RESERVED);
            } else {
                strIndexCounter = String.valueOf(indexCounter);
                aidsRecords.setIndex(strIndexCounter);
                indexCounter += 1;
            }
        }

        for (int i = 0; i < aidsRecordsArrayList.size(); i++){
            if (aidsRecordsArrayList.get(i).getIndex().equals("-")){
                aidsRecordsArrayList.remove(i);
            }
        }      
    }

    /**
     * This method rearrange the index of the aid records in the
     * arraylist in ascending order and avoid the aid records 
     * which its index is "-"
     */
    public void rearrangeIndex() {

        int indexCounter = 1;
        String strIndex;

        for (int i = 0; i < aidsRecordsArrayList.size(); i++) {
            if (!aidsRecordsArrayList.get(i).getIndex().equals("-")) {
                strIndex = String.valueOf(indexCounter);
                aidsRecordsArrayList.get(i).setIndex(strIndex);
                indexCounter += 1;
            }
        }

    }

    /**
     * This method writes all the aid records in the arraylist
     * into a csv file
     */
    public void writeToFile() {

        updateIndex();
        rearrangeIndex();

        String recordsCombined;
        // AidsRecords tempAidsRecords;
        int indexCounter = 1;

        try {

            File writeFile = new File(filename);
            FileOutputStream fos = new FileOutputStream(writeFile);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i < aidsRecordsArrayList.size(); i++) {

                if (!(aidsRecordsArrayList.get(i).getDonorName().equals("-")
                        && aidsRecordsArrayList.get(i).getNgoName().equals("-"))) {
                    recordsCombined = aidsRecordsArrayList.get(i).csvFormatToWrite(indexCounter);
                    bw.write(recordsCombined);
                    bw.newLine();
                    indexCounter += 1;
                }
            }

            bw.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /**
     * This method writes all the completed matches of 
     * the aid records into a csv file
     */
    public void writeToFileHistory(String file) {

        updateIndex();
        rearrangeIndex();

        String recordsCombined;
        int indexCounter = 1;

        try {

            File writeFile = new File(file);
            FileOutputStream fos = new FileOutputStream(writeFile);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i < aidsRecordsArrayList.size(); i++) {

                if (aidsRecordsArrayList.get(i).getStatus().equals(RESERVED)) {
                    recordsCombined = aidsRecordsArrayList.get(i).csvFormatToWrite(indexCounter);
                    bw.write(recordsCombined);
                    bw.newLine();
                    indexCounter += 1;
                }
            }

            bw.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /**
     * This method check either the donor's donations or NGO's requests
     * has the same item as aid records at a specific index in the arraylist 
     * @param user type of user either donor or NGO
     * @param i index of aid records in arraylist
     * @return the index of the aid records or -1 if invalid
     */
    public int checkSimilarItemForSpecific(String user, int i) {

        int index = -1;

        if (user.equals("donor") | user.equals("Donor")) {
            if (!aidsRecordsArrayList.get(i).getDonorName().equals("-")
                    && aidsRecordsArrayList.get(i).getStatus().equals(AVAILABLE)) {
                index = i;
            }
        } else if (user.equals("ngo") | user.equals("Ngo")) {
            if (!aidsRecordsArrayList.get(i).getNgoName().equals("-")
                    && aidsRecordsArrayList.get(i).getStatus().equals(AVAILABLE)) {
                index = i;
            }
        } else {
            index = -1;
        }

        return index;
    }

    /**
     * This method searchs for either donor's donations or NGO's requests
     * of the same item from the arraylist of aid records and display aid records
     * in a tabular form
     * @param itemEntered item name
     * @param user type of user either donor or NGO
     * @return 
     */
    public ArrayList<Integer> searchSimilarItemRecords(String itemEntered, String user) {

        int countSameItem = 0;
        int tempIndex = -1;
        ArrayList<Integer> indexSimilarItem = new ArrayList<>();

        System.out.println(
                "|------+------------+--------------+------------+------------------+------------------+------------+------------+------------|");
        System.out.println(
                "| No   | Donor      | Phone        | Aids       | Quantity Given   | Quantity Needed  | NG0        | Manpower   | Status     |");
        System.out.println(
                "|------+------------+--------------+------------+------------------+------------------+------------+------------+------------|");

        for (int i = 0; i < aidsRecordsArrayList.size(); i++) {

            if (aidsRecordsArrayList.get(i).getItemName().equals(itemEntered)) {

                countSameItem += 1;
                tempIndex = checkSimilarItemForSpecific(user, i);
                if (tempIndex != -1) {
                    indexSimilarItem.add(i);
                    System.out.printf("|%5s |", aidsRecordsArrayList.get(i).getIndex());
                    System.out.printf("%11s |", aidsRecordsArrayList.get(i).getDonorName());
                    System.out.printf("%13s |", aidsRecordsArrayList.get(i).getDonorPhone());
                    System.out.printf("%11s |", aidsRecordsArrayList.get(i).getItemName());
                    System.out.printf("%17s |", aidsRecordsArrayList.get(i).getDonorQuantityGiven());
                    System.out.printf("%17s |", aidsRecordsArrayList.get(i).getNgoQuantityNeeded());
                    System.out.printf("%11s |", aidsRecordsArrayList.get(i).getNgoName());
                    System.out.printf("%11s |", aidsRecordsArrayList.get(i).getNgoManpower());
                    System.out.printf("%11s |\n", aidsRecordsArrayList.get(i).getStatus());
                }

            }

        }

        System.out.println(
                "|------+------------+--------------+------------+------------------+------------------+------------+------------+------------|");

        if (countSameItem == 0) {
            System.out.println(" -- No records with similar item provided/requested.");
        }

        return indexSimilarItem;
    }

    /**
     * This method displays all the aid records in the distribution center
     */
    public void displayAllAidsRecords() {

        System.out.println(
                "|------+------------+--------------+------------+------------------+------------------+------------+------------+------------|");
        System.out.println(
                "| No   | Donor      | Phone        | Aids       | Quantity Given   | Quantity Needed  | NG0        | Manpower   | Status     |");
        System.out.println(
                "|------+------------+--------------+------------+------------------+------------------+------------+------------+------------|");

        for (int i = 0; i < aidsRecordsArrayList.size(); i++) {

            System.out.printf("|%5s |", aidsRecordsArrayList.get(i).getIndex());
            System.out.printf("%11s |", aidsRecordsArrayList.get(i).getDonorName());
            System.out.printf("%13s |", aidsRecordsArrayList.get(i).getDonorPhone());
            System.out.printf("%11s |", aidsRecordsArrayList.get(i).getItemName());
            System.out.printf("%17s |", aidsRecordsArrayList.get(i).getDonorQuantityGiven());
            System.out.printf("%17s |", aidsRecordsArrayList.get(i).getNgoQuantityNeeded());
            System.out.printf("%11s |", aidsRecordsArrayList.get(i).getNgoName());
            System.out.printf("%11s |", aidsRecordsArrayList.get(i).getNgoManpower());
            System.out.printf("%11s |\n", aidsRecordsArrayList.get(i).getStatus());

        }

        System.out.println(
                "|------+------------+--------------+------------+------------------+------------------+------------+------------+------------|");

    }

    /**
     * This method displays all the aid records with 
     * the "Unmatched" status in the distribution center
     */
    public void displayUnmatchedAidsRecords() {

        String tempStatus;
        int countUnmatchedRecords = 0;

        System.out.println(
                "|------+------------+--------------+------------+------------------+------------------+------------+------------+------------|");
        System.out.println(
                "| No   | Donor      | Phone        | Aids       | Quantity Given   | Quantity Needed  | NG0        | Manpower   | Status     |");
        System.out.println(
                "|------+------------+--------------+------------+------------------+------------------+------------+------------+------------|");

        for (int i = 0; i < aidsRecordsArrayList.size(); i++) {

            tempStatus = aidsRecordsArrayList.get(i).getStatus();

            if (tempStatus.equals(AVAILABLE)) {

                countUnmatchedRecords += 1;

                System.out.printf("|%5s |", aidsRecordsArrayList.get(i).getIndex());
                System.out.printf("%11s |", aidsRecordsArrayList.get(i).getDonorName());
                System.out.printf("%13s |", aidsRecordsArrayList.get(i).getDonorPhone());
                System.out.printf("%11s |", aidsRecordsArrayList.get(i).getItemName());
                System.out.printf("%17s |", aidsRecordsArrayList.get(i).getDonorQuantityGiven());
                System.out.printf("%17s |", aidsRecordsArrayList.get(i).getNgoQuantityNeeded());
                System.out.printf("%11s |", aidsRecordsArrayList.get(i).getNgoName());
                System.out.printf("%11s |", aidsRecordsArrayList.get(i).getNgoManpower());
                System.out.printf("%11s |\n", aidsRecordsArrayList.get(i).getStatus());
            }
        }

        System.out.println(
                "|------+------------+--------------+------------+------------------+------------------+------------+------------+------------|");

        if (countUnmatchedRecords == 0) {
            System.out.println(" -- No unmatched records.");
        }

    }

    /**
     * This function determine either the donor at the specific index is valid
     * @param donorIndex index of the donor
     * @return 1 if valid, others if otherwise
     */
    public int validateDonor(int donorIndex) {

        int donorAndNgoValid = 0;

        if (donorIndex == -1) {
            System.out.println(" -- Exiting distribution center page.");
            donorAndNgoValid = -1;

        } else if (aidsRecordsArrayList.get(donorIndex).getDonorName().equals("-")) {
            System.out.println(" -- The donor is invalid. Please enter another number.");

        } else if (aidsRecordsArrayList.get(donorIndex).getStatus().equals(RESERVED)) {
            System.out.println(" -- The donor's record is matched. Please enter another number.");

        } else {
            donorAndNgoValid += 1;

        }

        return donorAndNgoValid;

    }

    public int validateDonorOneToOne(int donorIndex) {

        int donorAndNgoValid = 0;

        if (donorIndex == -1) {
            System.out.println(" -- Exiting the matching process...");
            donorAndNgoValid = -1;

        } else if (aidsRecordsArrayList.get(donorIndex).getDonorName().equals("-")) {
            System.out.println(" -- The donor is invalid. Please enter another number.");

        } else if (aidsRecordsArrayList.get(donorIndex).getStatus().equals(RESERVED)) {
            System.out.println(" -- The donor's record is matched. Please enter another number.");

        } else {
            donorAndNgoValid += 1;

        }

        return donorAndNgoValid;

    }

    /**
     * This function determine either the NGO at the specific index is valid
     * @param ngoIndex index of the NGO
     * @return 1 if valid, others if otherwise
     */
    public int validateNgo(int ngoIndex) {

        int donorAndNgoValid = 0;

        if (ngoIndex == -1) {
            System.out.println(" -- Exiting distribution center page.");
            donorAndNgoValid = -1;

        } else if (aidsRecordsArrayList.get(ngoIndex).getNgoName().equals("-")) {
            System.out.println(" -- The NGO is invalid. Please enter another number.");

        } else if (aidsRecordsArrayList.get(ngoIndex).getStatus().equals(RESERVED)) {
            System.out.println(" -- The NGO's record is matched. Please enter another number.");

        } else {
            donorAndNgoValid += 1;

        }

        return donorAndNgoValid;

    }

    /**
     * This method displays all the records that are about to be matched
     * with the one-to-one relationship
     * @param indexDonor index of the donor
     * @param indexNgo index of the NGO
     */
    public void displayMatchingProcessTableOneToOne(int indexDonor, int indexNgo) {
        System.out.println("The following records will be matched:");

        System.out.println(
                "|------+------------+------------+------------------+------------------+------------|");
        System.out.println(
                "| No   | Donor      | Aids       | Quantity Given   | Quantity Needed  | NG0        |");
        System.out.println(
                "|------+------------+------------+------------------+------------------+------------|");

        System.out.printf("|%5s |", aidsRecordsArrayList.get(indexDonor).getIndex());
        System.out.printf("%11s |", aidsRecordsArrayList.get(indexDonor).getDonorName());
        System.out.printf("%11s |", aidsRecordsArrayList.get(indexDonor).getItemName());
        System.out.printf("%17s |", aidsRecordsArrayList.get(indexDonor).getDonorQuantityGiven());
        System.out.printf("%17s |", aidsRecordsArrayList.get(indexDonor).getNgoQuantityNeeded());
        System.out.printf("%11s |\n", aidsRecordsArrayList.get(indexDonor).getNgoName());

        System.out.printf("|%5s |", aidsRecordsArrayList.get(indexNgo).getIndex());
        System.out.printf("%11s |", aidsRecordsArrayList.get(indexNgo).getDonorName());
        System.out.printf("%11s |", aidsRecordsArrayList.get(indexNgo).getItemName());
        System.out.printf("%17s |", aidsRecordsArrayList.get(indexNgo).getDonorQuantityGiven());
        System.out.printf("%17s |", aidsRecordsArrayList.get(indexNgo).getNgoQuantityNeeded());
        System.out.printf("%11s |\n", aidsRecordsArrayList.get(indexNgo).getNgoName());

        System.out.println(
                "|------+------------+------------+------------------+------------------+------------|");
        System.out.println();
    }

    /**
     * This method displays all the records that are about to be matched
     * with the one-to-many relationship
     * @param indexDonor index of the donor
     * @param usedIndexSimilarItem arraylist of indexes of the NGOs
     */
    public void displayMatchingProcessTableOneToMany(int indexDonor, ArrayList<Integer> usedIndexSimilarItem) {
        System.out.println("The following records will be matched:");

        System.out.println(
                "|------+------------+------------+------------------+------------------+------------|");
        System.out.println(
                "| No   | Donor      | Aids       | Quantity Given   | Quantity Needed  | NG0        |");
        System.out.println(
                "|------+------------+------------+------------------+------------------+------------|");

        System.out.printf("|%5s |", aidsRecordsArrayList.get(indexDonor).getIndex());
        System.out.printf("%11s |", aidsRecordsArrayList.get(indexDonor).getDonorName());
        System.out.printf("%11s |", aidsRecordsArrayList.get(indexDonor).getItemName());
        System.out.printf("%17s |", aidsRecordsArrayList.get(indexDonor).getDonorQuantityGiven());
        System.out.printf("%17s |", aidsRecordsArrayList.get(indexDonor).getNgoQuantityNeeded());
        System.out.printf("%11s |\n", aidsRecordsArrayList.get(indexDonor).getNgoName());

        for (int indexNgo : usedIndexSimilarItem) {
            System.out.printf("|%5s |", aidsRecordsArrayList.get(indexNgo).getIndex());
            System.out.printf("%11s |", aidsRecordsArrayList.get(indexNgo).getDonorName());
            System.out.printf("%11s |", aidsRecordsArrayList.get(indexNgo).getItemName());
            System.out.printf("%17s |", aidsRecordsArrayList.get(indexNgo).getDonorQuantityGiven());
            System.out.printf("%17s |", aidsRecordsArrayList.get(indexNgo).getNgoQuantityNeeded());
            System.out.printf("%11s |\n", aidsRecordsArrayList.get(indexNgo).getNgoName());
        }

        System.out.println(
                "|------+------------+------------+------------------+------------------+------------|");
        System.out.println();
    }

    /**
     * This method displays all the records that are about to be matched
     * with the many-to-one relationship
     * @param indexNgo index ofthe NGO
     * @param usedIndexSimilarItem arraylist of indexes of the donors
     */
    public void displayMatchingProcessTableManyToOne(int indexNgo, ArrayList<Integer> usedIndexSimilarItem) {

        System.out.println("The following records will be matched:");

        System.out.println(
                "|------+------------+------------+------------------+------------------+------------|");
        System.out.println(
                "| No   | Donor      | Aids       | Quantity Given   | Quantity Needed  | NG0        |");
        System.out.println(
                "|------+------------+------------+------------------+------------------+------------|");

        for (int indexDonor : usedIndexSimilarItem) {
            System.out.printf("|%5s |", aidsRecordsArrayList.get(indexDonor).getIndex());
            System.out.printf("%11s |", aidsRecordsArrayList.get(indexDonor).getDonorName());
            System.out.printf("%11s |", aidsRecordsArrayList.get(indexDonor).getItemName());
            System.out.printf("%17s |", aidsRecordsArrayList.get(indexDonor).getDonorQuantityGiven());
            System.out.printf("%17s |", aidsRecordsArrayList.get(indexDonor).getNgoQuantityNeeded());
            System.out.printf("%11s |\n", aidsRecordsArrayList.get(indexDonor).getNgoName());
        }

        System.out.printf("|%5s |", aidsRecordsArrayList.get(indexNgo).getIndex());
        System.out.printf("%11s |", aidsRecordsArrayList.get(indexNgo).getDonorName());
        System.out.printf("%11s |", aidsRecordsArrayList.get(indexNgo).getItemName());
        System.out.printf("%17s |", aidsRecordsArrayList.get(indexNgo).getDonorQuantityGiven());
        System.out.printf("%17s |", aidsRecordsArrayList.get(indexNgo).getNgoQuantityNeeded());
        System.out.printf("%11s |\n", aidsRecordsArrayList.get(indexNgo).getNgoName());

        System.out.println(
                "|------+------------+------------+------------------+------------------+------------|");
        System.out.println();

    }

    /**
     * This method displays all the records that are about to be matched
     * with the many-to-many relationship
     * @param usedIndexSimilarItemDonor arraylist of indexes of the donors
     * @param usedIndexSimilarItemNgo arraylist of indexes of the NGOs
     */
    public void displayMatchingProcessTableManyToMany(ArrayList<Integer> usedIndexSimilarItemDonor,
            ArrayList<Integer> usedIndexSimilarItemNgo) {

        System.out.println("The following records will be matched:");

        System.out.println(
                "|------+------------+------------+------------------+------------------+------------|");
        System.out.println(
                "| No   | Donor      | Aids       | Quantity Given   | Quantity Needed  | NG0        |");
        System.out.println(
                "|------+------------+------------+------------------+------------------+------------|");

        for (int indexDonor : usedIndexSimilarItemDonor) {
            System.out.printf("|%5s |", aidsRecordsArrayList.get(indexDonor).getIndex());
            System.out.printf("%11s |", aidsRecordsArrayList.get(indexDonor).getDonorName());
            System.out.printf("%11s |", aidsRecordsArrayList.get(indexDonor).getItemName());
            System.out.printf("%17s |", aidsRecordsArrayList.get(indexDonor).getDonorQuantityGiven());
            System.out.printf("%17s |", aidsRecordsArrayList.get(indexDonor).getNgoQuantityNeeded());
            System.out.printf("%11s |\n", aidsRecordsArrayList.get(indexDonor).getNgoName());
        }

        for (int indexNgo : usedIndexSimilarItemNgo) {
            System.out.printf("|%5s |", aidsRecordsArrayList.get(indexNgo).getIndex());
            System.out.printf("%11s |", aidsRecordsArrayList.get(indexNgo).getDonorName());
            System.out.printf("%11s |", aidsRecordsArrayList.get(indexNgo).getItemName());
            System.out.printf("%17s |", aidsRecordsArrayList.get(indexNgo).getDonorQuantityGiven());
            System.out.printf("%17s |", aidsRecordsArrayList.get(indexNgo).getNgoQuantityNeeded());
            System.out.printf("%11s |\n", aidsRecordsArrayList.get(indexNgo).getNgoName());
        }

        System.out.println(
                "|------+------------+------------+------------------+------------------+------------|");
        System.out.println();

    }

    /**
     * This method matches the records of a donor's donation with 
     * an NGO's request and write the updated result in a csv files 
     */
    public void matchOneToOne() {

        // -- Local Variables -- //

        // loop and terminate the methods
        boolean methodRunning = true;

        int indexDonor = -1;
        int indexNgo = -1;
        int donorAndNgoValid = 0;
        int proceedToMatch = 0;
        int intDonorQuantityG = 0;
        int intNgoQuantityN = 0;

        String tempIndex;
        String tempDonorName;
        String tempDonorPhone;
        String tempItemName;
        String tempDonorQuantityGiven;
        String tempNgoQuantityNeeded;
        String tempNgoName;
        String tempNgoManpower;

        int diffInQuantity = 0;
        String strDiffInQuantity;

        Scanner input = new Scanner(System.in);

        while (methodRunning) {

            // -- Resetting Local Variables In Loop -- //
            donorAndNgoValid = 0;
            intDonorQuantityG = 0;
            intNgoQuantityN = 0;
            diffInQuantity = 0;

            try {

                System.out.println("    ..:: Matching Aids Records ::..    ");
                System.out.println("    --  One to One Relationship  --    ");
                System.out.println();
                System.out.println(" >> Please enter the donor and NGO index no of aids to be matched.");
                System.out.println(" >> Enter 0 to exit the matching process.");
                System.out.println();

                displayUnmatchedAidsRecords();

                System.out.println();
                System.out.println(" :: Selecting Donor :: ");
                System.out.println();
                System.out.print(" Please enter a donor no: ");
                indexDonor = input.nextInt();
                indexDonor -= 1;
                System.out.println();

                donorAndNgoValid += validateDonorOneToOne(indexDonor);

                if (donorAndNgoValid == -1) {
                    methodRunning = false;
                }

                if (donorAndNgoValid == 1) {

                    tempItemName = aidsRecordsArrayList.get(indexDonor).getItemName();

                    searchSimilarItemRecords(tempItemName, "Ngo");
                    System.out.println();
                    System.out.println(" :: Selecting NGO :: ");
                    System.out.println();
                    System.out.println(" >> Select one NGO from the table.");
                    System.out.println(" >> Please enter the NGO's index no of aids to be matched.");
                    System.out.println(" >> Enter 0 to exit the matching process.");
                    System.out.println();
                    System.out.print(" Please enter an NGO no : ");
                    indexNgo = input.nextInt();
                    indexNgo -= 1;
                    System.out.println();

                    donorAndNgoValid += validateNgo(indexNgo);
                    if (donorAndNgoValid == 0) {
                        methodRunning = false;
                    }

                }

                if (donorAndNgoValid == 2) {

                    if (aidsRecordsArrayList.get(indexDonor).getItemName() // if item sama
                            .equals(aidsRecordsArrayList.get(indexNgo).getItemName())) {

                        displayMatchingProcessTableOneToOne(indexDonor, indexNgo);

                        System.out.println(" :: Proceed to match the record? ::");
                        System.out.println(" >> 1 - Yes");
                        System.out.println(" >> 2 - No");
                        System.out.print(" Enter a number: ");
                        proceedToMatch = input.nextInt();
                        System.out.println();

                        if (proceedToMatch == 1) {

                            intDonorQuantityG = Integer
                                    .parseInt(aidsRecordsArrayList.get(indexDonor).getDonorQuantityGiven());
                            intNgoQuantityN = Integer
                                    .parseInt(aidsRecordsArrayList.get(indexNgo).getNgoQuantityNeeded());

                            if (intDonorQuantityG == intNgoQuantityN) {

                                // Copy Ngo.get(indexNgo) parts, paste at Donor.get(indexDonor)
                                tempNgoName = aidsRecordsArrayList.get(indexNgo).getNgoName();
                                tempNgoManpower = aidsRecordsArrayList.get(indexNgo).getNgoManpower();
                                tempNgoQuantityNeeded = aidsRecordsArrayList.get(indexNgo).getNgoQuantityNeeded();

                                aidsRecordsArrayList.get(indexDonor).setNgoName(tempNgoName);
                                aidsRecordsArrayList.get(indexDonor).setNgoManPower(tempNgoManpower);
                                aidsRecordsArrayList.get(indexDonor).setNgoQuantityNeeded(tempNgoQuantityNeeded);
                                aidsRecordsArrayList.get(indexDonor).setStatus(RESERVED);

                                // Set all Ngo.get(indexNgo) to "-" except index and status
                                // Change status from AVAILABLE to RECORDMATCH

                                tempIndex = aidsRecordsArrayList.get(indexNgo).getIndex();
                                aidsRecordsArrayList.get(indexNgo).setAllAttAidsRecords(tempIndex, "-", "-", "-", "-",
                                        "-",
                                        "-",
                                        "-",
                                        RESERVED);

                            } else if (intDonorQuantityG > intNgoQuantityN) {

                                // -- Copy Donor.get(indexDonor) parts, paste at Ngo.get(indexNgo)
                                // -- donorQuantityGiven == ngoQuantityNeeded
                                // -- diffInQuantity == donorQuantityGiven balance after given to Ngo

                                tempDonorName = aidsRecordsArrayList.get(indexDonor).getDonorName();
                                tempDonorPhone = aidsRecordsArrayList.get(indexDonor).getDonorPhone();
                                tempDonorQuantityGiven = aidsRecordsArrayList.get(indexNgo).getNgoQuantityNeeded();

                                aidsRecordsArrayList.get(indexNgo).setDonorName(tempDonorName);
                                aidsRecordsArrayList.get(indexNgo).setDonorPhone(tempDonorPhone);
                                aidsRecordsArrayList.get(indexNgo).setDonorQuantityGiven(tempDonorQuantityGiven);
                                aidsRecordsArrayList.get(indexNgo).setStatus(RESERVED);

                                diffInQuantity = intDonorQuantityG - intNgoQuantityN;
                                strDiffInQuantity = String.valueOf(diffInQuantity);

                                aidsRecordsArrayList.get(indexDonor).setDonorQuantityGiven(strDiffInQuantity);

                            } else if (intDonorQuantityG < intNgoQuantityN) {

                                tempNgoName = aidsRecordsArrayList.get(indexNgo).getNgoName();
                                tempNgoManpower = aidsRecordsArrayList.get(indexNgo).getNgoManpower();
                                tempNgoQuantityNeeded = aidsRecordsArrayList.get(indexDonor).getDonorQuantityGiven();

                                aidsRecordsArrayList.get(indexDonor).setNgoName(tempNgoName);
                                aidsRecordsArrayList.get(indexDonor).setNgoManPower(tempNgoManpower);
                                aidsRecordsArrayList.get(indexDonor).setNgoQuantityNeeded(tempNgoQuantityNeeded);
                                aidsRecordsArrayList.get(indexDonor).setStatus(RESERVED);

                                diffInQuantity = intNgoQuantityN - intDonorQuantityG;
                                strDiffInQuantity = String.valueOf(diffInQuantity);

                                aidsRecordsArrayList.get(indexNgo).setNgoQuantityNeeded(strDiffInQuantity);

                            }

                            System.out.println(" -- Aid records matched. Thank you.");
                            writeToFile();
                            writeToFileHistory("CompletedMatches.csv");

                        } else if (proceedToMatch == 2) {
                            System.out.println(" -- Matching process cancelled.");

                        } else {
                            System.out.println(" -- Invalid input. Matching process cancelled. Please try again.");

                        }

                    } else {
                        System.out.println(" -- Both records entered have different aids provided/requested.");
                        System.out.println(" -- Please try again.");

                    }

                }

            } catch (InputMismatchException ime) {
                System.out.println(" -- Invalid input. Please try again.");

            } catch (IndexOutOfBoundsException iob) {
                System.out.println(" -- The index no entered is out of range. Please try again.");

            } finally {
                System.out.println(" -- Press any key to continue...");
                input.nextLine();
                input.nextLine();
            }

        }

    }

    /**
     * This method matches the records of a donor's donation with 
     * many NGO's request and write the updated result in a csv files 
     */
    public void matchOneToMany() {

        boolean methodRunning = true;

        int indexDonor = -1;
        int indexNgo = -1;
        int donorAndNgoValid = 0;
        int proceedToMatch = 0;
        int intDonorQuantityG = 0;
        int intNgoQuantityN = 0;

        String tempIndex;
        String tempDonorName;
        String tempDonorPhone;
        String tempItemName;
        String tempDonorQuantityGiven;
        String tempNgoQuantityNeeded;
        String tempNgoName;
        String tempNgoManpower;

        int diffInQuantity = 0;
        String strDiffInQuantity;

        ArrayList<Integer> indexSimilarItem = new ArrayList<>();
        ArrayList<Integer> usedIndexSimilarItem = new ArrayList<>();
        int tempIndexNgo;
        boolean exist = false;
        int i = 0;
        int j = 0;

        Scanner input = new Scanner(System.in);

        while (methodRunning) {

            // -- Resetting Local Variables In Loop -- //
            donorAndNgoValid = 0;
            intDonorQuantityG = 0;
            intNgoQuantityN = 0;
            diffInQuantity = 0;
            indexSimilarItem.clear();
            usedIndexSimilarItem.clear();
            exist = false;
            i = 0;
            j = 0;

            try {

                System.out.println("    ..:: Matching Aids Records ::..    ");
                System.out.println("    --  One to Many Relationship  --    ");
                System.out.println();
                System.out.println(" >> Please enter the donor and NGO index no of aids to be matched.");
                System.out.println(" >> Enter 0 to exit the matching process.");
                System.out.println();

                displayUnmatchedAidsRecords();

                System.out.println();
                System.out.println(" :: Selecting Donor :: ");
                System.out.println();
                System.out.print(" Please enter a donor no: ");
                indexDonor = input.nextInt();
                indexDonor -= 1;
                System.out.println();

                donorAndNgoValid = validateDonor(indexDonor);

                if (donorAndNgoValid == 1) {

                    tempItemName = aidsRecordsArrayList.get(indexDonor).getItemName();

                    indexSimilarItem = searchSimilarItemRecords(tempItemName, "Ngo");
                    System.out.println();

                    if (indexSimilarItem.size() == 0) {
                        System.out.println(" -- No choice to be matched.");
                        System.out.println();
                    }

                    while (indexSimilarItem.size() != 0) {

                        exist = false;

                        System.out.println(" :: Selecting NGO :: ");
                        System.out.println();
                        System.out.println(" >> Select available NGOs from the table.");
                        System.out.println(" >> Please enter the NGO's index no of aids to be matched.");
                        System.out.println(" >> The first NGO entered will be prioritized.");
                        System.out.println(" >> Enter 0 to stop selecting.");
                        System.out.println(" >> Enter -1 to exit the matching process.");
                        System.out.println();
                        System.out.print(" Enter an NGO no: ");
                        tempIndexNgo = input.nextInt();
                        tempIndexNgo -= 1;
                        System.out.println();

                        if (tempIndexNgo == -1) {
                            System.out.println(" -- Selecting process end.");
                            System.out.println();
                            break;

                        } else if (tempIndexNgo == -2) {
                            System.out.println(" -- Exiting the matching process...");
                            System.out.println();
                            methodRunning = false;
                            break;

                        }

                        for (int ind = 0; ind < indexSimilarItem.size(); ind++) {
                            if (tempIndexNgo == indexSimilarItem.get(ind)) {
                                usedIndexSimilarItem.add(tempIndexNgo);
                                indexSimilarItem.remove(ind);
                                exist = true;
                                donorAndNgoValid += 1;
                            }

                        }

                        if (exist == false) {
                            System.out.println(" -- The NGO is invalid or has been selected. Please enter again.");
                            System.out.println();
                            i--;
                        }

                        System.out.print(" >> Current selected NG0: ");
                        for (int l = 0; l < usedIndexSimilarItem.size(); l++) {
                            System.out.print((usedIndexSimilarItem.get(l) + 1) + " ");
                        }
                        System.out.println();
                        System.out.println();

                        if (indexSimilarItem.size() == 0) {
                            System.out.println(" -- No more choice to be matched.");
                            System.out.println();
                        }

                        i++;

                    }

                    if (usedIndexSimilarItem.size() == 0) {
                        System.out.println(" -- No record selected.");
                    }

                    if (donorAndNgoValid == usedIndexSimilarItem.size() + 1 && methodRunning == true) {

                        if (usedIndexSimilarItem.size() != 0) {

                            displayMatchingProcessTableOneToMany(indexDonor, usedIndexSimilarItem);

                            System.out.println(" :: Proceed to match the record? ::");
                            System.out.println(" >> 1 - Yes");
                            System.out.println(" >> 2 - No");
                            System.out.print(" Enter a number: ");
                            proceedToMatch = input.nextInt();
                            System.out.println();

                            if (proceedToMatch == 1) {

                                while (usedIndexSimilarItem.size() != 0) {

                                    indexNgo = usedIndexSimilarItem.get(j);

                                    intDonorQuantityG = Integer
                                            .parseInt(aidsRecordsArrayList.get(indexDonor).getDonorQuantityGiven());
                                    intNgoQuantityN = Integer
                                            .parseInt(aidsRecordsArrayList.get(indexNgo).getNgoQuantityNeeded());


                                    if (intDonorQuantityG == intNgoQuantityN) {

                                        // Copy Ngo.get(indexNgo) parts, paste at Donor.get(indexDonor)
                                        tempNgoName = aidsRecordsArrayList.get(indexNgo).getNgoName();
                                        tempNgoManpower = aidsRecordsArrayList.get(indexNgo).getNgoManpower();
                                        tempNgoQuantityNeeded = aidsRecordsArrayList.get(indexNgo)
                                                .getNgoQuantityNeeded();

                                        aidsRecordsArrayList.get(indexDonor).setNgoName(tempNgoName);
                                        aidsRecordsArrayList.get(indexDonor).setNgoManPower(tempNgoManpower);
                                        aidsRecordsArrayList.get(indexDonor)
                                                .setNgoQuantityNeeded(tempNgoQuantityNeeded);
                                        aidsRecordsArrayList.get(indexDonor).setStatus(RESERVED);

                                        // Set all Ngo.get(indexNgo) to "-" except index and status
                                        // Change status from AVAILABLE to RECORDMATCH

                                        tempIndex = aidsRecordsArrayList.get(indexNgo).getIndex();
                                        aidsRecordsArrayList.get(indexNgo).setAllAttAidsRecords(tempIndex, "-", "-",
                                                "-",
                                                "-",
                                                "-",
                                                "-",
                                                "-",
                                                RESERVED);

                                        break;

                                    } else if (intDonorQuantityG > intNgoQuantityN) {

                                        // -- Copy Donor.get(indexDonor) parts, paste at Ngo.get(indexNgo)
                                        // -- donorQuantityGiven == ngoQuantityNeeded
                                        // -- diffInQuantity == donorQuantityGiven balance after given to Ngo

                                        tempDonorName = aidsRecordsArrayList.get(indexDonor).getDonorName();
                                        tempDonorPhone = aidsRecordsArrayList.get(indexDonor).getDonorPhone();
                                        tempDonorQuantityGiven = aidsRecordsArrayList.get(indexNgo)
                                                .getNgoQuantityNeeded();

                                        aidsRecordsArrayList.get(indexNgo).setDonorName(tempDonorName);
                                        aidsRecordsArrayList.get(indexNgo).setDonorPhone(tempDonorPhone);
                                        aidsRecordsArrayList.get(indexNgo)
                                                .setDonorQuantityGiven(tempDonorQuantityGiven);
                                        aidsRecordsArrayList.get(indexNgo).setStatus(RESERVED);

                                        diffInQuantity = intDonorQuantityG - intNgoQuantityN;
                                        strDiffInQuantity = String.valueOf(diffInQuantity);

                                        aidsRecordsArrayList.get(indexDonor).setDonorQuantityGiven(strDiffInQuantity);

                                        usedIndexSimilarItem.remove(j);

                                    } else if (intDonorQuantityG < intNgoQuantityN) {

                                        tempNgoName = aidsRecordsArrayList.get(indexNgo).getNgoName();
                                        tempNgoManpower = aidsRecordsArrayList.get(indexNgo).getNgoManpower();
                                        tempNgoQuantityNeeded = aidsRecordsArrayList.get(indexDonor)
                                                .getDonorQuantityGiven();

                                        aidsRecordsArrayList.get(indexDonor).setNgoName(tempNgoName);
                                        aidsRecordsArrayList.get(indexDonor).setNgoManPower(tempNgoManpower);
                                        aidsRecordsArrayList.get(indexDonor)
                                                .setNgoQuantityNeeded(tempNgoQuantityNeeded);
                                        aidsRecordsArrayList.get(indexDonor).setStatus(RESERVED);

                                        diffInQuantity = intNgoQuantityN - intDonorQuantityG;
                                        strDiffInQuantity = String.valueOf(diffInQuantity);

                                        aidsRecordsArrayList.get(indexNgo).setNgoQuantityNeeded(strDiffInQuantity);

                                        break;
                                    }

                                }

                                System.out.println(" -- Aid records matched. Thank you.");
                                writeToFile();
                                writeToFileHistory("CompletedMatches.csv");


                            } else if (proceedToMatch == 2) {
                                System.out.println(" -- Matching process cancelled.");

                            } else {
                                System.out.println(" -- Invalid input. Matching process cancelled. Please try again.");

                            }

                        }

                    }

                } else if (donorAndNgoValid == -1) {
                    methodRunning = false;
                }

            } catch (InputMismatchException ime) {
                System.out.println(" -- Invalid input. Please try again.");

            } catch (IndexOutOfBoundsException iob) {
                System.out.println(" -- The index no entered is out of range. Please try again.");

            } finally {
                System.out.println(" -- Press any key to continue...");
                input.nextLine();
                input.nextLine();
            }

        }

    }

    /**
     * This method matches the records of many donor's donation with 
     * an NGO's request and write the updated result in a csv files 
     */
    public void matchManyToOne() {

        boolean methodRunning = true;

        int indexDonor = -1;
        int indexNgo = -1;
        int donorAndNgoValid = 0;
        int proceedToMatch = 0;
        int intDonorQuantityG = 0;
        int intNgoQuantityN = 0;

        String tempIndex;
        String tempDonorName;
        String tempDonorPhone;
        String tempItemName;
        String tempDonorQuantityGiven;
        String tempNgoQuantityNeeded;
        String tempNgoName;
        String tempNgoManpower;

        int diffInQuantity = 0;
        String strDiffInQuantity;

        ArrayList<Integer> indexSimilarItemDonor = new ArrayList<>();
        ArrayList<Integer> usedIndexSimilarItemDonor = new ArrayList<>();
        ArrayList<Integer> indexSimilarItemNgo = new ArrayList<>();
        int tempIndexDonor;
        boolean exist = false;
        int i = 0;
        int j = 0;

        Scanner input = new Scanner(System.in);

        while (methodRunning) {

            // -- Resetting Local Variables In Loop -- //
            donorAndNgoValid = 0;
            intDonorQuantityG = 0;
            intNgoQuantityN = 0;
            diffInQuantity = 0;
            indexSimilarItemDonor.clear();
            usedIndexSimilarItemDonor.clear();
            indexSimilarItemNgo.clear();
            exist = false;
            i = 0;
            j = 0;

            try {

                System.out.println("    ..:: Matching Aids Records ::..    ");
                System.out.println("    --  Many to One Relationship  --    ");
                System.out.println();
                System.out.println(" >> Please enter the donor and NGO index no of aids to be matched.");
                System.out.println(" >> Enter 0 to exit the matching process.");
                System.out.println();

                displayUnmatchedAidsRecords();

                System.out.println();
                System.out.println(" :: Selecting Donor :: ");
                System.out.println();
                System.out.print(" Please enter a donor no: ");
                indexDonor = input.nextInt();
                indexDonor -= 1;
                System.out.println();

                donorAndNgoValid += validateDonor(indexDonor);

                if (donorAndNgoValid == 1) {

                    tempItemName = aidsRecordsArrayList.get(indexDonor).getItemName();

                    indexSimilarItemDonor = searchSimilarItemRecords(tempItemName, "Donor");
                    System.out.println();

                    for (int ind = 0; ind < indexSimilarItemDonor.size(); ind++) {
                        if (indexSimilarItemDonor.get(ind) == indexDonor) {
                            indexSimilarItemDonor.remove(ind);
                        }
                    }
                    usedIndexSimilarItemDonor.add(indexDonor);

                    if (indexSimilarItemDonor.size() == 0) {
                        System.out.println(" :: Selecting Donor :: ");
                        System.out.println();
                        System.out.println(" >> Select another available donors from the table.");
                        System.out.println(" >> Please enter the donor's index no of aids to be matched.");
                        System.out.println(" >> The first donor entered will be prioritized.");
                        System.out.println(" >> Enter 0 to stop selecting.");
                        System.out.println(" >> Enter -1 to exit the matching process.");
                        System.out.println();
                        System.out.println(" Enter another donor no: ");
                        System.out.println();
                        System.out.println(" -- No choice to be matched.");
                        System.out.println();
                    }

                    while (indexSimilarItemDonor.size() != 0) {

                        exist = false;

                        System.out.println(" :: Selecting Donor :: ");
                        System.out.println();
                        System.out.println(" >> Select another available donors from the table.");
                        System.out.println(" >> Please enter the donor's index no of aids to be matched.");
                        System.out.println(" >> The first donor entered will be prioritized.");
                        System.out.println(" >> Enter 0 to stop selecting.");
                        System.out.println(" >> Enter -1 to exit the matching process.");
                        System.out.println();
                        System.out.print(" Enter another donor no: ");
                        tempIndexDonor = input.nextInt();
                        tempIndexDonor -= 1;
                        System.out.println();

                        if (tempIndexDonor == -1) {
                            System.out.println(" -- Selecting process end.");
                            System.out.println();
                            break;

                        } else if (tempIndexDonor == -2) {
                            System.out.println(" -- Exiting the matching process...");
                            System.out.println();
                            methodRunning = false;
                            break;

                        }

                        for (int ind = 0; ind < indexSimilarItemDonor.size(); ind++) {
                            if (tempIndexDonor == indexSimilarItemDonor.get(ind)) {
                                usedIndexSimilarItemDonor.add(tempIndexDonor);
                                indexSimilarItemDonor.remove(ind);
                                exist = true;
                                donorAndNgoValid += 1;
                            }

                        }

                        if (exist == false) {
                            System.out.println(" -- The donor is invalid or has been selected. Please enter again.");
                            System.out.println();
                            i--;
                        }

                        System.out.print(" >> Current selected donor: ");
                        for (int l = 0; l < usedIndexSimilarItemDonor.size(); l++) {
                            System.out.print((usedIndexSimilarItemDonor.get(l) + 1) + " ");
                        }
                        System.out.println();
                        System.out.println();

                        if (indexSimilarItemDonor.size() == 0) {
                            System.out.println(" -- No more choice to be matched.");
                            System.out.println();
                        }

                        i++;

                    }

                    if (usedIndexSimilarItemDonor.size() == 1) {
                        System.out.println(" -- No additional record selected.");
                        System.out.println();
                    }

                    if (donorAndNgoValid == usedIndexSimilarItemDonor.size() && usedIndexSimilarItemDonor.size() > 0
                            && methodRunning == true) {

                        indexSimilarItemNgo = searchSimilarItemRecords(tempItemName, "Ngo");
                        System.out.println();

                        // maybe buat loop

                        if (indexSimilarItemNgo.size() == 0) {
                            System.out.println(" :: Selecting NGO :: ");
                            System.out.println();
                            System.out.println(" >> Please enter a NGO index no of aids to be matched.");
                            System.out.println(" >> Enter 0 to exit the matching process.");
                            System.out.println();
                            System.out.print(" Please enter a NGO no : ");
                            System.out.println();
                            System.out.println(" -- No choice to be matched.");
                            System.out.println();

                        } else {
                            System.out.println(" :: Selecting NGO :: ");
                            System.out.println();
                            System.out.println(" >> Please enter a NGO index no of aids to be matched.");
                            System.out.println(" >> Enter 0 to exit the matching process.");
                            System.out.println();

                            do {

                                exist = false;

                                System.out.print(" Please enter a NGO no : ");
                                indexNgo = input.nextInt();
                                indexNgo -= 1;
                                System.out.println();

                                if (indexNgo == -1) {
                                    System.out.println(" -- Exiting the matching process...");
                                    System.out.println();
                                    methodRunning = false;
                                    break;
                                }

                                for (int k = 0; k < indexSimilarItemNgo.size(); k++) {
                                    if (indexSimilarItemNgo.get(k) == indexNgo) {
                                        donorAndNgoValid += 1;
                                        exist = true;
                                    }
                                }

                                if (exist == false) {
                                    System.out.println(" -- The NGO is invalid. Please enter another number.");
                                    System.out.println();
                                }

                            } while (exist == false);
                        }

                        if (donorAndNgoValid == usedIndexSimilarItemDonor.size() + 1 && methodRunning == true) {

                            // display matchig table
                            displayMatchingProcessTableManyToOne(indexNgo, usedIndexSimilarItemDonor);

                            System.out.println(" :: Proceed to match the record? ::");
                            System.out.println(" >> 1 - Yes");
                            System.out.println(" >> 2 - No");
                            System.out.print(" Enter a number: ");
                            proceedToMatch = input.nextInt();
                            System.out.println();

                            if (proceedToMatch == 1) {

                                while (usedIndexSimilarItemDonor.size() != 0) {

                                    indexDonor = usedIndexSimilarItemDonor.get(j);

                                    intDonorQuantityG = Integer
                                            .parseInt(aidsRecordsArrayList.get(indexDonor).getDonorQuantityGiven());
                                    intNgoQuantityN = Integer
                                            .parseInt(aidsRecordsArrayList.get(indexNgo).getNgoQuantityNeeded());

                                    if (intDonorQuantityG == intNgoQuantityN) {

                                        // Copy Ngo.get(indexNgo) parts, paste at Donor.get(indexDonor)
                                        tempNgoName = aidsRecordsArrayList.get(indexNgo).getNgoName();
                                        tempNgoManpower = aidsRecordsArrayList.get(indexNgo).getNgoManpower();
                                        tempNgoQuantityNeeded = aidsRecordsArrayList.get(indexNgo)
                                                .getNgoQuantityNeeded();

                                        aidsRecordsArrayList.get(indexDonor).setNgoName(tempNgoName);
                                        aidsRecordsArrayList.get(indexDonor).setNgoManPower(tempNgoManpower);
                                        aidsRecordsArrayList.get(indexDonor)
                                                .setNgoQuantityNeeded(tempNgoQuantityNeeded);
                                        aidsRecordsArrayList.get(indexDonor).setStatus(RESERVED);

                                        // Set all Ngo.get(indexNgo) to "-" except index and status
                                        // Change status from AVAILABLE to RECORDMATCH

                                        tempIndex = aidsRecordsArrayList.get(indexNgo).getIndex();
                                        aidsRecordsArrayList.get(indexNgo).setAllAttAidsRecords(tempIndex, "-", "-",
                                                "-",
                                                "-",
                                                "-",
                                                "-",
                                                "-",
                                                RESERVED);

                                        break;

                                    } else if (intDonorQuantityG > intNgoQuantityN) {

                                        // -- Copy Donor.get(indexDonor) parts, paste at Ngo.get(indexNgo)
                                        // -- donorQuantityGiven == ngoQuantityNeeded
                                        // -- diffInQuantity == donorQuantityGiven balance after given to Ngo

                                        tempDonorName = aidsRecordsArrayList.get(indexDonor).getDonorName();
                                        tempDonorPhone = aidsRecordsArrayList.get(indexDonor).getDonorPhone();
                                        tempDonorQuantityGiven = aidsRecordsArrayList.get(indexNgo)
                                                .getNgoQuantityNeeded();

                                        aidsRecordsArrayList.get(indexNgo).setDonorName(tempDonorName);
                                        aidsRecordsArrayList.get(indexNgo).setDonorPhone(tempDonorPhone);
                                        aidsRecordsArrayList.get(indexNgo)
                                                .setDonorQuantityGiven(tempDonorQuantityGiven);
                                        aidsRecordsArrayList.get(indexNgo).setStatus(RESERVED);

                                        diffInQuantity = intDonorQuantityG - intNgoQuantityN;
                                        strDiffInQuantity = String.valueOf(diffInQuantity);

                                        aidsRecordsArrayList.get(indexDonor).setDonorQuantityGiven(strDiffInQuantity);

                                        break;

                                    } else if (intDonorQuantityG < intNgoQuantityN) {

                                        tempNgoName = aidsRecordsArrayList.get(indexNgo).getNgoName();
                                        tempNgoManpower = aidsRecordsArrayList.get(indexNgo).getNgoManpower();
                                        tempNgoQuantityNeeded = aidsRecordsArrayList.get(indexDonor)
                                                .getDonorQuantityGiven();

                                        aidsRecordsArrayList.get(indexDonor).setNgoName(tempNgoName);
                                        aidsRecordsArrayList.get(indexDonor).setNgoManPower(tempNgoManpower);
                                        aidsRecordsArrayList.get(indexDonor)
                                                .setNgoQuantityNeeded(tempNgoQuantityNeeded);
                                        aidsRecordsArrayList.get(indexDonor).setStatus(RESERVED);

                                        diffInQuantity = intNgoQuantityN - intDonorQuantityG;
                                        strDiffInQuantity = String.valueOf(diffInQuantity);

                                        aidsRecordsArrayList.get(indexNgo).setNgoQuantityNeeded(strDiffInQuantity);

                                        usedIndexSimilarItemDonor.remove(j);

                                    }

                                }

                                System.out.println(" -- Aid records matched. Thank you.");
                                writeToFile();
                                writeToFileHistory("CompletedMatches.csv");


                            }

                            else if (proceedToMatch == 2) {
                                System.out.println(" -- Matching process cancelled.");

                            } else {
                                System.out.println(" -- Invalid input. Matching process cancelled. Please try again.");

                            }
                        }

                    }

                }

                else if (donorAndNgoValid == -1) {
                    methodRunning = false;
                }

            } catch (InputMismatchException ime) {
                System.out.println(" -- Invalid input. Please try again.");

            } catch (IndexOutOfBoundsException iob) {
                System.out.println(" -- The index no entered is out of range. Please try again.");

            } finally {
                System.out.println(" -- Press any key to continue...");
                input.nextLine();
                input.nextLine();

            }

        }

    }

    /**
     * This method matches the records of many donor's donation with 
     * many NGO's request and write the updated result in a csv files
     */
    public void matchManyToMany() {

        boolean methodRunning = true;

        int indexDonor = -1;
        int indexNgo = -1;
        int donorAndNgoValid = 0;
        int proceedToMatch = 0;
        int intDonorQuantityG = 0;
        int intNgoQuantityN = 0;

        String tempIndex;
        String tempDonorName;
        String tempDonorPhone;
        String tempItemName;
        String tempDonorQuantityGiven;
        String tempNgoQuantityNeeded;
        String tempNgoName;
        String tempNgoManpower;

        int diffInQuantity = 0;
        String strDiffInQuantity;

        ArrayList<Integer> indexSimilarItemDonor = new ArrayList<>();
        ArrayList<Integer> usedIndexSimilarItemDonor = new ArrayList<>();
        ArrayList<Integer> indexSimilarItemNgo = new ArrayList<>();
        ArrayList<Integer> usedIndexSimilarItemNgo = new ArrayList<>();
        int tempIndexDonor;
        int tempIndexNgo;
        boolean exist = false;
        int i = 0;
        int j = 0;
        int k = 0;

        Scanner input = new Scanner(System.in);

        while (methodRunning) {

            donorAndNgoValid = 0;
            intDonorQuantityG = 0;
            intNgoQuantityN = 0;
            diffInQuantity = 0;
            indexSimilarItemDonor.clear();
            usedIndexSimilarItemDonor.clear();
            indexSimilarItemNgo.clear();
            usedIndexSimilarItemNgo.clear();
            exist = false;
            i = 0;
            j = 0;
            k = 0;

            try {

                System.out.println("    ..:: Matching Aids Records ::..    ");
                System.out.println("   --  Many to Many Relationship  --    ");
                System.out.println();
                System.out.println(" >> Please enter the donor and NGO index no of aids to be matched.");
                System.out.println(" >> Enter 0 to exit the matching process.");
                System.out.println();

                displayUnmatchedAidsRecords();

                System.out.println();
                System.out.println(" :: Selecting Donor :: ");
                System.out.println();
                System.out.print(" Please enter a donor: ");
                indexDonor = input.nextInt();
                indexDonor -= 1;
                System.out.println();

                donorAndNgoValid = validateDonor(indexDonor);

                if (donorAndNgoValid == -1) {
                    methodRunning = false;
                }

                if (donorAndNgoValid == 1) {

                    tempItemName = aidsRecordsArrayList.get(indexDonor).getItemName();

                    indexSimilarItemDonor = searchSimilarItemRecords(tempItemName, "Donor");
                    System.out.println();

                    for (int ind = 0; ind < indexSimilarItemDonor.size(); ind++) {
                        if (indexSimilarItemDonor.get(ind) == indexDonor) {
                            indexSimilarItemDonor.remove(ind);
                        }
                    }
                    usedIndexSimilarItemDonor.add(indexDonor);

                    if (indexSimilarItemDonor.size() == 0) {
                        System.out.println(" -- No choice to be matched.");
                        System.out.println();
                    }

                    i = 0;
                    while (indexSimilarItemDonor.size() != 0) {

                        exist = false;

                        System.out.println(" :: Selecting Donor :: ");
                        System.out.println();
                        System.out.println(" >> Select another available donors from the table.");
                        System.out.println(" >> Please enter the donor's index no of aids to be matched.");
                        System.out.println(" >> The first donor entered will be prioritized.");
                        System.out.println(" >> Enter 0 to stop selecting.");
                        System.out.println(" >> Enter -1 to exit the matching process.");
                        System.out.println();
                        System.out.print(" Enter another donor: ");
                        tempIndexDonor = input.nextInt();
                        tempIndexDonor -= 1;
                        System.out.println();

                        if (tempIndexDonor == -1) {
                            System.out.println(" -- Selecting process end.");
                            System.out.println();
                            break;

                        } else if (tempIndexDonor == -2) {
                            System.out.println(" -- Exiting the matching process...");
                            System.out.println();
                            methodRunning = false;
                            break;

                        }

                        for (int ind = 0; ind < indexSimilarItemDonor.size(); ind++) {
                            if (tempIndexDonor == indexSimilarItemDonor.get(ind)) {
                                usedIndexSimilarItemDonor.add(tempIndexDonor);
                                indexSimilarItemDonor.remove(ind);
                                exist = true;
                                donorAndNgoValid += 1;
                            }

                        }

                        if (exist == false) {
                            System.out.println(" -- The donor is invalid or has been selected. Please enter again.");
                            System.out.println();
                            i--;
                        }

                        System.out.print(" >> Current selected donor: ");
                        for (int l = 0; l < usedIndexSimilarItemDonor.size(); l++) {
                            System.out.print((usedIndexSimilarItemDonor.get(l) + 1) + " ");
                        }
                        System.out.println();
                        System.out.println();

                        if (indexSimilarItemDonor.size() == 0) {
                            System.out.println(" -- No more choice to be matched.");
                            System.out.println();
                        }

                        i++;

                    }

                    if (donorAndNgoValid == usedIndexSimilarItemDonor.size() && methodRunning == true) {

                        indexSimilarItemNgo = searchSimilarItemRecords(tempItemName, "Ngo");
                        System.out.println();

                        if (indexSimilarItemNgo.size() == 0) {
                            System.out.println(" -- No choice to be matched.");
                        }

                        i = 0;
                        while (indexSimilarItemNgo.size() != 0) {

                            exist = false;

                            System.out.println(" :: Selecting NGO :: ");
                            System.out.println();
                            System.out.println(" >> Select available NGOs from the table.");
                            System.out.println(" >> Please enter the NGO's index no of aids to be matched.");
                            System.out.println(" >> The first NGO entered will be prioritized.");
                            System.out.println(" >> Enter 0 to stop selecting.");
                            System.out.println(" >> Enter -1 to exit the matching process.");
                            System.out.println();
                            System.out.print(" Enter an NGO no: ");
                            tempIndexNgo = input.nextInt();
                            tempIndexNgo -= 1;
                            System.out.println();

                            System.out.println();

                            if (tempIndexNgo == -1) {
                                System.out.println(" -- Selecting process end.");
                                System.out.println();
                                break;

                            } else if (tempIndexNgo == -2) {
                                System.out.println(" -- Exiting the matching process...");
                                System.out.println();
                                methodRunning = false;
                                break;

                            }

                            for (int ind = 0; ind < indexSimilarItemNgo.size(); ind++) {
                                if (tempIndexNgo == indexSimilarItemNgo.get(ind)) {
                                    usedIndexSimilarItemNgo.add(tempIndexNgo);
                                    indexSimilarItemNgo.remove(ind);
                                    exist = true;
                                    donorAndNgoValid += 1;
                                }
                            }

                            if (exist == false) {
                                System.out.println(" -- The NGO is invalid or has been selected. Please enter again.");
                                System.out.println();
                                i--;
                            }

                            System.out.print(" >> Current selected NG0: ");
                            for (int l = 0; l < usedIndexSimilarItemNgo.size(); l++) {
                                System.out.print((usedIndexSimilarItemNgo.get(l) + 1) + " ");
                            }
                            System.out.println();
                            System.out.println();

                            if (indexSimilarItemNgo.size() == 0) {
                                System.out.println(" -- No more choice to be matched.");
                                System.out.println();
                            }

                            i++;

                        }

                        if (usedIndexSimilarItemNgo.size() == 0) {
                            System.out.println(" -- No record selected.");
                        }

                        if (donorAndNgoValid == (usedIndexSimilarItemDonor.size() + usedIndexSimilarItemNgo.size())
                                && usedIndexSimilarItemNgo.size() > 0 && methodRunning == true) {

                            displayMatchingProcessTableManyToMany(usedIndexSimilarItemDonor, usedIndexSimilarItemNgo);

                            System.out.println(" :: Proceed to match the record? ::");
                            System.out.println(" >> 1 - Yes");
                            System.out.println(" >> 2 - No");
                            System.out.print(" Enter a number: ");
                            proceedToMatch = input.nextInt();
                            System.out.println();

                            if (proceedToMatch == 1) {

                                while (usedIndexSimilarItemDonor.size() != 0 && usedIndexSimilarItemNgo.size() != 0) {

                                    // j -- to loop donor
                                    // k -- to loop ngo

                                    indexDonor = usedIndexSimilarItemDonor.get(j);
                                    indexNgo = usedIndexSimilarItemNgo.get(k);

                                    intDonorQuantityG = Integer
                                            .parseInt(aidsRecordsArrayList.get(indexDonor).getDonorQuantityGiven());
                                    intNgoQuantityN = Integer
                                            .parseInt(aidsRecordsArrayList.get(indexNgo).getNgoQuantityNeeded());

                                    if (intDonorQuantityG == intNgoQuantityN) {

                                        // Copy Ngo.get(indexNgo) parts, paste at Donor.get(indexDonor)
                                        tempNgoName = aidsRecordsArrayList.get(indexNgo).getNgoName();
                                        tempNgoManpower = aidsRecordsArrayList.get(indexNgo).getNgoManpower();
                                        tempNgoQuantityNeeded = aidsRecordsArrayList.get(indexNgo)
                                                .getNgoQuantityNeeded();

                                        aidsRecordsArrayList.get(indexDonor).setNgoName(tempNgoName);
                                        aidsRecordsArrayList.get(indexDonor).setNgoManPower(tempNgoManpower);
                                        aidsRecordsArrayList.get(indexDonor)
                                                .setNgoQuantityNeeded(tempNgoQuantityNeeded);
                                        aidsRecordsArrayList.get(indexDonor).setStatus(RESERVED);

                                        // Set all Ngo.get(indexNgo) to "-" except index and status
                                        // Change status from AVAILABLE to RECORDMATCH

                                        tempIndex = aidsRecordsArrayList.get(indexNgo).getIndex();
                                        aidsRecordsArrayList.get(indexNgo).setAllAttAidsRecords(tempIndex, "-", "-",
                                                "-",
                                                "-",
                                                "-",
                                                "-",
                                                "-",
                                                RESERVED);

                                        usedIndexSimilarItemDonor.remove(j);
                                        usedIndexSimilarItemNgo.remove(k);

                                    } else if (intDonorQuantityG > intNgoQuantityN) {

                                        // -- Copy Donor.get(indexDonor) parts, paste at Ngo.get(indexNgo)
                                        // -- donorQuantityGiven == ngoQuantityNeeded
                                        // -- diffInQuantity == donorQuantityGiven balance after given to Ngo

                                        tempDonorName = aidsRecordsArrayList.get(indexDonor).getDonorName();
                                        tempDonorPhone = aidsRecordsArrayList.get(indexDonor).getDonorPhone();
                                        tempDonorQuantityGiven = aidsRecordsArrayList.get(indexNgo)
                                                .getNgoQuantityNeeded();

                                        aidsRecordsArrayList.get(indexNgo).setDonorName(tempDonorName);
                                        aidsRecordsArrayList.get(indexNgo).setDonorPhone(tempDonorPhone);
                                        aidsRecordsArrayList.get(indexNgo)
                                                .setDonorQuantityGiven(tempDonorQuantityGiven);
                                        aidsRecordsArrayList.get(indexNgo).setStatus(RESERVED);

                                        diffInQuantity = intDonorQuantityG - intNgoQuantityN;
                                        strDiffInQuantity = String.valueOf(diffInQuantity);

                                        aidsRecordsArrayList.get(indexDonor).setDonorQuantityGiven(strDiffInQuantity);

                                        usedIndexSimilarItemNgo.remove(k);

                                    } else if (intDonorQuantityG < intNgoQuantityN) {

                                        tempNgoName = aidsRecordsArrayList.get(indexNgo).getNgoName();
                                        tempNgoManpower = aidsRecordsArrayList.get(indexNgo).getNgoManpower();
                                        tempNgoQuantityNeeded = aidsRecordsArrayList.get(indexDonor)
                                                .getDonorQuantityGiven();

                                        aidsRecordsArrayList.get(indexDonor).setNgoName(tempNgoName);
                                        aidsRecordsArrayList.get(indexDonor).setNgoManPower(tempNgoManpower);
                                        aidsRecordsArrayList.get(indexDonor)
                                                .setNgoQuantityNeeded(tempNgoQuantityNeeded);
                                        aidsRecordsArrayList.get(indexDonor).setStatus(RESERVED);

                                        diffInQuantity = intNgoQuantityN - intDonorQuantityG;
                                        strDiffInQuantity = String.valueOf(diffInQuantity);

                                        aidsRecordsArrayList.get(indexNgo).setNgoQuantityNeeded(strDiffInQuantity);

                                        usedIndexSimilarItemDonor.remove(j);

                                    }

                                }

                                System.out.println(" -- Aid records matched. Thank you.");
                                writeToFile();
                                writeToFileHistory("CompletedMatches.csv");

                            } else if (proceedToMatch == 2) {
                                System.out.println(" -- Matching process cancelled.");

                            } else {
                                System.out.println(" -- Invalid input. Matching process cancelled. Please try again.");

                            }

                        }

                    }

                }

            } catch (InputMismatchException ime) {
                System.out.println(" -- Invalid input. Please try again.");

            } catch (IndexOutOfBoundsException iob) {
                System.out.println(" -- The index no entered is out of range. Please try again.");

            } finally {
                System.out.println(" -- Press any key to continue...");
                input.nextLine();
                input.nextLine();

            }

        }

    }

}