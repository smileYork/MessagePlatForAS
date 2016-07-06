package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeAndDateType {

    private static Date date = new Date();

    public static String nowDateNoTime() {

        SimpleDateFormat sfForDate = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());

        return sfForDate.format(date);
    }

    public static String nowDateWithTime() {
        SimpleDateFormat sfForDate = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        return sfForDate.format(date);
    }

    public static String nowTime() {
        SimpleDateFormat sfForDate = new SimpleDateFormat("HH:mm:ss",
                Locale.getDefault());

        return sfForDate.format(date);
    }

    public static String GetStringFromLong(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault());
        Date dt = new Date(millis);
        return sdf.format(dt);
    }

}
