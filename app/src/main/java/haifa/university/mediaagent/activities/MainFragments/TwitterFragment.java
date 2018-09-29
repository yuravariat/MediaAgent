package haifa.university.mediaagent.activities.MainFragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

import haifa.university.mediaagent.R;
import haifa.university.mediaagent.common.AppHelper;
import haifa.university.mediaagent.common.AppLogger;
import haifa.university.mediaagent.common.GenericResponse;
import haifa.university.twitter_provider.TweeterUser;
import haifa.university.twitter_provider.TwitterProvider;
import haifa.university.twitter_provider.TwitterTweet;

/**
 * Created by yura on 20/10/2015.
 */
public class TwitterFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = "TwitterFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int SHOW_ITEMS_LIMIT = 20;
    private TwitterLoginButton loginButton;
    private TweeterUser profile;
    private TextView profileText;
    private LinearLayout loggedInLayout;
    private LinearLayout twitterDetailsLayout;
    private LinearLayout spinnerBackGround;
    private ListView tweetsListView;
    private static ArrayList<TwitterTweet> tweetsList;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TwitterFragment newInstance(int sectionNumber) {
        TwitterFragment fragment = new TwitterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TwitterFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        final View rootView = inflater.inflate(R.layout.fragment_twitter, container, false);

        Log.d("TwitterFragment", "default On create");

        spinnerBackGround = (LinearLayout)rootView.findViewById(R.id.loading_spinner_layout);
        profileText = (TextView)rootView.findViewById(R.id.twitter_profile_text);
        tweetsListView = (ListView)rootView.findViewById(R.id.tweets_list);
        twitterDetailsLayout = (LinearLayout)rootView.findViewById(R.id.twitter_details_layout);
        loggedInLayout = (LinearLayout)rootView.findViewById(R.id.logged_in_layout);

        loginButton = (TwitterLoginButton)rootView.findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                //Toast.makeText(getContext(), "Twitter logged in", Toast.LENGTH_SHORT).show();
                AppLogger.getInstance().writeLog(TAG, "Twitter logged in", AppLogger.LogLevel.TRACE);
                OnLoginStatusChanged();
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(getContext(), "Twitter login failed", Toast.LENGTH_SHORT).show();
            }
        });

        ((Button)rootView.findViewById(R.id.sign_out_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterProvider.getInstance().logOut();
                AppLogger.getInstance().writeLog(TAG, "Twitter logged out", AppLogger.LogLevel.TRACE);
                OnLoginStatusChanged();
            }
        });

        ((Button) rootView.findViewById(R.id.get_tweets_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTweets();
            }
        });

        OnLoginStatusChanged();
        TryFillTweetsList();

        return rootView;
    }
    private void OnLoginStatusChanged(){
        if(TwitterProvider.getInstance().isLoggedIn()){
            twitterDetailsLayout.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            loggedInLayout.setVisibility(View.VISIBLE);
            if(profile==null) {
                Callback<TweeterUser> userCallback = new Callback<TweeterUser>() {
                    @Override
                    public void success(Result<TweeterUser> result) {
                        //Do something with result, which provides a Tweet inside of result.data
                        profile = result.data;
                        profileText.setText(profile.name + ", " + profile.idStr);
                    }
                    public void failure(TwitterException exception) {
                        //Do something on failure
                    }
                };
                TwitterProvider.getInstance().getProfile(userCallback);
            }
            else{
                profileText.setText(profile.name + ", " + profile.idStr);
            }
        }
        else{
            loginButton.setVisibility(View.VISIBLE);
            loggedInLayout.setVisibility(View.GONE);
            twitterDetailsLayout.setVisibility(View.GONE);
            profile = null;
        }
    }
    private void showTweets(){
        spinnerBackGround.setVisibility(View.VISIBLE);
        if(tweetsList!=null){
            TryFillTweetsList();
        }
        else{
            if(TwitterProvider.getInstance().isLoggedIn()){
                new BackgroundWorkGetTweets().execute();
            }
        }
    }
    private void TryFillTweetsList(){
        if(TwitterProvider.getInstance().isLoggedIn() && tweetsList!=null &&
                tweetsList.size()>0 && tweetsListView!=null) {
            TweetsArrayAdapter adap = new TweetsArrayAdapter(getContext(),R.layout.bookmark_list_item, tweetsList);
            tweetsListView.setAdapter(adap);
            AppHelper.setListViewHeightBasedOnChildren(tweetsListView);
        }
        spinnerBackGround.setVisibility(View.GONE);
    }
    private class BackgroundWorkGetTweets extends AsyncTask<Object,Integer,ArrayList<TwitterTweet>> {
        @Override
        protected ArrayList<TwitterTweet> doInBackground(Object ... params) {
            GenericResponse<ArrayList<TwitterTweet>> res = TwitterProvider.getInstance().getTweets(50, null);
            if(!res.isOK()){
                AppLogger.getInstance().writeLog(TAG, "BackgroundWorkGetTweets getTweets errors = " + res.getErrorsJSON(),AppLogger.LogLevel.ERROR);
            }
            return (res.isOK() && res.hasData()) ? res.data : new ArrayList<TwitterTweet>();
        }
        @Override
        protected void onPostExecute(ArrayList<TwitterTweet> results) {
            Toast.makeText(getContext(), results.size() + " tweets", Toast.LENGTH_SHORT).show();
            Log.d("TwitterFragment", results.size() + " tweets");
            tweetsList = results.size() > SHOW_ITEMS_LIMIT ?
                    new ArrayList<TwitterTweet>(results.subList(0, SHOW_ITEMS_LIMIT)) : results;
            TryFillTweetsList();
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
    public class TweetsArrayAdapter extends ArrayAdapter<TwitterTweet> {
        public TweetsArrayAdapter(Context context, @LayoutRes int resource, @NonNull ArrayList<TwitterTweet> objects) {
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

            TwitterTweet tweet = getItem(position);
            title.setText(tweet.text);
            url.setText(tweet.createdAt);

            return v;
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList("key", bookmarks);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
