/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package friendsbook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author ankcs
 */
public class FBDateAndTime {

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    //public static String dt;
    //return a String of the current system date and time

    public static String DateTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    public static ArrayList<FBUpdateAndPost> SortPostsByTime(ArrayList<FBUpdateAndPost> updateAndPost) {

        try {
            for (int i = 0; i < updateAndPost.size() - 1; i++) {
                Date date1 = updateAndPost.get(i).getDate();
                for (int j = i + 1; j < updateAndPost.size(); j++) {
                    Date date2 = updateAndPost.get(j).getDate();
                    if (date1.compareTo(date2) < 0) {
                        FBUpdateAndPost t = updateAndPost.get(i);
                        updateAndPost.set(i, updateAndPost.get(j));
                        updateAndPost.set(j, t);
                    }
                }
            }
            return updateAndPost;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

}
