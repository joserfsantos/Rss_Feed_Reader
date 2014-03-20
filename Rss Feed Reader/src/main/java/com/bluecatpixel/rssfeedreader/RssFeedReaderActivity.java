package com.bluecatpixel.rssfeedreader;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bluecatpixel.rssfeedreader.control.GetRssFeedTask;
import com.bluecatpixel.rssfeedreader.control.ImageLoaderTask;
import com.bluecatpixel.rssfeedreader.model.Channel;
import com.bluecatpixel.rssfeedreader.model.Constants;
import com.bluecatpixel.rssfeedreader.model.Item;
import com.bluecatpixel.rssfeedreader.model.RssFeedResponse;

import java.util.List;

/**
 * @author josericardosantos (Blue Cat Pixel)
 *         <p/>
 *         Show a list with Rss Feed of BBC
 */
public class RssFeedReaderActivity extends Activity implements LoaderCallbacks<RssFeedResponse>,
        OnItemClickListener, DrawerLayout.DrawerListener {

    private ImageView ivChannel;
    private TextView tvChannelTitle;
    private Channel channel;
    private RssFeedItemAdapter adapter;
    private ImageLoaderTask imageLoader;
    private LayoutInflater mInflater;
    private ListView mItemsList;
    private ProgressDialog mPdLoading;
    private int mScreenWidth;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ListView mMenuList;
    private int mCurrentSelectedPosition = 0;

    private boolean isActionBarButtonsVisible = true;
    private String mBbcRssFeedUrl = Constants.BBC_NEWS_RSS_FEED_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rss_feed_reader_layout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(this);

        ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                mDrawerLayout.closeDrawer(mItemsList);
            }

            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.openDrawer(mItemsList);
            }
        };

        mMenuList = (ListView) findViewById(R.id.lv_menu_options);

        mMenuList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentSelectedPosition = position;
                if (mMenuList != null) {
                    mMenuList.setItemChecked(position, true);
                }
                if (mDrawerLayout != null) {
                    mDrawerLayout.closeDrawer(mMenuList);
                }
                restartList();
            }
        });
        mMenuList.setAdapter(new ArrayAdapter<String>(
                this,
                R.layout.menu_list_item,
                R.id.menu_list_item_text,
                new String[]{
                        getString(R.string.menu_option_news),
                        getString(R.string.menu_option_world),
                        getString(R.string.menu_option_sport),
                        getString(R.string.menu_option_uk),
                        getString(R.string.menu_option_england),
                        getString(R.string.menu_option_northern_ireland),
                        getString(R.string.menu_option_scotland),
                        getString(R.string.menu_option_wales),
                        getString(R.string.menu_option_business),
                        getString(R.string.menu_option_politics),
                        getString(R.string.menu_option_health),
                        getString(R.string.menu_option_education),
                        getString(R.string.menu_option_science_nature),
                        getString(R.string.menu_option_technology),
                        getString(R.string.menu_option_entertainment),
                        getString(R.string.menu_option_have_your_say),
                        getString(R.string.menu_option_magazine),
                        getString(R.string.menu_option_latest_published_stories)
                }
        ));
        mMenuList.setItemChecked(mCurrentSelectedPosition, true);

        ivChannel = (ImageView) findViewById(R.id.iv_channel_image);

        tvChannelTitle = (TextView) findViewById(R.id.tv_channel_title);

        mItemsList = (ListView) findViewById(R.id.lv_items);

        mPdLoading = new ProgressDialog(this);
        mPdLoading.setMessage(getString(R.string.loading_rss_feed_from_bbc));
        mPdLoading.setCanceledOnTouchOutside(false);

        adapter = new RssFeedItemAdapter(this);

        mItemsList.setAdapter(adapter);
        mItemsList.setOnItemClickListener(this);

        imageLoader = new ImageLoaderTask(this);

        this.mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // gets screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            size.set(display.getWidth(), display.getHeight());
        } else {
            getWindowManager().getDefaultDisplay().getSize(size);
        }
        mScreenWidth = size.x;

        restartList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.rss_feed_menu, menu);

        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(isActionBarButtonsVisible);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) {
            restartList();
        } else if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    @Override
    protected void onDestroy() {

        if (imageLoader != null) {
            imageLoader.clearAllResources();
        }

        super.onDestroy();
    }

    @Override
    public Loader<RssFeedResponse> onCreateLoader(int arg0, Bundle arg1) {

        mPdLoading.show();

        return new GetRssFeedTask(this, mBbcRssFeedUrl);
    }

    @Override
    public void onLoadFinished(Loader<RssFeedResponse> arg0,
                               RssFeedResponse resp) {

        mPdLoading.dismiss();

        if (resp == null || resp.getException() != null) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.error_title))
                    .setMessage(resp.getException().getMessage())
                    .setNeutralButton(getString(android.R.string.ok), null)
                    .show();

        } else {
            channel = resp.getChannel();
            if (channel != null) {
                adapter.setData(channel.getItems());
                mItemsList.smoothScrollToPosition(0);

                if (channel.getImage() != null) {
                    imageLoader.displayImage(channel.getImage().getUrl(), ivChannel);
                } else {
                    ivChannel.setImageBitmap(null);
                }
                tvChannelTitle.setText(channel.getTitle());

            } else {
                ivChannel.setImageBitmap(null);
                tvChannelTitle.setText("");
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<RssFeedResponse> arg0) {
        this.channel = null;

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        Intent i = new Intent(this, ShowWebPageActivity.class);
        i.putExtra(Constants.EXTRA_WEBPAGE_URL, channel.getItems().get(position).getLink());
        startActivity(i);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

        isActionBarButtonsVisible = false;
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerClosed(View drawerView) {

        isActionBarButtonsVisible = true;
        invalidateOptionsMenu();

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    private void restartList() {
        switch (mCurrentSelectedPosition) {
            case 0:
                mBbcRssFeedUrl = Constants.BBC_NEWS_RSS_FEED_URL;
                break;
            case 1:
                mBbcRssFeedUrl = Constants.BBC_WORLD_RSS_FEED_URL;
                break;
            case 2:
                mBbcRssFeedUrl = Constants.BBC_SPORT_RSS_FEED_URL;
                break;
            case 3:
                mBbcRssFeedUrl = Constants.BBC_UK_RSS_FEED_URL;
                break;
            case 4:
                mBbcRssFeedUrl = Constants.BBC_ENGLAND_RSS_FEED_URL;
                break;
            case 5:
                mBbcRssFeedUrl = Constants.BBC_NORTHERN_IRELAND_RSS_FEED_URL;
                break;
            case 6:
                mBbcRssFeedUrl = Constants.BBC_SCOTLAND_RSS_FEED_URL;
                break;
            case 7:
                mBbcRssFeedUrl = Constants.BBC_WALES_RSS_FEED_URL;
                break;
            case 8:
                mBbcRssFeedUrl = Constants.BBC_BUSINESS_RSS_FEED_URL;
                break;
            case 9:
                mBbcRssFeedUrl = Constants.BBC_POLITICS_RSS_FEED_URL;
                break;
            case 10:
                mBbcRssFeedUrl = Constants.BBC_HEALTH_RSS_FEED_URL;
                break;
            case 11:
                mBbcRssFeedUrl = Constants.BBC_EDUCATION_RSS_FEED_URL;
                break;
            case 12:
                mBbcRssFeedUrl = Constants.BBC_SCIENCE_AND_ENVIRONMENT_RSS_FEED_URL;
                break;
            case 13:
                mBbcRssFeedUrl = Constants.BBC_TECHNOLOGY_RSS_FEED_URL;
                break;
            case 14:
                mBbcRssFeedUrl = Constants.BBC_ENTERTAINMENT_AND_ARTS_RSS_FEED_URL;
                break;
            case 15:
                mBbcRssFeedUrl = Constants.BBC_HAVE_YOUR_SAY_RSS_FEED_URL;
                break;
            case 16:
                mBbcRssFeedUrl = Constants.BBC_MAGAZINE_RSS_FEED_URL;
                break;
            case 17:
                mBbcRssFeedUrl = Constants.BBC_LATEST_PUBLISHED_RSS_FEED_URL;
                break;
            default:
                mBbcRssFeedUrl = Constants.BBC_NEWS_RSS_FEED_URL;
                break;
        }
        getLoaderManager().restartLoader(0, null, this);
    }

    private class RssFeedItemAdapter extends ArrayAdapter<Item> {

        public RssFeedItemAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
        }

        public void setData(List<Item> data) {
            clear();
            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    add(data.get(i));
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.list_item,
                        parent, false);
            } else {
                view = convertView;
            }

            ImageView ivThumbnail = (ImageView) view
                    .findViewById(R.id.iv_item_thumbnail);
            TextView tvTitle = (TextView) view
                    .findViewById(R.id.tv_item_title);
            TextView tvPubDate = (TextView) view
                    .findViewById(R.id.tv_item_pubdate);
            TextView tvDescription = (TextView) view
                    .findViewById(R.id.tv_item_description);

            if (getItem(position).getThumbnails() != null && getItem(position).getThumbnails().size() > 0) {
                // if the second thumbnail wifth is larger than 25% of width's screen then show the first one
                if (getItem(position).getThumbnails().size() > 1 && getItem(position).getThumbnails().get(1).getWidth() > (mScreenWidth / 4)) {
                    imageLoader.displayImage(getItem(position).getThumbnails().get(0).getUrl(), ivThumbnail);
                } else {
                    imageLoader.displayImage(getItem(position).getThumbnails().get(1).getUrl(), ivThumbnail);
                }
            }

            tvTitle.setText(getItem(position).getTitle());
            tvPubDate.setText(getItem(position).getPubDate());
            tvDescription.setText(getItem(position).getDescription());

            return view;
        }

    }
}
