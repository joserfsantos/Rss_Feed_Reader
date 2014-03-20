package com.bluecatpixel.rssfeedreader.utils;

import android.content.Context;

import java.io.File;
import java.util.Locale;

/**
 * @author josericardosantos (Blue Cat Pixel)
 *         <p/>
 *         File cache to save temporarily the images of the lazzy list
 */
public class FileCache {

    private static final String FILE_PREFIX = "temp_";
    private static final String FILE_EXT = ".jpg";

    private File mCacheDir;

    public FileCache(Context context) {
        // Saves the file in the cache directory
        mCacheDir = context.getCacheDir();
    }

    public File getFile(String url) {

        // Saves the temp file with a masked name
        String filename = String.format(Locale.US, "%s%d%s", FILE_PREFIX, url.hashCode(), FILE_EXT);

        return new File(mCacheDir, filename);

    }

    public void clear() {
        File[] files = mCacheDir.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            f.delete();
        }
    }

}