package fr.unice.polytech.freetimealgorithm.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hakim on 14/06/14.
 */
public class DateTools {
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yy-MM-dd  HH:mm:ss");

    public static String print(long date) {
        return dateFormatter.format(new Date(date));
    }

    public static double msToHours(long miliseconds) {
        return (double)miliseconds/3600000;
    }

    public static long hoursToMilis(double hours) {
        return (long)(hours * 3600000);
    }
}
