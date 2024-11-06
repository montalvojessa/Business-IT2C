package Montalvo;

import java.util.Scanner;
import java.sql.*;

public class Report {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    Business b = new Business();
    
    
    public void report_type() {
        boolean exit = true;
        do {
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n", "", "**Business Report**", "");
            System.out.printf("|%-5s%-95s|\n", "", "1. Individual Business Report");
            System.out.printf("|%-5s%-95s|\n", "", "2. Exit");
            System.out.printf("|%-5sEnter Choice: ", "");
            int choice;
            while (true) {
                try {
                    choice = input.nextInt();
                    if (choice > 0 && choice < 3) {
                        break;
                    } else {
                        System.out.printf("|%-5sEnter Choice Again: ", "");
                    }
                } catch (Exception e) {
                    input.next();
                    System.out.printf("|%-5sEnter Choice Again: ", "");
                }
            }
            switch (choice) {
                case 1:
                    b.view();
                    IndividualView();
                    break;
                default:
                    exit = false;
                    break;
            }
        } while (exit);
    }

    private void IndividualView() {
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**Individual Business Report**", "");
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**!Enter 0 in ID to Exit!**", "");
        System.out.print("|\tEnter Business ID to View: ");

        int b_id;
        while (true) {
            try {
                b_id = input.nextInt();
                if (doesIDExists(b_id, conf)) {
                    break;
                } else if (b_id == 0) {
                    exit = false;
                    break;
                } else {
                    System.out.print("|\tEnter Business ID to View Again: ");
                }
            } catch (Exception e) {
                input.next();
                System.out.print("|\tEnter Business ID to View Again: ");
            }
        }

        if (exit) {
            try {
                String businessSQL = "SELECT B_Name, B_Founder, B_Contact, B_Status FROM B_Business WHERE B_Id = ?";
                PreparedStatement businessStmt = conf.connectDB().prepareStatement(businessSQL);
                businessStmt.setInt(1, b_id);
                ResultSet businessRs = businessStmt.executeQuery();

                if (businessRs.next()) {
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    System.out.printf("|%-25s%-50s%-25s|\n", "", "Business Information", "");
                    System.out.printf("|%-15s: %-83s|\n", "Business Name", businessRs.getString("B_Name"));
                    System.out.printf("|%-15s: %-83s|\n", "Founder", businessRs.getString("B_Founder"));
                    System.out.printf("|%-15s: %-83s|\n", "Contact", businessRs.getString("B_Contact"));
                    System.out.printf("|%-15s: %-83s|\n", "Status", businessRs.getString("B_Status"));
                    System.out.println("+----------------------------------------------------------------------------------------------------+");

                    String employeeSQL = "SELECT E_Fname, E_Mname, E_Lname, E_Bdate, E_Gender, E_Contact, E_Status, E_Cdate " +
                                         "FROM E_Employee WHERE B_Id = ?";
                    PreparedStatement employeeStmt = conf.connectDB().prepareStatement(employeeSQL);
                    employeeStmt.setInt(1, b_id);
                    ResultSet employeeRs = employeeStmt.executeQuery();

                    System.out.printf("|%-25s%-50s%-25s|\n", "", "**Employee History**", "");
                    System.out.println("+------------------------------------------------------------------------------------------------------------------------------+");
                    System.out.printf("| %-22s | %-22s | %-22s | %-22s | %-22s | %-22s | %-22s | %-22s |\n",
                            "First Name", "Middle Name", "Last Name", "Birth Date", "Gender", "Contact", "Status", "Hired Date");
                    System.out.println("+------------------------------------------------------------------------------------------------------------------------------+");

                    boolean hasEmployees = false;
                    while (employeeRs.next()) {
                        hasEmployees = true;
                        System.out.printf("| %-22s | %-22s | %-22s | %-22s | %-22s | %-22s | %-22s | %-22s |\n",
                                employeeRs.getString("E_Fname"),
                                employeeRs.getString("E_Mname"),
                                employeeRs.getString("E_Lname"),
                                employeeRs.getString("E_Bdate"),
                                employeeRs.getString("E_Gender"),
                                employeeRs.getString("E_Contact"),
                                employeeRs.getString("E_Status"),
                                employeeRs.getString("E_Cdate"));
                    }

                    if (!hasEmployees) {
                        System.out.printf("|%-25s%-50s%-25s|\n", "", "!!No Employee History!!", "");
                    }

                    System.out.println("+------------------------------------------------------------------------------------------------------------------------------+");

                    businessRs.close();
                    employeeRs.close();
                    businessStmt.close();
                    employeeStmt.close();
                } else {
                    System.out.println("|\tNo record found for Business ID: " + b_id + " |");
                }

            } catch (Exception e) {
                System.out.println("|\tError retrieving data: " + e.getMessage() + " |");
            }
        }
    }
    private boolean doesIDExists(int id, config conf) {
        String query = "SELECT COUNT(*) FROM B_Business WHERE B_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking Business ID: " + e.getMessage());
        }
        return false;
    }
}