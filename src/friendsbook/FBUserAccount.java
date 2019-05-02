/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package friendsbook;

import java.util.Date;

/**
 *
 * @author ankcs
 */
public class FBUserAccount {
    private String acctID;
    private String password;
    private String name;
    private String school;
    private int gender;
    private String dob;
    
    
    public FBUserAccount(String AcctID, String Password,String Name,String School,int Gender,String Dob)
    {
        acctID=AcctID;
        password=Password;
        name=Name;
        school=School;
        gender=Gender;
        dob=Dob;
        
    }

    public String getAcctID() {
        return acctID;
    }

    public void setAcctID(String acctID) {
        this.acctID = acctID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGender() {
        if(gender==1)
           return "Female";
        
        if(gender==2)
           return "Male" ;
       
           return "Not Specified";
    }

    public void setGender(String gender) {
        if(gender.equals("Female")) 
            this.gender=1;
        
        else if(gender.equals("Male"))
            this.gender=2;
        else
            this.gender=3;
        
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
    
}
