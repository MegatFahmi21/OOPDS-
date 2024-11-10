package DistCenter.Queue;

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
 * The class of first-in-first-out (FIFO) queue
 */
public class FifoQueue {

    GenericQueue<String> queue = new GenericQueue<>();

    private static String filename = "allAidsRecords.csv";
    private static String filename2 = "CompletedMatches.csv";
    private ArrayList<AidsRecords> aidsRecordsArrayList = new ArrayList<>();

    private static final String AVAILABLE = "Available";
    private static final String RESERVED = "Reserved";
    private static final String COLLECTED = "Collected";

    /**
     * This constructor read the aid records data from 
     * the csv file and store it in an arraylist
     */
    public FifoQueue() {

        try {

            String[] aidsFromFile;

            Scanner sc = new Scanner(new File(filename));
            while (sc.hasNext()) {

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
     * The menu of the Collection Simulation with FIFO queue
     */
    public void FifoMain() {

        boolean FifoRunning = true;

        Scanner input = new Scanner(System.in);
        int command;
        String selectedNgo;
        AidsRecords collectedAidsRecords = new AidsRecords();

        displayAllAidsRecords();

        do {

            try {
                System.out.println();
                System.out.println(" :: Queue: FIFO Mode ::");
                System.out.println();
                System.out.println(" FIFO Queue: " + queue);
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
                        // collectedAidsRecords = queue.dequeue();
                        specificDequeue();
                        writeToFileFifo();
                        writeToFileCollected();
                        displayAllAidsRecords();
                        break;

                    case 0:
                        System.out.println();
                        System.out.println(" -- Exiting the collection simulation...");
                        FifoRunning = false;
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

        } while (FifoRunning == true);

    }

    /**
     * This method validates the NGO that is to be inserted into the queue
     * @param selectedNgo the name of the NGO to be inserted
     */
    public void specificEnqueue(String selectedNgo) {

        // 1 -- NGO exist in records
        // 2 -- Status of record = Reserved

        boolean ngoExist = false;

        for (int i = 0; i < aidsRecordsArrayList.size(); i++) {
            if (aidsRecordsArrayList.get(i).getNgoName().equals(selectedNgo)
                    && aidsRecordsArrayList.get(i).getStatus().equals(RESERVED)) {
                queue.enqueue(selectedNgo);
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
     * This method removes the first element at the beginning of the queue
     * and change the status of the aid record
     */
    public void specificDequeue() {

        String firstElement = queue.dequeue();
        System.out.println(" " + firstElement);
        System.out.println();
        
        for (int i = 0; i < aidsRecordsArrayList.size(); i++) {
            if (aidsRecordsArrayList.get(i).getNgoName().equals(firstElement)
                    && aidsRecordsArrayList.get(i).getStatus().equals(RESERVED)) {
                aidsRecordsArrayList.get(i).setStatus(COLLECTED);
            }
        }

    }

    /**
     * This method writes all the aid records from the arraylist
     * into the csv file
     */
    public void writeToFileFifo() {

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