package com.bluecatpixel.rssfeedreader.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by josericardosantos (Blue Cat Pixel)
 */
public class BitmapLruCache extends LruCache<String, Bitmap> {

    public BitmapLruCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {

        if (!oldValue.isRecycled()) {
            oldValue.recycle();
        }

        super.entryRemoved(evicted, key, oldValue, newValue);
    }
}
