package Montalvo;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = true;
        do {
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n", "", "**Business App**", "");
            System.out.printf("|%-5s%-95s|\n", "", "1. Business Manage");
            System.out.printf("|%-5s%-95s|\n", "", "2. Employ Manage");
            System.out.printf("|%-5s%-95s|\n", "", "3. Reports");
            System.out.printf("|%-5s%-95s|\n", "", "4. Exit");
            System.out.printf("|%-5sEnter Choice: ", "");
            int choice;
            while(true){
                try{
                    choice = input.nextInt();
                    if(choice>0 && choice<6){
                        break;
                    }else{
                        System.out.printf("|%-5sEnter Choice Again: ","");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.printf("|%-5sEnter Choice Again: ","");
                }
            }
            switch (choice) {
                case 1:
                    Business b = new Business();
                    b.business();
                    break;
                case 2:
                    Employing e = new Employing();
                    e.Employe();
                    break;
                case 3:
                    Report r = new Report();
                    r.report_type();
                    break;
                default:
                    exit = false;
                    break;
            }
        } while (exit);
    }
}
