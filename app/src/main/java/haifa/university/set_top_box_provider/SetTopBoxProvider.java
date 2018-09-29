package haifa.university.set_top_box_provider;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import haifa.university.browser_provider.Bookmark;
import haifa.university.browser_provider.BookmarkColumns;
import haifa.university.browser_provider.BrowserUri;
import haifa.university.mediaagent.common.AppHelper;
import haifa.university.mediaagent.common.GenericResponse;

/**
 * Created by yura on 19/10/2015.
 */
public class SetTopBoxProvider {
    private static SetTopBoxProvider ourInstance = new SetTopBoxProvider();

    public static SetTopBoxProvider getInstance() {
        return ourInstance;
    }

    private SetTopBoxProvider() {
    }
    public GenericResponse<List<SetTopBoxView>> generateViews(Context context,Date from){
        GenericResponse<List<SetTopBoxView>> response = new GenericResponse<>();
        List<SetTopBoxView> views = new ArrayList<>();

        response.data = views;
        return response;
    }
}
