package haifa.university.mediaagent.activities.MainFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import haifa.university.browser_provider.Bookmark;
import haifa.university.browser_provider.BrowserProvider;
import haifa.university.browser_provider.BrowserUri;
import haifa.university.mediaagent.R;
import haifa.university.mediaagent.activities.MainLayoutTabType;
import haifa.university.mediaagent.common.AppHelper;
import haifa.university.mediaagent.common.AppLogger;
import haifa.university.mediaagent.common.GenericResponse;

/**
 * Created by yura on 20/10/2015.
 */
public class HistoryFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = "HistoryFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int SHOW_ITEMS_LIMIT = 20;
    private static List<Bookmark> bookmarks;
    private LinearLayout spinnerBackGround;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HistoryFragment newInstance(int sectionNumber) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = null;

        Log.d("MainFragment",MainLayoutTabType.BrowserHistory.toString() + " On create");
        rootView = inflater.inflate(R.layout.fragment_browser_history, container, false);
        Button getHistoryButton = (Button) rootView.findViewById(R.id.get_hitory_button);
        getHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundWorkGetHistory().execute(getContext());
            }
        });

        spinnerBackGround = (LinearLayout)rootView.findViewById(R.id.loading_layout_history);

        if(bookmarks!=null && !bookmarks.isEmpty()){
            Log.d("MainFragment","FillHistoryList");
            FillHistoryList(bookmarks,getHistoryButton);
        }

        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList("key", bookmarks);
    }
    private void FillHistoryList(List<Bookmark> bookmarks,View v) {
        BookmarksArrayAdapter bookmarksAdapter = new BookmarksArrayAdapter(
                getContext(),
                R.layout.bookmark_list_item,
                bookmarks
        );
        ListView bookmarks_list = (ListView) v.getRootView().findViewById(R.id.browser_history_list);
        bookmarks_list.setAdapter(bookmarksAdapter);
        AppHelper.setListViewHeightBasedOnChildren(bookmarks_list);
        bookmarks_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bookmark bookmark = (Bookmark) parent.getItemAtPosition(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookmark.url));
                startActivity(browserIntent);
            }
        });
    }
    private class BackgroundWorkGetHistory extends AsyncTask<Object,Integer,List<Bookmark>>{
        @Override
        protected List<Bookmark> doInBackground(Object ... params) {
            Context context = (Context)params[0];
            List<Bookmark> bookmarks = new ArrayList<>();
            GenericResponse<List<Bookmark>> bookmarksDefault =
                    BrowserProvider.getInstance().getHistory(getContext(), BrowserUri.Default);
            boolean chromeExist = AppHelper.getInstance().isPackageExist("com.android.chrome");
            GenericResponse<List<Bookmark>> chromeBookmarks = null;
            if(chromeExist) {
                chromeBookmarks =
                        BrowserProvider.getInstance().getHistory(getContext(), BrowserUri.Chrome);
            }
            //List<Bookmark> operaBookmarks = BrowserProvider.getInstance().getBookmarks(getContext(), BrowserUri.Opera);
            //List<Bookmark> firefoxBookmarks = BrowserProvider.getInstance().getBookmarks(getContext(), BrowserUri.Firefox);
            if(bookmarksDefault.isOK() && bookmarksDefault.hasData()){
                bookmarks.addAll(bookmarksDefault.data);
            }
            else{
                AppLogger.getInstance().writeLog(TAG, "BackgroundWorkGetHistory getHistory[bookmarksDefault] errors = "
                        + bookmarksDefault.getErrorsJSON(),AppLogger.LogLevel.ERROR);
            }
            if(chromeExist && chromeBookmarks.isOK() && chromeBookmarks.hasData()){
                bookmarks.addAll(chromeBookmarks.data);
            }
            else if(chromeExist){
                AppLogger.getInstance().writeLog(TAG, "BackgroundWorkGetHistory getHistory[chromeBookmarks] errors = "
                        + chromeBookmarks.getErrorsJSON(), AppLogger.LogLevel.ERROR);
            }

            return bookmarks;
        }
        @Override
        protected void onPostExecute(List<Bookmark> results) {
            Toast.makeText(getContext(), "History " + results.size() + " items", Toast.LENGTH_SHORT).show();
            Log.d("MainFragment", "History " + results.size() + " items");
            bookmarks = results.size() > SHOW_ITEMS_LIMIT ? results.subList(0, SHOW_ITEMS_LIMIT) : results;
            FillHistoryList(bookmarks, HistoryFragment.this.getView());
            spinnerBackGround.setVisibility(View.GONE);
        }
        @Override
        protected void onPreExecute() {
            spinnerBackGround.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
    public class BookmarksArrayAdapter extends ArrayAdapter<Bookmark> {
        BookmarksArrayAdapter(Context context, @LayoutRes int resource, @NonNull List<Bookmark> objects) {
            super(context, resource, objects);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v==null){
                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.bookmark_list_item,parent,false);
            }
            TextView title = (TextView)v.findViewById(R.id.bookmark_title);
            TextView url = (TextView)v.findViewById(R.id.bookmark_url);

            Bookmark bookmark = getItem(position);
            title.setText(bookmark.title);
            url.setText(bookmark.url);

            return v;
        }
    }
}
