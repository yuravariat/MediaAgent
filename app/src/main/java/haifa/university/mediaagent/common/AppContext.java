package haifa.university.mediaagent.common;

/**
 * Created by yura on 19/10/2015.
 */
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.webkit.WebView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import haifa.university.info_beads_mediaagent.BrowseHistoryInfoBead;
import haifa.university.info_beads_mediaagent.FacebookInfoBead;
import haifa.university.info_beads_mediaagent.InfoCollectorInfoBead;
import haifa.university.info_beads_mediaagent.TwitterInfoBead;
import haifa.university.info_beads_mediaagent.YouTubeInfoBead;
import haifa.university.mediaagent.activities.MainActivity;
import haifa.university.twitter_provider.TwitterProvider;

public class AppContext extends Application {
    public int notificationCount;
    public WebView _WebView = null;
    /**
     * Keeps a reference of the application context
     */
    private static Context sContext;
    private static Activity mainActivity;

    /**
     * Info beads in pull mode.
     */
    private static BrowseHistoryInfoBead browseHistoryInfoBead;
    private static FacebookInfoBead facebookInfoBead;
    private static TwitterInfoBead twitterInfoBead;
    private static YouTubeInfoBead youTubeInfoBead;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        TwitterProvider.getInstance().Init();

        browseHistoryInfoBead = new BrowseHistoryInfoBead();
        facebookInfoBead = new FacebookInfoBead();
        twitterInfoBead = new TwitterInfoBead();
        youTubeInfoBead = new YouTubeInfoBead();
    }
    public static BrowseHistoryInfoBead getBrowseHistoryInfoBead(){
        return browseHistoryInfoBead;
    }
    public static FacebookInfoBead getFacebookInfoBead(){
        return facebookInfoBead;
    }
    public static TwitterInfoBead getTwitterInfoBead(){
        return twitterInfoBead;
    }
    public static YouTubeInfoBead getYouTubeInfoBead(){
        return youTubeInfoBead;
    }
    public void setMainActivity(MainActivity activity){
        mainActivity = activity;
    }
    public Activity getMainActivity(){
        return mainActivity;
    }
    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return sContext;
    }
}
