/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package friendsbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author ankcs
 */
public class FBLoginRegister {

    // 1 --> Female
    // 2 --> Male
    // 3 --> Not Specified
    public static void register() {
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();

            String acctID = "";
            String password = "";
            String name = "";
            String school = "";
            int gender = 0;
            String dob = null;
            Scanner input = new Scanner(System.in);
            //   String regularExpression = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!*#?&])[A-Za-z\\d@$!%*#?&]{3,10}$\"";

            int count = 0;
            //prompts and input

            int valid = -1;
            do {
                if (valid == 1) {
                    System.out.println("Invalid account ID." + "\n" + "ID must have atleast one letter, one digit, one special character(# ? ! *) and range between 3-10");
                }
                if (valid == 2) {
                    System.out.println("AcctID already Exists");
                }
                if (valid != 0) {
                    System.out.println("Please enter your Account ID");
                }
                acctID = input.nextLine();
                if (acctID.equals("x")) {
                    return;
                }
                valid = isValid(acctID);

            } while (valid != 0);

            System.out.println("Please enter your Password");
            do {
                if (count++ > 0) {
                    System.out.println("Invalid password " + "\n" + "Password must not be same as acctount ID and range between 8-16");
                }
                password = input.nextLine();
                if (password.equals("x")) {
                    return;
                }
            } while (password.length() < 8 || password.length() > 16 || password.equals(acctID));

            System.out.println("Please enter your Name");
            name = input.nextLine();
            if (name.equals("x")) {
                return;
            }

            System.out.println("Please enter your School");
            school = input.nextLine();
            if (school.equals("x")) {
                return;
            }
            System.out.println("Please enter your Date of Birth");
            dob = input.nextLine();

            System.out.println("Please select your Gender");
            System.out.println("Select 1 for Female");
            System.out.println("Select 2 for Male");
            System.out.println("Select 3 for Not Specified");
            gender = input.nextInt();
//            if (gender == Integer.parseInt("x")) {
//                return;
//            }

//            if (dob.equals("x")) {
//                return;
//            }
            //insert a record
            st.executeUpdate("Insert into FBUserAccount values('" + acctID + "', '" + password + "','" + name + "','" + school + "'," + gender + ",'" + dob + "')");
            System.out.println("Account creation successfull\n");
            rs = st.executeQuery("select * from FBUserAccount");
            rs.next();
            FBUserAccount account = new FBUserAccount(rs.getString("acctID"), rs.getString("password"),
                    rs.getString("name"), rs.getString("school"),
                    rs.getInt("gender"), rs.getString("dob"));
            FBHome.homepage(account);
                //System.out.println("Succesfull");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                st.close();
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void login() {
        //we need id and password
        Scanner input = new Scanner(System.in);
        String acctID = "";
        String password = "";
        boolean idFound = false;

        //get the login info.
        System.out.println("Please enter your Account ID");
        acctID = input.next();
        System.out.println("Please enter your password");
        password = input.next();

        //access the database and then login
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
            // do a query to make sure that id and ssn is not used

            rs = st.executeQuery("select * from FBUserAccount where acctID = '" + acctID + "'");
            if (rs.next()) {
                if (acctID.equals(rs.getString("acctID")) && password.equals(rs.getString("password"))) {
                    FBUserAccount account = new FBUserAccount(rs.getString("acctID"), rs.getString("password"),
                            rs.getString("name"), rs.getString("school"),
                            rs.getInt("gender"), rs.getString("dob"));
                    FBHome.homepage(account);
                    System.out.println("Succesfully Logged in");

                }

            } else {
                System.out.println("Wrong Account ID or Password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                st.close();
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static int isValid(String acctID) {
        //access the database and then login
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();

            rs = st.executeQuery("select * from FBUserAccount where acctID = '" + acctID + "'");
            if (rs.next()) {
                return 2;
            }
            if (acctID.length() < 3 || acctID.length() > 10) {
                return 1;
            }
            boolean containsDigits = false;
            for (int i = 0; i < acctID.length(); i++) {
                if (acctID.charAt(i) >= '0' && acctID.charAt(i) <= '9') {
                    containsDigits = true;
                }
            }
            if (containsDigits == false) {
                return 1;
            }
            boolean containsSpecChar = false;
            {
                for (int i = 0; i < acctID.length(); i++) {
                    if (acctID.charAt(i) == '#' || acctID.charAt(i) == '?' || acctID.charAt(i) == '!' || acctID.charAt(i) == '*') {
                        containsSpecChar = true;
                    }
                }
                if (containsSpecChar == false) {
                    return 1;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                st.close();
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

}
