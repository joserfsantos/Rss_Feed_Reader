package com.bluecatpixel.rssfeedreader.model;

/**
 * @author josericardosantos (Blue Cat Pixel)
 *         <p/>
 *         Thumbnail of the item Rss Feed
 */
public class Thumbnail {

    private String mUrl;
    private int mWidth;
    private int mHeight;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }
}
