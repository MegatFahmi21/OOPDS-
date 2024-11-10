package User;
import Donor.Donations;
import Donor.Donor;
import Ngo.Ngo;
import Ngo.Aids;


import java.util.List;

import DistCenter.AidsRecords;

import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;



/**
 * User superclass containing methods universally used by most, if not all user types/roles.
 */
public abstract class User {

    /**
     * Load DonorData.csv into donors ArrayList
     * @return an array list of donors
     * @throws IOException
     */
    public static ArrayList<Donor> DataLoader() throws IOException {
        ArrayList<Donor> donors = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get("DonorData.csv"));

        for (int i = 0; i < lines.size(); i++) {
            String[] items = lines.get(i).split(",");
            donors.add (new Donor(items[0], items[1]));
        }

        return donors;
    }


    /**
     * Save donors ArrayList into DonorsData.csv.
     * @param donors
     * @throws IOException
     */
    public static void saveDonorToCSV (ArrayList<Donor> donors) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < donors.size(); i++) {
            sb.append (donors.get(i).toCSVString() + "\n");
        }
        Files.write(Paths.get("DonorData.csv"), sb.toString().getBytes());
    }

    /**
     * Load allAidsRecords.csv into donated ArrayList
     * @return list of donated aids
     * @throws IOException
     */
    public static ArrayList<Donations> DonationLoader() throws IOException {
        ArrayList<Donations> donated = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get("allAidsRecords.csv"));

        for (int i = 0; i < lines.size(); i++) {
            String[] items = lines.get(i).split(",");
            if (items[8].equals("Reserved")) {
                donated.add (new Donations(items[1], items[2], items[3], items[4], items[5], items[6], items[7], "Reserved"));
            }
            else if (items[8].equals("Collected")) {
                donated.add (new Donations(items[1], items[2], items[3], items[4], items[5], items[6], items[7], "Collected"));
            }
            else {
                donated.add (new Donations(items[1], items[2], items[3], items[4], items[5], items[6], items[7], "Available"));
            }
        }
        return donated;
    }

    /**
     * Save newly created donation into allAidsRecords.csv. 
     * @param donated
     * @throws IOException
     */
    public static void saveDonationsToCSV (ArrayList<Donations> donated) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < donated.size(); i++) {
            sb.append (donated.get(i).formatToWriteD(i + 1) + "\n");
        }
        Files.write(Paths.get("allAidsRecords.csv"), sb.toString().getBytes());
    }

    /**
     * To count the total amount of lines in allAidsRecords.csv
     * @return total line of csv file
     */
    public static long countLine() { 

        Path path = Paths.get("allAidsRecords.csv");
  
        long lines = 0;
        try {
  
            lines = Files.lines(path).count();
  
        } catch (IOException e) {
            e.printStackTrace();
        }
  
        return lines;
  
    }

    /**
     * Load past donations from csv file only if name of donor matches 
     * @param name
     * @return complete matches of aids
     * @throws IOException
     */
    public static ArrayList<AidsRecords> PastDonations(String name) throws IOException{
        ArrayList<AidsRecords> donated = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get("CompletedMatches.csv"));

        for(int i=0; i<lines.size(); i++) {
            String[] items = lines.get(i).split(",");
            if(items[1].equals(name)) {
                donated.add (new AidsRecords(items[0] ,items[1], items[2], items[3], items[4], items[5], items[6], items[7], items[8]));
            }
        }
        return donated;       
    }

    /**
     * Load NgoData.csv into ngos ArrayList. 
     * @return array list of NGO's
     * @throws IOException
     */
    public static ArrayList<Ngo> NgoDataLoader() throws IOException {
        ArrayList<Ngo> ngos = new ArrayList<Ngo>();
        List<String> lines = Files.readAllLines(Paths.get("NgoData.csv"));

        for (int i = 0; i < lines.size(); i++) {
            String[] items = lines.get(i).split(",");

            ngos.add (new Ngo (items[0], items[1], items[2]));
        }

        return ngos;
    }

    /**
     * Save ngos ArrayList into NgoData.csv.
     * @param ngos
     * @throws IOException
     */
    public static void saveNgoToCSV (ArrayList<Ngo> ngos) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ngos.size(); i++) {
            sb.append (ngos.get(i).toCSVString() + "\n");
        }
        Files.write(Paths.get("NgoData.csv"), sb.toString().getBytes());
    }

     /**
      * load allAidsRecords.csv into request ArrayList
      * @return
      * @throws IOException
      */
     public static ArrayList<Aids> RequestLoader() throws IOException {
        ArrayList<Aids> request = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get("allAidsRecords.csv"));

        for (int i = 0; i < lines.size(); i++) {
            String[] items = lines.get(i).split(",");
            if(items[8].equals("Reserved")){
                request.add (new Aids(items[1], items[2], items[3], items[4], items[5], items[6], items[7], "Reserved"));
            }
            else if(items[8].equals("Collected")) {
                request.add (new Aids(items[1], items[2], items[3], items[4], items[5], items[6], items[7], "Collected"));
            }
            else {
                request.add (new Aids(items[1], items[2], items[3], items[4], items[5], items[6], items[7], "Available"));
            }
        }
        return request;
    }

    /**
     * Save newly created request into allAidsRecords.csv. 
     * @param request
     * @throws IOException
     */
    public static void saveRequestToCSV (ArrayList<Aids> request) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < request.size(); i++) {
            sb.append (request.get(i).formatToWriteA(i + 1) + "\n");
        }
        Files.write(Paths.get("allAidsRecords.csv"), sb.toString().getBytes());
    }

    /**
     * Load past donations from csv file only if name of NGO matches
     * @param name
     * @return complete matches of aids
     * @throws IOException
     */
    public static ArrayList<AidsRecords> PastAids(String name) throws IOException{
        ArrayList<AidsRecords> aids = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get("CompletedMatches.csv"));

        for(int i=0; i<lines.size(); i++) {
            String[] items = lines.get(i).split(",");
            if(items[6].equals(name)) {
                aids.add (new AidsRecords(items[0] ,items[1], items[2], items[3], items[4], items[5], items[6], items[7], items[8]));
            }
        }
        return aids;                            
    }

}
