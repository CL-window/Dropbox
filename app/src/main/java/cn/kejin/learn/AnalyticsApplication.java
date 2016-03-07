package cn.kejin.learn;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Author: xoracle ( Liang Ke Jin )
 * Date: 2016/3/6
 */
public class AnalyticsApplication extends Application
{
    private static Tracker mTracker;

    @Override
    public void onCreate()
    {
        super.onCreate();

        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
    }

    synchronized static public Tracker getDefaultTracker()
    {
        return mTracker;
    }
}