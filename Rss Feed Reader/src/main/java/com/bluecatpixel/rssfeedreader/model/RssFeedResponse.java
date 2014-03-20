package com.bluecatpixel.rssfeedreader.model;

/**
 * @author josericardosantos (Blue Cat Pixel)
 *         <p/>
 *         class to transport the answer between activity and task
 */
public class RssFeedResponse {

    private Channel mChannel;
    private Exception mException;

    public RssFeedResponse() {

    }

    public Exception getException() {
        return mException;
    }

    public void setException(Exception exception) {
        this.mException = exception;
    }

    public Channel getChannel() {
        return mChannel;
    }

    public void setChannel(Channel channel) {
        this.mChannel = channel;
    }

}
