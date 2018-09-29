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
public class SetTopBoxFragment extends Fragment {
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
    public static SetTopBoxFragment newInstance(int sectionNumber) {
        SetTopBoxFragment fragment = new SetTopBoxFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SetTopBoxFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = null;

        Log.d("MainFragment",MainLayoutTabType.SetTopBox.toString() + " On create");
        rootView = inflater.inflate(R.layout.fragment_settopbox, container, false);

        spinnerBackGround = (LinearLayout)rootView.findViewById(R.id.loading_layout_history);

        return rootView;
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList("key", bookmarks);
    }
}
