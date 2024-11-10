import Donor.DonorMenu;
import Ngo.NgoMenu;
import DistCenter.DistCenterFlow;

import java.util.Scanner;
import java.io.IOException;

/**
 * Main function of system
 */
public class Main {
    /**
     * Main funtion to display menu
     * @param args the command line arguements
     */
    public static void main (String[] args) {
        Scanner input = new Scanner (System.in);

        try {
            menu(input);
        }
        catch (IOException ex) {
            System.out.println ("\nERROR: COULD NOT FIND .CSV FILE!\n"+"-".repeat(30));
        }
    }

/**
 * Displays the main menu
 * User enters input
 * @param input the user input for main menu
 * @throws IOException if file not found
 */
    public static void menu (Scanner input) throws IOException {
        System.out.printf   ("\n" + "-".repeat(7)+ " Aid Distribution Center " + "-".repeat(7) + "\n" + "\n");
        System.out.printf   ("1. Donor \n");
        System.out.printf   ("2. NGO \n");
        System.out.printf   ("3. Distribution Centre \n");
        System.out.printf   ("0. Exit \n");
        System.out.println  ();
        System.out.printf   ("Enter Number: ");


        var userType = input.next();
        while (!userType.equals("0") && !userType.equals("1") && !userType.equals("2") && !userType.equals("3")) {
            System.out.print ("Input error: Enter 1/2/3 only! (0 to return): ");
            userType = input.next();
        }

        switch(userType) {
            case "0":
                System.out.println ("\nExited the program.");
                break;

            case "1":
                DonorMenu.menu();
                menu (input);   // return to main menu 
                break;

            case "2":
            NgoMenu.menu();
            menu (input);   // return to main menu 
            break;

            case "3":
            DistCenterFlow.distCenterMain();
            menu (input);
            break;
        }                   
    }
}
