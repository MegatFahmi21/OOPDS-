package DistCenter;

import java.util.InputMismatchException;
import java.util.Scanner;

import DistCenter.Queue.FifoQueue;
import DistCenter.Queue.PriorityQ;

/**
 * Determine the flow of the distribution center
 */
public class DistCenterFlow {

/**
 * Display menu for distribution center and ask for user input
 */
    public static void distCenterMain() {

        boolean distCenterRunning = true;
        Scanner input = new Scanner(System.in);

        int dcChoice = 0;
        boolean proceed = false;

        while (distCenterRunning) {
            System.out.println();
            System.out.println(" :: Distribution Center ::");
            System.out.println();
            System.out.println(" 1 -- View All Aids Records");
            System.out.println(" 2 -- Match One-to-One Aids Records");
            System.out.println(" 3 -- Match One-to-Many Aids Records");
            System.out.println(" 4 -- Match Many-to-One Aids Records");
            System.out.println(" 5 -- Match Mant-to-Many Aids Records");
            System.out.println(" 6 -- Collection Simulation - FIFO Mode");
            System.out.println(" 7 -- Collection Simulation - Priority Mode");
            System.out.println(" 0 -- Back to User Selection");
            System.out.println();

            try {

                System.out.print(" Enter a number: ");
                dcChoice = input.nextInt();
                System.out.println();

                if (dcChoice < 0 || dcChoice > 7) {
                    System.out.println(" -- The input is out of range. Please try again.");
                } else {
                    proceed = true;
                }

                if (proceed == true) {

                    DistCenter dc = new DistCenter();

                    if (dcChoice == 1) {
                        dc.displayAllAidsRecords();

                    } else if (dcChoice == 2) {
                        dc.matchOneToOne();

                    } else if (dcChoice == 3) {
                        dc.matchOneToMany();

                    } else if (dcChoice == 4) {
                        dc.matchManyToOne();

                    } else if (dcChoice == 5) {
                        dc.matchManyToMany();

                    } else if (dcChoice == 0) {
                        System.out.println(" -- Thank you for using our service.");
                        distCenterRunning = false;

                    } else if (dcChoice == 6) {
                        FifoQueue fq = new FifoQueue();
                        fq.FifoMain();

                    } else if (dcChoice == 7) {
                        PriorityQ pq = new PriorityQ();
                        pq.PriorityMain();

                    }

                }

            } catch (InputMismatchException ime) {
                System.out.println(" -- Invalid input. Please try again");
                input.next();
            }

        }

    }

}