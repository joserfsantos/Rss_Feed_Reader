package com.bluecatpixel.rssfeedreader.control;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.bluecatpixel.rssfeedreader.R;
import com.bluecatpixel.rssfeedreader.model.RssFeedResponse;
import com.bluecatpixel.rssfeedreader.utils.ConnectivityChecker;
import com.bluecatpixel.rssfeedreader.xmlparser.RssFeedHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author josericardosantos (Blue Cat Pixel)
 */
public class GetRssFeedTask extends AsyncTaskLoader<RssFeedResponse> {

    private static final String CHAR_SET_UTF_8 = "utf-8";

    private String mUrl;
    private RssFeedResponse mResp;
    private Context mContext;

    public GetRssFeedTask(Context context, String url) {
        super(context);
        mContext = context;
        mUrl = url;
    }

    @Override
    public RssFeedResponse loadInBackground() {

        mResp = new RssFeedResponse();

        RssFeedHandler handler = null;
        URL url = null;
        InputStream is = null;

        // Check if there is internet connectivity
        if (!ConnectivityChecker.hasInternetConnectivity(mContext)) {
            mResp.setException(new ConnectException(mContext.getResources().getString(R.string.error_connectivity_loading_rss_feed_from_bbc)));
            return mResp;
        }
        try {

            url = new URL(mUrl);

            handler = new RssFeedHandler();

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();

            XMLReader xmlReader = saxParser.getXMLReader();

            // Apply the handler to XmlReader
            xmlReader.setContentHandler(handler);

            is = url.openStream();

            xmlReader.parse(new InputSource(new InputStreamReader(is, Charset.forName(CHAR_SET_UTF_8))));

            mResp.setChannel(handler.getChannel());

        } catch (Exception e) {
            mResp.setException(new Exception(mContext.getResources().getString(R.string.error_generic_loading_rss_feed_from_bbc)));
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    mResp.setException(new Exception(mContext.getResources().getString(R.string.error_generic_loading_rss_feed_from_bbc)));
                }
            }
        }

        return mResp;
    }

    @Override
    protected void onStartLoading() {
        if (mResp != null) {

            deliverResult(mResp);
        }

        if (takeContentChanged() || mResp == null) {

            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // try to cancel load task if it is possible.
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        // stop the loader
        onStopLoading();

        // release resources
        if (mResp != null) {
            mResp = null;
        }
    }

}
