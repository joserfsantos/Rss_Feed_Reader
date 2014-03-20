package com.bluecatpixel.rssfeedreader.model;

/**
 * @author josericardosantos (Blue Cat Pixel)
 */
public class Guid {

    private boolean isPermaLink;
    private String mGuid;

    public boolean isPermaLink() {
        return isPermaLink;
    }

    public void setPermaLink(boolean isPermaLink) {
        this.isPermaLink = isPermaLink;
    }

    public String getGuid() {
        return mGuid;
    }

    public void setGuid(String guid) {
        this.mGuid = guid;
    }

}
