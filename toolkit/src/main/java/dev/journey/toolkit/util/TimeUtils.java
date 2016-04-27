package dev.journey.toolkit.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mwp on 16/4/27.
 */
public class TimeUtils {

    /**
     * @return
     */
    public static String createTimeStamp() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return timeStamp;
    }
}
