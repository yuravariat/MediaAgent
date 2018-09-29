package haifa.university.mediaagent.activities.MainFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import haifa.university.mediaagent.R;
import haifa.university.mediaagent.activities.MainActivity;
import haifa.university.mediaagent.activities.MainLayoutTabType;
import haifa.university.mediaagent.common.AppHelper;
import haifa.university.mediaagent.common.AppLogger;
import haifa.university.mediaagent.common.GenericResponse;
import haifa.university.youtube_provider.IYouTubeProvider;
import haifa.university.youtube_provider.PlaylistItem;
import haifa.university.youtube_provider.YouTubeProvider;

/**
 * Created by yura on 20/10/2015.
 */
public class YouTubeFragment extends Fragment implements IYouTubeProvider,View.OnClickListener {

    private static final String TAG = "YouTubeFragment";
    private static final int SHOW_ITEMS_LIMIT = 20;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private View rootView = null;
    private LinearLayout spinnerBackGround;
    private ListView youtubeWatchedList;
    private static List<PlaylistItem> youtubeWatchedListItems;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    //Steps for retriave watch history from google api v3:
    //1. from youtube.channels.list with part=contentDetails and mine=true I get a watchHistory playlist id
    //2. from youtube.playlistItems.list with part=snippet and the playlist id from 1) I get a list of video user watched

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static YouTubeFragment newInstance(int sectionNumber) {
        Log.i(TAG,"newInstance");
        YouTubeFragment fragment = new YouTubeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public YouTubeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainLayoutTabType tabType = MainLayoutTabType.values()[getArguments().getInt(ARG_SECTION_NUMBER)];
        Log.d(TAG,"On create");
        rootView = inflater.inflate(R.layout.fragment_youtube, container, false);

        // Views
        spinnerBackGround = (LinearLayout)rootView.findViewById(R.id.loading_spinner_layout);
        spinnerBackGround.setVisibility(View.GONE);
        mStatusTextView = (TextView)rootView.findViewById(R.id.status);
        youtubeWatchedList = (ListView)rootView.findViewById(R.id.youtube_watched_list);

        // Button listeners
        rootView.findViewById(R.id.sign_in_button).setOnClickListener(this);
        rootView.findViewById(R.id.sign_out_button).setOnClickListener(this);
        rootView.findViewById(R.id.disconnect_button).setOnClickListener(this);
        rootView.findViewById(R.id.get_youtube_watched_button).setOnClickListener(this);

        YouTubeProvider.getInstance().setGoogleApiClient((MainActivity)getActivity(),getActivity(),this);

        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton signInButton = (SignInButton)rootView.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(YouTubeProvider.getInstance().getGoogleSignInOptions().getScopeArray());

        FillVideosList();

        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onStart() {
        super.onStart();
        YouTubeProvider.getInstance().makeSilentLogin();
    }
    @Override
    public void onStop() {
        super.onStop();
        YouTubeProvider.getInstance().stopAutoManageClient();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        YouTubeProvider.getInstance().onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void inProcess(boolean started){
        if(started){
            showProgressDialog();
        }
        else {
            hideProgressDialog();
        }
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    @Override
    public void updateUI(boolean signedIn) {
        if (signedIn) {
            mStatusTextView.setText(YouTubeProvider.getInstance().getDisplayName());
            rootView.findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            rootView.findViewById(R.id.logged_in_layout).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.youtube_history_layout).setVisibility(View.VISIBLE);

            if(youtubeWatchedListItems!=null) {
                PlaylistItemsArrayAdapter adap = new PlaylistItemsArrayAdapter(getContext(),
                        R.layout.bookmark_list_item, youtubeWatchedListItems);
                youtubeWatchedList.setAdapter(adap);
                AppHelper.setListViewHeightBasedOnChildren(youtubeWatchedList);
            }

        } else {
            mStatusTextView.setText(R.string.signed_out);
            rootView.findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.logged_in_layout).setVisibility(View.GONE);
            rootView.findViewById(R.id.youtube_history_layout).setVisibility(View.GONE);
            youtubeWatchedListItems = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                YouTubeProvider.getInstance().signIn();
                break;
            case R.id.sign_out_button:
                YouTubeProvider.getInstance().signOut();
                break;
            case R.id.disconnect_button:
                YouTubeProvider.getInstance().revokeAccess();
                break;
            case R.id.get_youtube_watched_button:
                GetWatchedHistory();
                break;
        }
    }
    public void GetWatchedHistory() {
        try {
            spinnerBackGround.setVisibility(View.VISIBLE);
            (new RetrieveTokenTask()).execute();
        } catch (Exception ex) {
            spinnerBackGround.setVisibility(View.GONE);
            Log.e(TAG, ex.getMessage());
        }
    }
    private void FillVideosList(){
        if(youtubeWatchedListItems!=null && youtubeWatchedListItems.size()>0) {
            PlaylistItemsArrayAdapter adap = new PlaylistItemsArrayAdapter(getContext(), R.layout.bookmark_list_item, youtubeWatchedListItems);
            youtubeWatchedList.setAdapter(adap);
            AppHelper.setListViewHeightBasedOnChildren(youtubeWatchedList);
        }
        spinnerBackGround.setVisibility(View.GONE);
    }
    private class RetrieveTokenTask extends AsyncTask<String, Void, List<PlaylistItem>> {
        @Override
        protected List<PlaylistItem> doInBackground(String... params) {
            GenericResponse<List<PlaylistItem>> itemsRes = YouTubeProvider.getInstance().getWatchHistoryPlayListItems(null);
            if(!itemsRes.isOK()){
                AppLogger.getInstance().writeLog(TAG, "RetrieveTokenTask getWatchHistoryPlayListItems errors = " + itemsRes.getErrorsJSON(),AppLogger.LogLevel.ERROR);
            }
            return (itemsRes.isOK() && itemsRes.hasData()) ? itemsRes.data : new ArrayList<PlaylistItem>();
        }
        @Override
        protected void onPostExecute(List<PlaylistItem> items) {
            Toast.makeText(getContext(), items.size() + " videos", Toast.LENGTH_SHORT).show();
            Log.d("YouTubeFragment", items.size() + " videos");

            youtubeWatchedListItems = items.size() > SHOW_ITEMS_LIMIT ? items.subList(0,SHOW_ITEMS_LIMIT) : items;
            FillVideosList();
        }
        @Override
        protected void onPreExecute() {
            //spinnerBackGround.setVisibility(View.VISIBLE);
        }
    }
    public class PlaylistItemsArrayAdapter extends ArrayAdapter<PlaylistItem> {
        public PlaylistItemsArrayAdapter(Context context, @LayoutRes int resource, @NonNull List<PlaylistItem> objects) {
            super(context, resource, objects);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.bookmark_list_item, parent, false);
            }
            TextView title = (TextView) v.findViewById(R.id.bookmark_title);
            TextView url = (TextView) v.findViewById(R.id.bookmark_url);

            PlaylistItem item = getItem(position);
            title.setText(item.snippet.title != null ? item.snippet.title + " " + item.snippet.publishedAt.toString() : "");
            url.setText(item.snippet.description != null ? item.snippet.description : "");

            return v;
        }
    }
}
