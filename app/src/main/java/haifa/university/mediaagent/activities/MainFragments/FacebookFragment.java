package haifa.university.mediaagent.activities.MainFragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import haifa.university.facebook_provider.FBLike;
import haifa.university.facebook_provider.FacebookProvider;
import haifa.university.facebook_provider.IAccessTokenTracker;
import haifa.university.mediaagent.R;
import haifa.university.mediaagent.activities.MainLayoutTabType;
import haifa.university.mediaagent.common.AppHelper;
import haifa.university.mediaagent.common.AppContext;
import haifa.university.mediaagent.common.AppLogger;
import haifa.university.mediaagent.common.GenericResponse;

/**
 * Created by yura on 20/10/2015.
 */
public class FacebookFragment extends Fragment implements IAccessTokenTracker {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = "FacebookFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int SHOW_ITEMS_LIMIT = 20;
    private CallbackManager facebookCallbackManager;
    private LinearLayout facebookDetailsLayout;
    private LinearLayout spinnerBackGround;
    private ProfilePictureView facebookProfilePic;
    private ListView facebookLikesListView;
    private static ArrayList<FBLike> facebookLikes;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FacebookFragment newInstance(int sectionNumber) {
        FacebookFragment fragment = new FacebookFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FacebookFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = null;

        Log.d("FacebookFragment", MainLayoutTabType.Facebook.toString() + " On create");

        if(facebookLikes==null){
            facebookLikes = new ArrayList<FBLike>();
        }

        // Initialize facebookCallbackManager
        facebookCallbackManager = FacebookProvider.getInstance().createCallbackManager();
        FacebookProvider.getInstance().regiterAccessTokenTracker(this);

        rootView = inflater.inflate(R.layout.fragment_facebook, container, false);

        facebookDetailsLayout = (LinearLayout)rootView.findViewById(R.id.facebook_details_layout);
        facebookDetailsLayout.setVisibility(View.GONE);

        Button getLikesButton = (Button) rootView.findViewById(R.id.get_facebook_likes_button);
        getLikesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFacebookLikes();
            }
        });

        LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("public_profile", "user_friends", "user_likes", "user_events");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // do nothing onCurrentAccessTokenChanged will handle it
            }

            @Override
            public void onCancel() {
                Toast.makeText(AppContext.getContext(), "Facebook login cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(AppContext.getContext(), "Facebook login error", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerBackGround = (LinearLayout)rootView.findViewById(R.id.loading_spinner_layout);
        facebookLikesListView = (ListView)rootView.findViewById(R.id.facebook_likes_list);
        facebookProfilePic = (ProfilePictureView)rootView.findViewById(R.id.facebook_profile_picture);

        ShowFacebookProfile(rootView, FacebookProvider.getInstance().getCurrentProfile());
        TryFillLikesList();

        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList("key", bookmarks);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(facebookCallbackManager!=null){
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        FacebookProvider.getInstance().unregiterAccessTokenTracker(this);
    }
    private void ShowFacebookProfile(View v,Profile profile) {
        if(v!=null && profile!=null){
            facebookDetailsLayout.setVisibility(View.VISIBLE);
            TextView profileText = (TextView)v.getRootView().findViewById(R.id.facebook_profile_text);
            profileText.setText(profile.getFirstName() + " " + profile.getLastName());
            profileText.setVisibility(View.VISIBLE);
            facebookProfilePic.setProfileId(profile.getId());
        }
        if(v!=null && profile==null){
            facebookDetailsLayout.setVisibility(View.GONE);
            TextView profileText = (TextView)v.getRootView().findViewById(R.id.facebook_profile_text);
            profileText.setText("");
            profileText.setVisibility(View.GONE);
        }
    }
    private void GetFacebookLikes(){
        new BackgroundWorkGetFacebookLikes().execute(getContext());
    }
    private void TryFillLikesList(){
        if(FacebookProvider.getInstance().isLoggedIn() && facebookLikes!=null &&
                facebookLikes.size()>0 && facebookLikesListView!=null) {
            LikesArrayAdapter adap = new LikesArrayAdapter(getContext(),R.layout.bookmark_list_item, facebookLikes);
            facebookLikesListView.setAdapter(adap);
            AppHelper.setListViewHeightBasedOnChildren(facebookLikesListView);
        }
        spinnerBackGround.setVisibility(View.GONE);
    }

    @Override
    public void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
        ShowFacebookProfile(getView(), FacebookProvider.getInstance().getCurrentProfile());
        if(FacebookProvider.getInstance().getCurrentProfile()!=null){
            AppLogger.getInstance().writeLog(TAG, "Facebook logged in", AppLogger.LogLevel.TRACE);
        }
        else{
            AppLogger.getInstance().writeLog(TAG, "Facebook logged out", AppLogger.LogLevel.TRACE);
        }
    }

    private class BackgroundWorkGetFacebookLikes extends AsyncTask<Object,Integer,ArrayList<FBLike>> {
        @Override
        protected ArrayList<FBLike> doInBackground(Object ... params) {
            GenericResponse<ArrayList<FBLike>> likes = FacebookProvider.getInstance().getLikes();
            if(!likes.isOK()){
                AppLogger.getInstance().writeLog(TAG, "BackgroundWorkGetFacebookLikes getLikes errors = " + likes.getErrorsJSON(),AppLogger.LogLevel.ERROR);
            }
            return (likes.isOK() && likes.hasData()) ? likes.data : new ArrayList<FBLike>();
        }
        @Override
        protected void onPostExecute(ArrayList<FBLike> results) {
            Toast.makeText(getContext(), "Liked " + results.size() + " items", Toast.LENGTH_SHORT).show();
            Log.d("FacebookFragment", "Liked " + results.size() + " items");
            facebookLikes = results.size() > SHOW_ITEMS_LIMIT ?
                    new ArrayList(results.subList(0,SHOW_ITEMS_LIMIT)) : results ;
            TryFillLikesList();
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
    public class LikesArrayAdapter extends ArrayAdapter<FBLike> {
        public LikesArrayAdapter(Context context, @LayoutRes int resource, @NonNull ArrayList<FBLike> objects) {
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

            FBLike like = getItem(position);
            title.setText(like.name);
            url.setText(like.link);

            return v;
        }
    }
}
