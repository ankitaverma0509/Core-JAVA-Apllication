/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package friendsbook;

import java.util.Scanner;

/**
 *
 * @author ankcs
 */
public class FriendsBook {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
          //declare varaibles
        Scanner input = new Scanner(System.in);
        String selection = "";
        
        while(!selection.equals("x"))
        {
            //display the menu
            System.out.println("Welcome to FriendsBook");
            System.out.println("Please make your selection");
            System.out.println("1: Create a new Account");
            System.out.println("2: Login your account");
            System.out.println("x: Finish the simulation");
            
            //get the selection from the user
            selection = input.nextLine();
            System.out.println();
            
            if(selection.equals("1"))
            {
                //register
                FBLoginRegister.register();
                 
            }
            else if(selection.equals("2"))
            {
                //login
               FBLoginRegister.login();
            }
            
            
        }
    }
    
    
    
}
