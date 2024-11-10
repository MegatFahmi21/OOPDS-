package Ngo;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Represent a NGO menu
 */
public class NgoMenu {

    /**
     * Display the NGO menu
     * NGO is given the option to login or register
     * @throws IOException
     */
    public static void menu() throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.print    ("\n" + "-".repeat(7)+"NGO" + "-".repeat(7) + "\n");
        System.out.printf   ("1. Log in\n");
        System.out.printf   ("2. Register\n\n");
        System.out.print    ("Select login/register 1/2  (0 to return): ");

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
                menu();     // return to Ngo menu

                break;
            case "2":
                register(input);
                menu();     // return to Ngo menu

                break;  
        }
    }

    /**
     * Login
     * NGO has to enter their name and phone 
     * and print whether successful or not
     * @param input
     * @throws IOException
     */
    private static void login (Scanner input) throws IOException {
        input.nextLine();  // Consume newline left-over

        String ngo = "";
        String ngoPhone = "";
        String manPowerCount = "";
        int index = 0;

        while (!Ngo.login(ngo,ngoPhone)) {

            System.out.print ("Enter your name: ");
            ngo = input.nextLine();
            if (ngo.equals("0"))         break;

            System.out.print ("Enter your phone: ");
            ngoPhone = input.nextLine();
            if (ngoPhone.equals("0"))   break;

            if (Ngo.login(ngo,ngoPhone)) {
                System.out.println ("\n" + "-".repeat(30));
                System.out.println ("LOGIN SUCCESSFUL!\n" + "-".repeat(30)+"\n");
                ArrayList<Ngo> ngos = User.User.NgoDataLoader();
        
                for (int i = 0; i < ngos.size(); i++)
                    if (ngos.get(i).getPhone().equals(ngoPhone))
                        index = i;
                        System.out.print (" NAME : " + ngos.get(index).getNgo()                   +
                                          "\n PHONE: " + ngos.get(index).getPhone()               +
                                          "\n MANPOWER: " + ngos.get(index).getManPowerCount()               
                                         );
                        Activities(ngos.get(index).getNgo(), ngoPhone, ngos.get(index).getManPowerCount());

                System.out.print ("\n"+"\nEnter [0] to return to menu: ");
                var temp = input.next();
                }
            else
                //System.out.println ("\nLOGIN UNSUCCESSFUL!\n"+"-".repeat(30));
                System.out.println ("\n[LOGIN UNSUCCESSFUL!]\n");
        }
    }

 

    /**
     * Register
     * NGO has to enter their name, phone and manpower to be saved as a new NGO
     * @param input
     * @throws IOException
     */
    private static void register (Scanner input) throws IOException {
        input.nextLine();  // Consume newline left-over

        System.out.print ("Enter Your Name: ");
        var ngo = input.nextLine();
        if (ngo.equals("0"))         return;
        while(!ngo.matches("[a-zA-Z]+")){
            System.out.print("\nPlease enter a valid name!\n--> ");
            ngo = input.nextLine();
            }

        System.out.print ("\nEnter Your Phone Number: ");
        var phone = input.nextLine();
        if (phone.equals("0"))       return;
        while(phone.length() != 11){
            System.out.print("\nPhone length does not meet the requirement!\n--> ");
            phone = input.nextLine();
            }

        System.out.print ("\nEnter Manpower Count: ");
        String manPowerCount = input.nextLine();
        if (manPowerCount.equals("0"))      
        return;


        Ngo.register (ngo, phone, manPowerCount);

        System.out.println ("Registration successful!");
        System.out.print   ("Enter [0] to return to menu: ");
        var temp = input.next();
    }

    /**
     * Activities menu
     * NGO's can choose between requesting new aids and viewing received aids
     * @param ngo
     * @param ngoPhone
     * @param manPowerCount
     * @throws IOException
     */
    public static void Activities (String ngo, String ngoPhone, String manPowerCount) throws IOException{
        Scanner input = new Scanner(System.in);
        System.out.printf   ("\n\n1. Request New Aids\n");
        System.out.printf   ("2. Received Aids\n\n");
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
                RequestsAids(ngo, ngoPhone, manPowerCount);
                break;
            case "2":
                ReceivedAids(ngo);
                break;  
        }
    }

 
    /**
     * New donations
     * Enter the aids (name, quantity, ngo.) to be donated.
     * @param ngo
     * @param ngoPhone
     * @param manPowerCount
     * @throws IOException
     */
    private static void RequestsAids(String ngo, String ngoPhone, String manPowerCount) throws IOException{
        System.out.println ( "-".repeat(30)+"\n"+"State your requests...");

        Scanner input = new Scanner(System.in);
    
        System.out.print ("Enter type of aid: ");
        String aid = input.nextLine();

        System.out.print ("Enter the amount of items: ");
        String amount = input.nextLine();
        input.nextLine();

        Aids.RequestsAids(aid, amount, ngo, manPowerCount, "Available");
        System.out.println ("Updated: Requests have been sent!");

    }

    /**
     * Print past aids from csv file only for the logged in ngo account
     * @param ngo
     * @throws IOException
     */
    private static void ReceivedAids(String ngo) throws IOException{
        System.out.println("-".repeat(30)+"\n"+"Aids Received: ");
        Aids.AidsHistory(ngo);
    }
}