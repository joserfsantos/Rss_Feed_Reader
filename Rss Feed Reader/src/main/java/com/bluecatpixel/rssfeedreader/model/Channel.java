package com.bluecatpixel.rssfeedreader.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author josericardosantos (Blue Cat Pixel)
 *         <p/>
 *         BBC Channel
 */
public class Channel {

    private String mTitle;
    private String mLink;
    private String mDescription;
    private String mLanguage;
    private String mLastBuildDate;
    private String mCopyright;

    private Image mImage;

    private int mTtl;

    private List<Item> mItems;

    public Channel() {
        mItems = new ArrayList<Item>();
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        this.mLanguage = language;
    }

    public String getLastBuildDate() {
        return mLastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.mLastBuildDate = lastBuildDate;
    }

    public String getCopyright() {
        return mCopyright;
    }

    public void setCopyright(String copyright) {
        this.mCopyright = copyright;
    }

    public Image getImage() {
        return mImage;
    }

    public void setImage(Image image) {
        this.mImage = image;
    }

    public List<Item> getItems() {
        return mItems;
    }

    public void setItems(List<Item> items) {
        this.mItems = items;
    }

    public int getTtl() {
        return mTtl;
    }

    public void setTtl(int ttl) {
        this.mTtl = ttl;
    }

}
