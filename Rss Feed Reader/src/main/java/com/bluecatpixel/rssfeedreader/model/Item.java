package com.bluecatpixel.rssfeedreader.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author josericardosantos (Blue Cat Pixel)
 *         <p/>
 *         Rss Feed Item
 */
public class Item {

    private String mTitle;
    private String mDescription;
    private String mLink;
    private Guid mGuid;
    private String mPubDate;
    private List<Thumbnail> mThumbnails;

    public Item() {
        mThumbnails = new ArrayList<Thumbnail>();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public String getPubDate() {
        return mPubDate;
    }

    public void setPubDate(String pubDate) {
        this.mPubDate = pubDate;
    }

    public Guid getGuid() {
        return mGuid;
    }

    public void setGuid(Guid guid) {
        this.mGuid = guid;
    }

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }

    public void setThumbnails(List<Thumbnail> thumbnails) {
        this.mThumbnails = thumbnails;
    }

}
