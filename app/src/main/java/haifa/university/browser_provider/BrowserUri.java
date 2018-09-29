package haifa.university.browser_provider;

/**
 * Created by yura on 19/10/2015.
 */
import android.net.Uri;

public enum  BrowserUri {
    Default (Uri.parse("content://browser/bookmarks")),
    Chrome (Uri.parse("content://com.android.chrome.browser/bookmarks")),
    Firefox (Uri.parse("content://org.mozilla.firefox.db.browser/bookmarks")),
    Opera (Uri.parse("content://com.opera.android.browser/bookmarks"));

    private final Uri uri;
    private BrowserUri(Uri u) {
        uri = u;
    }
    public Uri getUri(){
        return uri;
    }
}

