package com.bluecatpixel.rssfeedreader.utils;

import android.graphics.Bitmap;

/**
 * @author josericardosantos (Blue Cat Pixel)
 *         <p/>
 *         Cache Memory to save images
 *         The size of the cache will be at least 1 MBytes and normally 25% of heap size
 *         (in that case I think 25% is not problematic but with other apps that requires
 *         more memory for other things we can reduce to 12.5% that is 1/8 of heap size)
 */
public class MemoryCache {

    //minimum limit memory - one MegaByte
    private static final int CACHE_MIN_LIM = 1048576;

    private BitmapLruCache mCache;
    private long size = 0;

    public MemoryCache() {
        //uses 25% of available heap size with a minimum of 1 MB
        int heapSizeLim = (int) (Runtime.getRuntime().maxMemory() / 4);
        int lim = heapSizeLim > CACHE_MIN_LIM ? heapSizeLim : CACHE_MIN_LIM;

        mCache = new BitmapLruCache(lim);
    }

    public Bitmap get(String key) {

        return mCache.get(key);
    }

    public void put(String key, Bitmap bitmap) {
        if (get(key) == null) {
            mCache.put(key, bitmap);
        }
    }

    public synchronized boolean isImageInCache(String key) {
        return mCache.get(key) != null;
    }

    // clears all entries in the cache
    public void clear() {

        mCache.evictAll();
    }

}