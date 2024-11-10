package Donor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represent a donor menu
 */
public class DonorMenu {


    /**
     * Display Donor Menu
     * Donor is given the option to login or register
     * @throws IOException
     */
    public static void menu() throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.print    ("\n" + "-".repeat(7)+"DONOR" + "-".repeat(7) + "\n");
        System.out.printf   ("1. Log in\n");
        System.out.printf   ("2. Register\n\n");
        System.out.print    ("Select login or register 1/2 (0 to return): ");


        var userType = input.next();
        while (!userType.equals("0") && !userType.equals("1") && !userType.equals("2")) {
            System.out.print ("Input error: Enter 1/2 only! (0 to return): ");
            userType = input.next();
        }
        switch(userType) {
            case "0":

                break;
            case "1":
                login(input);
                menu();     // return to donor menu

                break;
            case "2":
                register(input);
                menu();     // return to donor menu

                break;  
        }
    }


    /**
     * Login 
     * Donor has to enter their name and phone 
     * and print whether successful or not
     * @param input
     * @throws IOException
     */
    private static void login (Scanner input) throws IOException {
        input.nextLine();  // Consume newline left-over
        String name = "";
        String phone = "";
        String fname;
        String donorPhone;
        int index = 0;

        while (!Donor.login(name,phone)) {

            System.out.print ("Enter your name: ");
            name = input.nextLine();
            if (name.equals("0"))         
            break;

            System.out.print ("Enter your phone: ");
            phone = input.nextLine();
            if (phone.equals("0"))   
            break;

            if (Donor.login(name,phone)) {
                System.out.println ("\n" + "-".repeat(30));
                System.out.println ("LOGIN SUCCESSFUL!\n" + "-".repeat(30)+"\n");
                ArrayList<Donor> donors = User.User.DataLoader();
        
                for (int i = 0; i < donors.size(); i++)
                    if (donors.get(i).getName().equals(name) && donors.get(i).getPhone().equals(phone))
                        index = i;
                        System.out.println (" NAME : " + donors.get(index).getName()+"\n PHONE: " + donors.get(index).getPhone());
                        fname = donors.get(index).getFirstName();
                        donorPhone = donors.get(index).getPhone();
                        Activities(fname, donorPhone);
                System.out.print ("\n"+"\nEnter [0] to return to menu: ");
                var temp = input.next();
                }
            else
                System.out.println ("\n[LOGIN UNSUCCESSFUL!]\n");
        }
    }



    /**
     * Register
     * Donor has to enter their name & phone to be saved as a new donor
     * @param input
     * @throws IOException
     */
    private static void register (Scanner input) throws IOException {
        input.nextLine();  // Consume newline left-over

        System.out.print ("Enter Your Name: ");
        var name = input.nextLine();
        if (name.equals("0"))         return;
        while(!name.matches("[a-zA-Z]+")){
            System.out.print("\nPlease enter a valid name!\n--> ");
            name = input.nextLine();
            }

        System.out.print ("\nEnter Your Phone Number: ");
        var phone = input.nextLine();
        if (phone.equals("0"))       return;
        while(phone.length() != 11){
            System.out.print("\nPhone length does not meet the requirement!\n--> ");
            phone = input.nextLine();
            }

        Donor.register (name, phone);

        System.out.println ("Registration successful!");
        System.out.print   ("Enter [0] to return to menu: ");
        var temp = input.next();
    }

    /**
     * Activities menu
     * Donors can choose between making new donations or viewing past donations
     * @param firstname
     * @param donorPhone
     * @throws IOException
     */
    public static void Activities (String firstname, String donorPhone) throws IOException{
        Scanner input = new Scanner(System.in);
        System.out.printf   ("\n\n1. New Donations\n");
        System.out.printf   ("2. Past Donations\n\n");
        System.out.print    ("Select New Donations/Past Donations 1/2 (0 to return): ");

        var userType = input.next();
        while (!userType.equals("0") && !userType.equals("1") && !userType.equals("2")) {
            System.out.print ("Input error: Enter 1/2 only! (0 to return): ");
            userType = input.next();
        }

        switch(userType) {
            case "0":
                break;
            case "1":
                NewDonations(firstname, donorPhone);
                break;
            case "2":
                DonationsHistory(firstname);
                break;  
        }
    }

    /**
     * new donations
     * Enter the aids (ngo, aid, amount ) to be donated. 
     */
    private static void NewDonations(String firstname, String donorPhone) throws IOException{
        System.out.println ( "-".repeat(30)+"\n"+"Make new Donations");

        Scanner input = new Scanner(System.in);

        System.out.print ("Enter name of aid: ");
        String aid = input.nextLine();

        System.out.print ("Enter the amount of items: ");
        String amount = input.nextLine();
        input.nextLine();

        Donations.NewDonations(firstname, donorPhone, aid, amount);
        System.out.println ("Updated: New Donations submitted!");
    }


    /**
     * past donations with
     * print past donations from csv file only if name matches
     * @param firstname
     * @throws IOException
     */
    private static void DonationsHistory(String firstname) throws IOException{
        System.out.println("-".repeat(30)+"\n"+"Donations History: ");
        Donations.DonationsHistory(firstname);
    }
}