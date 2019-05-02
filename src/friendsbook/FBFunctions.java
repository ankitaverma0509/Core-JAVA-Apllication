/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package friendsbook;

import static java.lang.Integer.parseInt;
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
public class FBFunctions {

// status 1--> seen
// status 0-->unseen 
// type 0=friendRequest 
// type 1=message 
    // type 1=post
    // type 0=update
    public static void notification(ArrayList<FBNotification> notifications, FBUserAccount account) {
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
            Scanner input = new Scanner(System.in);
            String acctID = "";
            String content = "";
            String selection = "";
            do {
                for (int i = 0; i < notifications.size(); i++) {
                    System.out.println((i + 1) + ".  " + notifications.get(i).toString());
                    acctID = notifications.get(i).getSender();
                }
                System.out.println("Select the Number to perform action ");
                System.out.println("select x to finish the simulation");
                selection = input.nextLine();

                if (selection.equals("x")) {
                    return;
                }
                int i = parseInt(selection) - 1;
                // to chnage status from unseen to seen

                st.executeUpdate("update fbnotification set status='1'");

                if (notifications.get(i).getType() == 1) {
                    while (!selection.equals("x")) {

                        System.out.println("Reply  " + notifications.get(i).getSender() + "'s message");

                        System.out.println(" Write message up to 20 characters");
                        content = input.nextLine();
                        if (content.length() > 0) {
                            st.executeUpdate("Insert into fbnotification (sender,receiver,type,content, status,date) values('" + account.getAcctID() + "', '" + acctID + "', '" + 1 + "', '" + content + "', '" + 0 + "','" + FBDateAndTime.DateTime() + "')");
                            System.out.println("Message sent\n");
                        }

                        System.out.println("select x to finish the simulation or press enter to send new message");
                        selection = input.nextLine();
                    }
                } else if (notifications.get(i).getType() == 0) {
                    String frndID1 = account.getAcctID();
                    String frndID2 = notifications.get(i).getSender();
                    System.out.println(" Select 1 to Accept " + notifications.get(i).getSender() + "'s Friend request");
                    System.out.println(" Select 2 to deny " + notifications.get(i).getSender() + "'s Friend request");
                    System.out.println(" Select x to finish the simulation");

                    selection = input.nextLine();
                    if (selection.equals("1")) {

                        st.executeUpdate("Insert into fbfriend(FrndID1,FrndID2) values('" + frndID1 + "', '" + frndID2 + "')");
                        System.out.println("Request Accepted\n");

                    }
                    if (selection.equals("1") || selection.equals("2")) {

                        st.executeUpdate("DELETE FROM fbnotification WHERE  notiID ='" + notifications.get(i).getNotiID() + "'");
                        notifications.remove(i);
                        if (selection.equals("2")) {
                            System.out.println("Request Denied");
                        }

                    }

                }

            } while (true);
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

    public static void sendFriendRequest(FBUserAccount account) {

        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
            Scanner input = new Scanner(System.in);
            String acctID = "";
            String content = account.getName() + " wants to be your friend.";

            System.out.println("Enter the Friend's account ID");
            acctID = input.nextLine();

            rs = st.executeQuery(("select acctID from fbuseraccount where acctID='" + acctID + "'"));
            if (rs.next()) {
                rs = st.executeQuery("select * from fbfriend where FrndID2='" + acctID + "' and FrndID1='" + account.getAcctID() + "'");
                if (rs.next()) {
                    System.out.println(account.getName() + " is already friends with  " + acctID);
                    return;
                }

                rs = st.executeQuery("select * from fbnotification where sender='" + account.getAcctID() + "' and receiver='" + acctID + "' and type='" + 0 + "'");
                if (rs.next()) {
                    System.out.println("Already sent a friend Request");
                    return;
                }

                if (account.getAcctID().equals(acctID)) {
                    System.out.println("Can not send friend Request");
                    return;
                } else {
                    st.executeUpdate("Insert into fbnotification (sender,receiver,type,content, status,date) values('" + account.getAcctID() + "', '" + acctID + "', '" + 0 + "', '" + content + "', '" + 0 + "','" + FBDateAndTime.DateTime() + "')");
                    System.out.println("Request sent\n");
                }
            } else {
                System.out.println(acctID + " ID doesn't exist in FriendsBook");

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

    public static void sendMessage(ArrayList<FBNotification> notifications, FBUserAccount account) {

        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
            ArrayList<FBNotification> notifications_msg = new ArrayList<FBNotification>();
            rs = st.executeQuery("select * from fbnotification");
            while (rs.next()) {
                notifications_msg.add(new FBNotification(rs.getString("notiID"), rs.getString("sender"), rs.getString("receiver"), rs.getInt("type"), rs.getString("content"), rs.getInt("status")));

            }
            Scanner input = new Scanner(System.in);
            String acctID = "";
            String content = "";
            String selection = "";
            System.out.println("Enter the Friend's account ID");
            acctID = input.nextLine();
            rs = st.executeQuery("select * from FBUserAccount where acctID = '" + acctID + "'");
            if (rs.next()) {
                //rs = st.executeQuery("select content from fbnotification where sender='" + account.getAcctID() + "' and receiver='" + acctID + "'");
                for (int i = 0; i < notifications_msg.size(); i++) {
                    if (notifications_msg.get(i).getType() == 1) {
                        if ((notifications_msg.get(i).getSender().equals(account.getAcctID()) && notifications_msg.get(i).getReceiver().equals(acctID)) || (notifications_msg.get(i).getSender().equals(acctID) && notifications_msg.get(i).getReceiver().equals(account.getAcctID()))) {
                            System.out.println(notifications_msg.get(i).getContent());
                        }
                    }
                }
            } else {
                System.out.println("ASccount ID dosen't exists");
                return;
            }
            while (!selection.equals("x")) {
                System.out.println("Write message up to 20 characters");
                content = input.nextLine();
                if (content.length() > 0) {
                    int r = st.executeUpdate("Insert into fbnotification (sender,receiver,type,content, status, date) values('" + account.getAcctID() + "', '" + acctID + "', '" + 1 + "', '" + content + "', '" + 0 + "','" + FBDateAndTime.DateTime() + "')");
                    System.out.println("Message sent\n");
                }
                System.out.println("select x to finish the simulation or press enter to send new message");
                selection = input.nextLine();
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

    public static void createPost(FBUserAccount account) {
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
            Scanner input = new Scanner(System.in);
            String content = "";

            System.out.println("Create Post");
            content = input.nextLine();
            content = content.trim();
            content += " ";

            ArrayList<String> hashTags = new ArrayList<String>();
            //create post by inserting record
            st.executeUpdate("Insert into FBUpdateAndPost(acctID,type, content,date) values('" + account.getAcctID() + "', '" + 1 + "', '" + content + "', '" + FBDateAndTime.DateTime() + "')");
            rs = st.executeQuery("select postId from fbupdateandpost where acctID='" + account.getAcctID() + "' order by date desc limit 1");
            rs.next();
            String postID = rs.getString("postID");
            for (int i = 0; i < content.length(); i++) {
                if (content.charAt(i) == '#') {
                    for (int j = i + 1; j < content.length(); j++) {
                        if (content.charAt(j) == ' ') {
                            hashTags.add(content.substring(i, j));
                            i = j;
                            break;
                        }
                    }
                }
            }

            for (String hashTag : hashTags) {
                rs = st.executeQuery("select * from fbhashtag where hashtag = '" + hashTag + "'");
                if (rs.next()) {
                    System.out.println(rs.getString("count"));
                    int count = Integer.parseInt(rs.getString("count")) + 1;
                    String postIDs = rs.getString("postIDs");
                    postIDs += "," + postID;
                    st.executeUpdate("update fbhashtag set count ='" + count + "', postids='" + postIDs + "' where hashtag='" + hashTag + "'");
                } else {
                    String count = "1";
                    st.executeUpdate("insert into fbhashtag values ('" + hashTag + "','" + count + "','" + postID + "')");

                }
            }
            System.out.println("Successfully posted\n");

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

    public static void updateProfile(FBUserAccount account) {
        String acctID = "";
        String password = "";
        String name = "";
        String school = "";
        int gender = 0;
        String dob = "";

        Scanner input = new Scanner(System.in);
        String selection = "";

        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();

            //updating columns
            do {
                System.out.println("Please make your selection");
                System.out.println("select 1 to update name");
                System.out.println("select 2 to update School");
                System.out.println("select 3 to update gender");
                System.out.println("select 4 to update Date of Birth");
                System.out.println("select 5 to update Password");
                System.out.println("x: Finish the simulation");

                selection = input.nextLine();
                System.out.println();

                if (selection.equals("1")) {
                    name = input.nextLine();
                    st.executeUpdate("update FBUserAccount set name= '" + name + "' where acctID='" + account.getAcctID() + "'");
                    st.executeUpdate("Insert into FBUpdateAndPost(acctID,type, content,date) values('" + account.getAcctID() + "', '" + 0 + "', '" + name + "', '" + FBDateAndTime.DateTime() + "')");
                    System.out.println("Name Successfully Updated\n");

                } else if (selection.equals("2")) {
                    school = input.nextLine();
                    st.executeUpdate("update FBUserAccount set school= '" + school + "' where acctID='" + account.getAcctID() + "'");
                    st.executeUpdate("Insert into FBUpdateAndPost(acctID,type, content,date) values('" + account.getAcctID() + "', '" + 0 + "', '" + school + "', '" + FBDateAndTime.DateTime() + "')");
                    System.out.println("School Successfully Updated\n");
                } else if (selection.equals("3")) {
                    System.out.println("Select 1 for Female");
                    System.out.println("Select 2 for Male");
                    System.out.println("Select 3 for Not Specified");
                    gender = input.nextInt();
                    st.executeUpdate("update FBUserAccount set gender= '" + gender + "' where acctID='" + account.getAcctID() + "'");
                    st.executeUpdate("Insert into FBUpdateAndPost(acctID,type, content,date) values('" + account.getAcctID() + "', '" + 0 + "', '" + gender + "', '" + FBDateAndTime.DateTime() + "')");
                    System.out.println("Gender Successfully Updated\n");
                } else if (selection.equals("4")) {
                    System.out.println("");
                    dob = input.nextLine();
                    st.executeUpdate("update FBUserAccount set dob= '" + dob + "' where acctID='" + account.getAcctID() + "'");
                    st.executeUpdate("Insert into FBUpdateAndPost(acctID, type, content, date) values('" + account.getAcctID() + "', '" + 0 + "', '" + dob + "', '" + FBDateAndTime.DateTime() + "')");
                    System.out.println("Date of Birth Successfully Updated\n");
                } else if (selection.equals("5")) {
                    System.out.println("");
                    password = input.nextLine();
                    int r = st.executeUpdate("update FBUserAccount set password= '" + password + "' where acctID='" + account.getAcctID() + "'");
                    System.out.println("password Successfully Updated\n");
                }
            } while (!selection.equals("x"));

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

    public static void friendList(FBUserAccount account) {
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
            Scanner input = new Scanner(System.in);
            int count = 0;
            String selection = "";
           
            do {
                
                rs = st.executeQuery("select * from FBFriend");
                System.out.println("Your Friends: ");
                ArrayList<String> friends = new ArrayList<String>();
                while (rs.next()) {
                    if (rs.getString("FrndID1").equals(account.getAcctID())) {

                        friends.add(rs.getString("FrndID2"));
                        //System.out.println(rs.getString("FrndID2"));
                        count++;
                    }
                    if (rs.getString("FrndID2").equals(account.getAcctID())) {
                        friends.add(rs.getString("FrndID1"));
                        //System.out.println(rs.getString("FrndID1"));
                        count++;
                    }
                }
                System.out.println("Total Friends: " + count);
                for (int i = 0; i < friends.size(); i++) {
                    System.out.println(i+1+": " + friends.get(i));
                }

                System.out.println("Select Friend to see the profile");
                System.out.println("Select x to finish the Simulation");
                selection = input.nextLine();

                if (selection.equals("x")) {
                    return;
                }
                int i = parseInt(selection) - 1;
                rs = st.executeQuery(("select * from FBUserAccount where acctID='" + friends.get(i) + "'"));
                while (rs.next()) {
                    System.out.println("Account ID : " + rs.getString("acctID") + "\n Name       : " + rs.getString("name") + "\n Gender     : " + rs.getString("gender")+" (1: Female | 2: Male | 3: Not Specified)" + "\n Date of Birth : " + rs.getString("dob"));
                }
                while (!selection.equals("x")) {

                    System.out.println("select x to finish the simulation or press enter to comment ");
                    selection = input.nextLine();
                }
            } while (true);

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

    public static void selectUpdateAndPost(FBUserAccount account) {

        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
            Scanner input = new Scanner(System.in);
            String selection = "";
            String comment = "";
            String acctID = "";
            do {
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

                int j = 1;
                for (int i = 0; i < updateAndPost.size(); i++) {
                    {
                        if (i > 2) {
                            break;
                        }
                        if (updateAndPost.get(i).getType() == 1) {
                            System.out.println((j) + ": " + updateAndPost.get(i).getAcctID() + " has posted " + updateAndPost.get(i).getContent() + " at " + updateAndPost.get(i).getDate());
                        }
                        if (updateAndPost.get(i).getType() == 0) {
                            System.out.println((j) + ": " + updateAndPost.get(i).getAcctID() + " has updated " + updateAndPost.get(i).getContent() + " at " + updateAndPost.get(i).getDate());
                        }
                        j++;

                    }
                }
                System.out.println("Select the Number to Comment");
                System.out.println("select x to finish the simulation");
                selection = input.nextLine();

                if (selection.equals("x")) {
                    return;
                }
                int i = parseInt(selection) - 1;
                rs = st.executeQuery(("select * from FBComment c, FBUpdateAndPost u where c.postID=u.postID and c.postID='" + updateAndPost.get(i).getPostID() + "'"));
                while (rs.next()) {
                    System.out.println(rs.getString("acctID") + " commented " + rs.getString("comment"));
                }
                while (!selection.equals("x")) {
                    System.out.println("Comment up to 20 characters on  " + updateAndPost.get(i).getAcctID() + "'s post");
                    comment = input.nextLine();
                    if (comment.length() > 0) {
                        st.executeUpdate("Insert into FBComment (acctID,postID,comment) values('" + account.getAcctID() + "', '" + updateAndPost.get(i).getPostID() + "','" + comment + "')");
                        System.out.println("Commented\n");
                    }
                    System.out.println("select x to finish the simulation or press enter to comment ");
                    selection = input.nextLine();
                }
            } while (true);
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

    public static void hashTag(FBUserAccount account) {
        
        ArrayList<String> friends = new ArrayList<String>();
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        Scanner input = new Scanner(System.in);
        String selection = "";

        try {
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
            ArrayList<String> tags = new ArrayList<String>();
            rs = st.executeQuery("select * from fbhashtag order by count desc limit 3");
            int i = 1;
            while (rs.next()) {
                tags.add(rs.getString("hashtag"));
                System.out.println((i) + ". " + rs.getString("hashtag"));
                i++;

            }
            System.out.println("Enter Number to select hashTag to see related posts ");
            System.out.println("Select x to finish the simulation");
            selection = input.nextLine();
            int j=parseInt(selection)-1;
//            try {
//                parseInt(selection);
//            } catch (Exception e) {
//                return;
//            }
            rs = st.executeQuery("select postIDs from fbhashtag where hashtag = '" + tags.get(j) + "'");
            rs.next();
            String[] postsIDs = rs.getString("postIDs").split(",");
            rs = st.executeQuery("select * from FBFriend");
            friends.add(account.getAcctID());
            while (rs.next()) {

                if ((rs.getString("FrndID1").equals(account.getAcctID()))) {
                    friends.add(rs.getString("FrndID2"));
                } else if ((rs.getString("FrndID2").equals(account.getAcctID()))) {
                    friends.add(rs.getString("FrndID1"));
                }

            }
            for (String postID : postsIDs) {
                for (String friend : friends) {
                    rs = st.executeQuery("select * from fbupdateandpost where postid = '" + postID + "' and acctID='" + friend + "'");
                    while (rs.next()) {
                        System.out.println(friend + "'s post is: " + rs.getString("content"));
                    }

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
