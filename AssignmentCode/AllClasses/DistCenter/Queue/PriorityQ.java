package DistCenter.Queue;

import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import DistCenter.AidsRecords;
import DistCenter.DistCenter;

/**
 * The class of Priority queue
 */
public class PriorityQ {

    /**
     * private class inside priority queue that holds ngo, manPower, status to add to the pq list
     */
    private class PriorityAids implements Comparable<PriorityAids> {
        private String ngo;
        private String manPower;
        private String status;

        /**
        * Construct new Priority aids with specified values
        * @param ngo
        * @param manPower
        * @param status
        */
        public PriorityAids(String ngo, String manPower, String status) {
        
            this.ngo = ngo;
            this.manPower = manPower;
            this.status = status;
        }
        
        /**
        * This method returns the ngo of the queue
        * @return the ngo of the queue
        */
        public String getNgo() {
            return ngo;
        }

        /**
        * This method returns the menPower of the queue
        * @return the manPower of the queue
        */
        public String getmanPower() {
            return manPower;
        }

        /**
         * This method determines the printing format of the queue
        */
        public String toString() {
            return ngo;
        }

        /**
        * This method returns the status of the queue
        * @return the status of the queue
        */        
        public String getStatus(){
            return status;
        }

        /**
         * This method is to compare the number of manPower's between ngo
         */
        @Override
        public int compareTo (PriorityAids t) {
            if (manPower.compareTo(t.manPower) > 0)
                return 1;
            else if (manPower.compareTo(t.manPower) < 0)
                return -1;
            else
                return 0;
        }
        
        
        
    }
    
    

    private static String filename = "allAidsRecords.csv";
    private static String filename2 = "CompletedMatches.csv";
    private ArrayList<AidsRecords> aidsRecordsArrayList = new ArrayList<>();
    private ArrayList<PriorityAids> priorityAidsArrayList = new ArrayList<>();
    private PriorityQueue<PriorityAids> pq = new PriorityQueue<PriorityAids>(Collections.reverseOrder());

    private static final String AVAILABLE = "Available";
    private static final String RESERVED = "Reserved";
    private static final String COLLECTED = "Collected";

    public PriorityQ() {

        try {

            String[] aidsFromFile;

            Scanner sc = new Scanner(new File(filename));
            while (sc.hasNext()) {

                aidsFromFile = sc.next().split(",");

                priorityAidsArrayList.add(new PriorityAids(aidsFromFile[6], aidsFromFile[7], aidsFromFile[8]));

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
     * The menu of the Collection Simulation with Priority queue
     */
    public void PriorityMain() {

        boolean PriorityRunning = true;

        Scanner input = new Scanner(System.in);
        int command;
        String selectedNgo;
        AidsRecords collectedAidsRecords = new AidsRecords();

        displayAllAidsRecords();

        do {

            try {
                System.out.println();
                System.out.println(" :: Queue: Priority Mode ::");
                System.out.println();
                System.out.println(" Priority Queue: " + pq.toString());
                System.out.println();
                System.out.println(" 1 -- Enqueue an NGO");
                System.out.println(" 2 -- Dequeue an NGO");
                System.out.println(" 0 -- Exit");
                System.out.println();
                System.out.print(" Command > ");
                command = input.nextInt();

                switch (command) {
                    case 1:
                        // Enqueue
                        selectedNgo = input.next();
                        specificEnqueue(selectedNgo);
                        break;

                    case 2:
                        specificDequeue();
                        writeToFilePriority();
                        writeToFileCollected();
                        displayAllAidsRecords();
                        break;

                    case 0:
                        System.out.println();
                        System.out.println(" -- Exiting the collection simulation...");
                        PriorityRunning = false;
                        break;

                    default:
                        System.out.println();
                        System.out.println(" -- Invalid input. Please try again.");
                }

            } catch (InputMismatchException ime) {
                System.out.println();
                System.out.println(" -- Invalid input. Please try again.");
                input.next();

            } catch (IndexOutOfBoundsException iob) {
                // 
            }

        } while (PriorityRunning == true);

    }

    /**
     * This method validates the NGO that is to be inserted into the queue
     * @param selectedNgo the name of the NGO to be inserted
     */
    public void specificEnqueue(String selectedNgo) {

        // 1 -- NGO exist in records
        // 2 -- Status of record = Reserved

        boolean ngoExist = false;

        for (int i = 0; i < priorityAidsArrayList.size(); i++) {
            if (priorityAidsArrayList.get(i).getNgo().equals(selectedNgo) && priorityAidsArrayList.get(i).getStatus().equals(RESERVED)) {
                pq.add(new PriorityAids(priorityAidsArrayList.get(i).getNgo(),priorityAidsArrayList.get(i).getmanPower(),priorityAidsArrayList.get(i).getStatus()));
                ngoExist = true;
                break;
            }
        }

        if (ngoExist == false) {
            System.out.println();
            System.out.println(" -- The NGO is either does not exist or not reserved.");
        }
    }

    /**
     * This method removes the first element at the beginning of the queue, queue is rearrange after dequeueing according to the ngo's priority
     * and change the status of the aid record
     */
    public void specificDequeue() {
         
        PriorityAids firstElement = pq.remove();
        System.out.println(" " + firstElement);
        System.out.println();
        
        for (int i = 0; i < aidsRecordsArrayList.size(); i++) {
            if (aidsRecordsArrayList.get(i).getNgoName().equals(firstElement.getNgo())
                    && aidsRecordsArrayList.get(i).getStatus().equals(RESERVED)) {
                aidsRecordsArrayList.get(i).setStatus(COLLECTED);
            }
        }
    }

    /**
     * This method writes all the aid records from the arraylist
     * into the csv file
     */
    public void writeToFilePriority() {
        String recordsCombined;
        int indexCounter = 1;

        try {

            File writeFile = new File(filename);
            FileOutputStream fos = new FileOutputStream(writeFile);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i < aidsRecordsArrayList.size(); i++) {

                recordsCombined = aidsRecordsArrayList.get(i).csvFormatToWrite(indexCounter);
                bw.write(recordsCombined);
                bw.newLine();
                indexCounter += 1;

            }

            bw.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /**
     * This method writes all the aid records with the status "Reserved"
     * or "Collected" from the arraylist into the csv file
     */
    public void writeToFileCollected() {

        String recordsCombined;
        int indexCounter = 1;

        try {

            File writeFile = new File(filename2);
            FileOutputStream fos = new FileOutputStream(writeFile);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i < aidsRecordsArrayList.size(); i++) {

                if(aidsRecordsArrayList.get(i).getStatus().equals(RESERVED) || aidsRecordsArrayList.get(i).getStatus().equals(COLLECTED)) {
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
     * This method display all the aid records in a tabular format
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

}

