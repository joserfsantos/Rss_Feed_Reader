package com.bluecatpixel.rssfeedreader.utils;

/**
 * @author josericardosantos (Blue Cat Pixel)
 */
public class Utils {

    public static int parseToInt(String str) {
        int i;

        try {
            i = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }

        return i;
    }
}
