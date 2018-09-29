package haifa.university.browser_provider;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import haifa.university.mediaagent.common.AppContext;
import haifa.university.mediaagent.common.AppHelper;
import haifa.university.mediaagent.common.GenericResponse;

/**
 * Created by yura on 19/10/2015.
 */
public class BrowserProvider {
    private static BrowserProvider ourInstance = new BrowserProvider();

    public static BrowserProvider getInstance() {
        return ourInstance;
    }

    private BrowserProvider() {
    }

    public GenericResponse<List<Bookmark>> getBookmarks(Context context,BrowserUri browserUri){
        return getBookmarks(context,browserUri,true,null);
    }
    public GenericResponse<List<Bookmark>> getHistory(Context context,BrowserUri browserUri){
        return getBookmarks(context,browserUri,false,null);
    }
    public GenericResponse<List<Bookmark>> getBookmarks(Context context,BrowserUri browserUri,Date from){
        return getBookmarks(context,browserUri,true,from);
    }
    public GenericResponse<List<Bookmark>> getHistory(Context context,BrowserUri browserUri,Date from){
        return getBookmarks(context,browserUri,false,from);
    }
    public GenericResponse<List<Bookmark>> getHistoryFromAllBrowsers(Context context,Date from){
        GenericResponse<List<Bookmark>> response = new GenericResponse<>();
        List<Bookmark> bookmarks = new ArrayList<Bookmark>();
        List<BrowserUri> browsers = new ArrayList<BrowserUri>();
        browsers.add(BrowserUri.Default);
        if(AppHelper.getInstance().isPackageExist("com.android.chrome")){
            browsers.add(BrowserUri.Chrome);
        }
        try {
            for (BrowserUri br : browsers) {
                GenericResponse<List<Bookmark>> res = getHistory(context, br, from);
                if(res!=null && res.isOK()) {
                    bookmarks.addAll(res.data);
                }
            }
        }
        catch (Exception ex){
            response.addError(ex);
        }
        response.data = bookmarks;
        return response;
    }
    private GenericResponse<List<Bookmark>> getBookmarks(Context context,BrowserUri browserUri,boolean isBookmarks,Date from){
        GenericResponse<List<Bookmark>> response = new GenericResponse<>();
        List<Bookmark> items = new ArrayList(100);

        String[] proj = new String[] { BookmarkColumns.TITLE, BookmarkColumns.URL, BookmarkColumns.CREATED, BookmarkColumns.DATE, BookmarkColumns.VISITS };
        String sel = BookmarkColumns.BOOKMARK + " = " + ( isBookmarks ? "1" : "0" ); // 0 = history, 1 = bookmark

        Cursor mCur = null;

        try {
            mCur = context.getContentResolver().query(browserUri.getUri(), proj, sel, null, null);
        }
        catch (Exception ex){
            Log.e("BrowserProvider","getBookmarks getContentResolver, " + ex.toString());
            response.addError(ex);
        }

        if(mCur!=null) {

            mCur.moveToFirst();
            int titleIdx = mCur.getColumnIndex(BookmarkColumns.TITLE);
            int urlIdx = mCur.getColumnIndex(BookmarkColumns.URL);
            int crIdx = mCur.getColumnIndex(BookmarkColumns.CREATED);
            int dlIdx = mCur.getColumnIndex(BookmarkColumns.DATE);
            int vIdx = mCur.getColumnIndex(BookmarkColumns.VISITS);

            while (mCur.isAfterLast() == false) {

                try {
                    Date created = new Date(mCur.getLong(dlIdx));
                    if(from==null || (from!=null && created.compareTo(from)>=0)) {
                        Bookmark item = new Bookmark();
                        item.title = mCur.getString(titleIdx);
                        item.url = mCur.getString(urlIdx);
                        item.created = created;
                        item.lastVisited = new Date(mCur.getLong(crIdx));
                        item.visits = mCur.getInt(vIdx);
                        item.isBookmark = isBookmarks;
                        item.sourceApp = browserUri.toString();
                        items.add(item);
                    }
                }
                catch (Exception ex){
                    Log.e("BrowserProvider","getBookmarks, " + ex.toString());
                }
                mCur.moveToNext();
            }
            response.data = items;
        }
        return response;
    }
}
