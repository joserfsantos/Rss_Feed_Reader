package com.bluecatpixel.rssfeedreader.model;

/**
 * @author josericardosantos (Blue Cat Pixel)
 *         <p/>
 *         BBC Image
 */
public class Image {

    private String mUrl;
    private String mTitle;
    private String mLink;
    private int mWidth;
    private int mHeight;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

}
