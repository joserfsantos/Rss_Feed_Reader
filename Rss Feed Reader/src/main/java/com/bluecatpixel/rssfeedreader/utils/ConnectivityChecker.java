package com.bluecatpixel.rssfeedreader.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author josericardosantos (Blue Cat Pixel)
 *         <p/>
 *         Checks the internet connectivity
 */
public class ConnectivityChecker {

    public static boolean hasInternetConnectivity(Context context) {
        ConnectivityManager connection = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connection != null) {
            NetworkInfo info = connection.getActiveNetworkInfo();
            if (info != null) {
                return info.isConnected();
            }
        }

        return false;
    }

}
