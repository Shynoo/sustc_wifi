package cn.sustc.edu.wifi;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dengshougang on 16/5/10.
 */
public class TimeLog {
//    static String logtemp = "{0} date/time is: {1} \r\nuse time is {2} s {3} ms.";
    static SimpleDateFormat formatter ;
    public static String timeInfo() {
        return timeInfo("MM-dd HH:mm:ss");
    }
    public static String timeInfo(String format){
        Date date = new Date();
        formatter= new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return "["+dateString+"] ";
    }

}
