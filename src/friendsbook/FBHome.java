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
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author ankcs
 */
public class FBHome {

    public static void homepage(FBUserAccount account) {
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        //Arraylist to store all the notifications.
        ArrayList<FBNotification> notifications = new ArrayList<FBNotification>();
        Scanner input = new Scanner(System.in);
        String selection = "";
        int count = 0;
        try {
      //Database connection open
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
      //checking if there is any data in notification table where receiver is the current logged in user.
            rs = st.executeQuery("select * from fbnotification where receiver ='" + account.getAcctID() + "' order by date desc");
            while (rs.next()) {
      //adding those notification in to arrayList.
                notifications.add(new FBNotification(rs.getString("notiID"), rs.getString("sender"), rs.getString("receiver"), rs.getInt("type"), rs.getString("content"), rs.getInt("status")));
                if (rs.getInt("status") == 0) {
                    count++;
                }
            }

        
        while (!selection.equals("x")) {
            //display the menu
            System.out.println("welcome " + account.getName());
            ////////////////// Recent 3 Posts and updates from friends////////////////
            
            
           
                ArrayList<FBUpdateAndPost> updateAndPost = new ArrayList<FBUpdateAndPost>();
                ArrayList<String> friends = new ArrayList<String>();

                rs = st.executeQuery("select * from FBFriend");
                System.out.println("Your Friend's Updates and posts: ");
                while (rs.next()) {
                    if ((rs.getString("FrndID1").equals(account.getAcctID()))) {
                        friends.add(rs.getString("FrndID2"));
                    } else if ((rs.getString("FrndID2").equals(account.getAcctID()))) {
                        friends.add(rs.getString("FrndID1"));
                    }
                }
               
                for (String friend : friends) {
                    
                    rs = st.executeQuery("select * from FBUpdateAndPost where acctID='" + friend + "' order by date desc limit 3");
                    while (rs.next()) {
                        updateAndPost.add(new FBUpdateAndPost(rs.getString("postID"), rs.getString("acctID"), rs.getInt("type"), rs.getString("content"), rs.getTimestamp("date")));
                    }
                }

                updateAndPost = FBDateAndTime.SortPostsByTime(updateAndPost);

              
                for (int i = 0; i < updateAndPost.size(); i++) {
                    {
                        if(i>2)
                        {
                            break;
                        }
                        if (updateAndPost.get(i).getType() == 1) {
                            System.out.println( updateAndPost.get(i).getAcctID() + " has posted " + updateAndPost.get(i).getContent() + " at " + updateAndPost.get(i).getDate());
                        }
                        if (updateAndPost.get(i).getType() == 0) {
                            System.out.println( updateAndPost.get(i).getAcctID() + " has updated " + updateAndPost.get(i).getContent() + " at " + updateAndPost.get(i).getDate());
                        }
                    }
                } 
            
            
            /////////////////////////////////////////////////////////////////////////////
            System.out.println("Please make your selection");
            System.out.println("1: Check Notification new (" + count + ")");
            count = 0;
            System.out.println("2: Create New Post");
            System.out.println("3: Select Update and Post");
            System.out.println("4: See Friends");
            System.out.println("5: Send Friend Request");
            System.out.println("6: Send A Message");
            System.out.println("7: Update Profile");
            System.out.println("8: See Hashtag in trends");
            System.out.println("x: Finish the simulation");

            //get the selection from the user
            selection = input.nextLine();
            System.out.println();

            if (selection.equals("1")) {
                //Check Notification
                FBFunctions.notification(notifications, account);

            } else if (selection.equals("2")) {

                FBFunctions.createPost(account);

            } else if (selection.equals("3")) {
                FBFunctions.selectUpdateAndPost(account);

            } else if (selection.equals("4")) {
                FBFunctions.friendList(account);

            } else if (selection.equals("5")) {
                //Send Friend Request
                FBFunctions.sendFriendRequest(account);

            } else if (selection.equals("6")) {
                //Send A Message
                FBFunctions.sendMessage(notifications, account);

            } else if (selection.equals("7")) {
                //Update Profile
                FBFunctions.updateProfile(account);

            } else if (selection.equals("8")) {
                FBFunctions.hashTag(account);

            } else if (selection.equals("x")) {
                //exit

            }

        }
    } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                st.close();
                //rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
