package Montalvo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.sql.*;

public class Business {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    LocalDate cdate = LocalDate.now();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public void business(){
        boolean exit = true;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Manage Business**","");
            System.out.printf("|%-5s%-95s|\n","","1. Add");
            System.out.printf("|%-5s%-95s|\n","","2. Edit");
            System.out.printf("|%-5s%-95s|\n","","3. Delete");
            System.out.printf("|%-5s%-95s|\n","","4. View");
            System.out.printf("|%-5s%-95s|\n","","5. Exit");
            System.out.printf("|%-5sEnter Choice: ","");
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
            switch(choice){
                case 1:
                    add();
                    break;
                case 2:
                    view();
                    edit();
                    view();
                    break;
                case 3:
                    view();
                    delete();
                    view();
                    break;
                case 4:
                    view();
                    break;
                default:
                    exit = false;
                    break;
            }
        }while(exit);
    }
    
    private void add(){
        boolean exit = true;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Add Business**","");
            System.out.print("|\tEnter Business Name: ");
            input.nextLine();
            String name = input.nextLine();
            System.out.print("|\tBusiness Founer: ");
            String founder = input.next();
            long number;
            while(true){
                System.out.print("|\tEnter Contat#: +63 ");
                try{
                    number = input.nextLong();
                    if(number>9000000000L && number<9999999999L){
                        break;
                    }
                }catch(Exception e){
                    input.next();
                }
            }
            String Cnum = "+63 "+number;
            input.nextLine();
            String stat;
            String status;
            System.out.print("|\tEnter Status (Hiring/Not Hiring): ");
            while(true){
                try{
                    stat=input.nextLine();
                    if(stat.equalsIgnoreCase("Hiring")){
                        status = "Hiring";
                        break;
                    }else if(stat.equalsIgnoreCase("Not Hiring")){
                        status = "Not Hiring";
                        break;
                    }else{
                        System.out.print("|\tEnter Again: ");
                    }
                }catch(Exception e){
                    System.out.print("|\tEnter Again: ");
                }
            }
            String SQL = "INSERT INTO B_Business (B_Name, B_Founder, B_Contact, B_Status, B_Update_Date) Values (?,?,?,?,?)";
            conf.addRecord(SQL, name, founder, Cnum, status, cdate);
            exit=false;
        }while(exit);
    }
    
    private void edit(){
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Edit Business**","");
        System.out.print("|\tEnter ID to Edit: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists(id, conf)){
                    break;
                }else if(id == 0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID to Edit Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID to Edit Again: ");
            }
        }
        while(exit){
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.print("|\tEnter Business Name: ");
            input.nextLine();
            String name = input.nextLine();
            System.out.print("|\tBusiness Founer: ");
            String founder = input.next();
            long number;
            while(true){
                System.out.print("|\tEnter Contat#: +63 ");
                try{
                    number = input.nextLong();
                    if(number>9000000000L && number<9999999999L){
                        break;
                    }
                }catch(Exception e){
                    input.next();
                }
            }
            String Cnum = "+63 "+number;
            input.nextLine();
            String stat;
            String status;
            System.out.print("|\tEnter Status (Hiring/Not Hiring): ");
            while(true){
                try{
                    stat=input.nextLine();
                    if(stat.equalsIgnoreCase("Hiring")){
                        status = "Hiring";
                        break;
                    }else if(stat.equalsIgnoreCase("Not Hiring")){
                        status = "Not Hiring";
                        break;
                    }else{
                        System.out.print("|\tEnter Again: ");
                    }
                }catch(Exception e){
                    System.out.print("|\tEnter Again: ");
                }
            }
            String SQL = "UPDATE B_Business SET B_Name = ?, B_Founder = ?, B_Contact = ?, B_Status = ? Where B_Id = ?";
            conf.updateRecord(SQL, name, founder, Cnum, stat, id);
            exit=false;
        }
    }
    public void view(){
        String tbl_view = "SELECT * From B_Business";
        String[] tbl_Headers = {"ID", "Business Name", "Status"};
        String[] tbl_Columns = {"B_Id", "B_Name", "B_Status"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    
    private void delete(){
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Disband Business**","");
        System.out.print("|\tEnter ID to Delete: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists(id, conf)){
                    break;
                }else if(id == 0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID to Delete Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID to Delete Again: ");
            }
        }
        while(exit){
            String SQL = "DELETE FROM B_Business Where B_Id = ?";
            conf.deleteRecord(SQL, id);
            exit = false;
        }
    }
    
    
    
    //validation tanan sa ubos
    private boolean doesIDexists(int id, config conf) {
        String query = "SELECT COUNT(*) FROM B_Business Where B_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking Report ID: " + e.getMessage());
        }
        return false;
    }
}
