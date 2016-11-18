/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolpc.desktop;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author varun
 */
public class Utility {
    public String getDate(String date, String dateFormat) {
        long milliSeconds = Long.parseLong(date);
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date. 
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date. 
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public String getDuration(int duration) {
        String time = "";
        duration /= 1000;//in seconds
        int minutes = duration / 60;
        duration %= 60;
        if (minutes > 0) {
            time += minutes + " mins ";
        }
        time += duration + " secs";
        return time;
    }

    public String getSize(int size) {
        size /= 1024;
        return size + "KB";
    }

    public String getSize(long size) {
        size /= 1024;
        return size + "KB";
    }
}
