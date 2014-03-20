package com.bluecatpixel.rssfeedreader.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.bluecatpixel.rssfeedreader.utils.FileCache;
import com.bluecatpixel.rssfeedreader.utils.MemoryCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.bluecatpixel.rssfeedreader.model.Constants.DEFAULT_TIMEOUT;

/**
 * @author josericardosantos (Blue Cat Pixel)
 *         <p/>
 *         Simple implementation of Lazzy List
 */
public class ImageLoaderTask {

    private MemoryCache mMemoryCache = new MemoryCache();
    private FileCache mFileCache;
    private Map<ImageView, String> mImageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService mExecService;

    //handler to display images in UI thread
    private Handler handler = new Handler();

    public ImageLoaderTask(Context context) {
        mFileCache = new FileCache(context);
        mExecService = Executors.newFixedThreadPool(10);
    }

    private static void copyStream(InputStream is, OutputStream os) throws IOException {
        final int buffer_size = 1024;

        byte[] bytes = new byte[buffer_size];
        for (; ; ) {
            int count = is.read(bytes, 0, buffer_size);
            if (count == -1) {
                break;
            }
            os.write(bytes, 0, count);
        }
    }

    public void displayImage(String url, ImageView imageView) {
        mImageViews.put(imageView, url);

        Bitmap bitmap = mMemoryCache.get(url);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            recycleBitmap(url, bitmap);
        } else {
            queueImages(url, imageView);
        }
    }

    private synchronized void recycleBitmap(String key, Bitmap bitmap) {
        if (!bitmap.isRecycled() && !mMemoryCache.isImageInCache(key)) {
            bitmap.recycle();
        }
    }

    private void queueImages(String url, ImageView imageView) {
        ImagesToLoad p = new ImagesToLoad(url, imageView);
        mExecService.submit(new ImagesLoader(p));
    }

    private Bitmap getBitmap(String url) {

        File file = mFileCache.getFile(url);

        // from cache file
        Bitmap bitmap = decodeFile(file);
        if (bitmap != null) {

            return bitmap;
        }

        // download from web and saves it in cache file
        try {
            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();

            // Sets timeout
            connection.setConnectTimeout(DEFAULT_TIMEOUT);
            connection.setReadTimeout(DEFAULT_TIMEOUT);

            connection.setInstanceFollowRedirects(true);
            InputStream is = connection.getInputStream();
            OutputStream os = new FileOutputStream(file);
            copyStream(is, os);
            os.close();
            connection.disconnect();

            bitmap = decodeFile(file);

            return bitmap;

        } catch (Throwable t) {
            t.printStackTrace();
            if (t instanceof OutOfMemoryError) {
                mMemoryCache.clear();
            }
            return null;
        }
    }

    //decodes image from file
    private Bitmap decodeFile(File file) {

        // the images on the Rss Feed are small so there's no need to sample then
        // if they were too big we should create a sample by using the BitmapFactory.Options
        // to define the new size
        FileInputStream stream = null;

        try {
            //decode
            BitmapFactory.Options options = new BitmapFactory.Options();

            stream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
            stream.close();

            return bitmap;
        } catch (FileNotFoundException e) {
            // The file dont exist - No problem
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private boolean imageViewReused(ImagesToLoad imageToLoad) {

        String tag = mImageViews.get(imageToLoad.imageView);

        if (tag == null || !tag.equals(imageToLoad.url)) {
            return true;
        }
        return false;
    }

    public void clearAllResources() {
        mMemoryCache.clear();
        mFileCache.clear();
    }

    // Task for set the imageView's bitmaps
    private class ImagesToLoad {
        public String url;
        public ImageView imageView;

        public ImagesToLoad(String url, ImageView img) {
            this.url = url;
            this.imageView = img;
        }
    }

    class ImagesLoader implements Runnable {

        ImagesToLoad imageToLoad;

        ImagesLoader(ImagesToLoad imageToLoad) {
            this.imageToLoad = imageToLoad;
        }

        @Override
        public void run() {
            try {
                if (imageViewReused(imageToLoad)) {
                    return;
                }
                Bitmap bitmap = getBitmap(imageToLoad.url);
                mMemoryCache.put(imageToLoad.url, bitmap);
                if (imageViewReused(imageToLoad)) {
                    return;
                }
                BitmapDisplayer bitmapDisplayer = new BitmapDisplayer(bitmap, imageToLoad);
                handler.post(bitmapDisplayer);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    // display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        ImagesToLoad imageToLoad;

        public BitmapDisplayer(Bitmap bitmap, ImagesToLoad imageToLoad) {
            this.bitmap = bitmap;
            this.imageToLoad = imageToLoad;
        }

        public void run() {
            if (imageViewReused(imageToLoad)) {
                return;
            }
            if (bitmap != null) {
                imageToLoad.imageView.setImageBitmap(bitmap);
                recycleBitmap(imageToLoad.url, bitmap);
            }

        }
    }
}
