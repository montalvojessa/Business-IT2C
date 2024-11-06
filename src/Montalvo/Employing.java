package Montalvo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.sql.*;

public class Employing {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    LocalDate cdate = LocalDate.now();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    Business b = new Business();
    
    public void Employe(){
        boolean exit = true;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Manage Employee**","");
            System.out.printf("|%-5s%-95s|\n","","1. Hire");
            System.out.printf("|%-5s%-95s|\n","","2. Un Hired");
            System.out.printf("|%-5s%-95s|\n","","3. View");
            System.out.printf("|%-5s%-95s|\n","","4. Exit");
            System.out.printf("|%-5sEnter Choice: ","");
            int choice;
            while(true){
                try{
                    choice = input.nextInt();
                    if(choice>0 && choice<5){
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
                    b.view();
                    hire();
                    break;
                case 2:
                    Un_hire();
                    break;
                case 3:
                    view();
                    break;
                default:
                    exit = false;
                    break;
            }
        }while(exit);
    }
    public void hire(){
        LocalDate bdate;
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Hire a Personel**","");
        System.out.print("|\tEnter ID: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists(id, conf)){
                    break;
                }else if(id==0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID: ");
                }
            }catch(Exception e){
                System.out.print("|\tEnter ID: ");
            }
        }
        while(exit){
            System.out.print("|\tEnter First Name: ");
            String fname = input.next();
            System.out.print("|\tEnter Middle Name: ");
            String mname = input.next();
            System.out.print("|\tEnter Last Name: ");
            String lname = input.next();
            String bdate2;
            while(true){
                System.out.print("|\tEnter Birth Date (YYYY-MM-DD): ");
                try{
                    bdate2 = input.next();
                    bdate = LocalDate.parse(bdate2,dateFormat);
                    if(bdate.isBefore(cdate.minusYears(18))&&bdate.isAfter(cdate.minusYears(120))){
                        break;
                    }else{
                        System.out.printf("|%-10s%-80s%-10s|\n","","**Customer Must be 18 Years Old, and Should not be Older than 120**","");
                    }
                }catch(Exception e){
                    System.out.printf("|%-20s%-60s%-20s|\n","","**Follow (YYYY-MM-DD) example (2003-01-05)**","");
                }
            }
            String gender;
            while(true){
                System.out.print("|\tGender (Male/Female): ");
                try{
                    gender = input.next();
                    if(gender.equalsIgnoreCase("Male")||gender.equalsIgnoreCase("Female")){
                        break;
                    }
                }catch(Exception e){
                    
                }
            }
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
            String stat = "Hired";
            String SQL = "INSERT INTO E_Employee (B_Id, E_Fname, E_Mname, E_Lname, E_Bdate, E_Gender, E_Contact, E_Status, E_Cdate) Values (?,?,?,?,?,?,?,?,?)";
            conf.addRecord(SQL, id, fname, mname, lname, bdate, gender,Cnum, stat, cdate);
            exit = false;
        }
    }
    public void Un_hire(){
        viewhire();
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Un-Hire a Personel**","");
        System.out.print("|\tEnter ID: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists2(id, conf)){
                    break;
                }else if(id==0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID: ");
                }
            }catch(Exception e){
                System.out.print("|\tEnter ID: ");
            }
        }
        
        while(exit){
            String stat = "UN-Hired";
            String SQL = "UPDATE E_Employee SET E_Status = ? Where E_Id = ?";
            conf.updateRecord(SQL, stat, id);
            exit = false;
        }
    }
    public void viewhire(){
        String tbl_view = "SELECT * From E_Employee Where E_Status = 'Hired'";
        String[] tbl_Headers = {"ID", "First Name", "Last Name", "Status"};
        String[] tbl_Columns = {"E_Id", "E_Fname", "E_Lname", "E_Status"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    public void view(){
        String tbl_view = "SELECT * From E_Employee";
        String[] tbl_Headers = {"ID", "First Name", "Last Name", "Status"};
        String[] tbl_Columns = {"E_Id", "E_Fname", "E_Lname", "E_Status"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    
    
    //validation tanan ubos
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
    private boolean doesIDexists2(int id, config conf) {
        String query = "SELECT COUNT(*) FROM E_Employee Where E_Id = ?";
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
