package haifa.university.youtube_provider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import haifa.university.mediaagent.activities.MainActivity;
import haifa.university.mediaagent.common.AppContext;
import haifa.university.mediaagent.common.AppLogger;
import haifa.university.mediaagent.common.GenericResponse;

/**
 * Created by yura on 22/02/2016.
 */
public class YouTubeProvider implements GoogleApiClient.OnConnectionFailedListener{
    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private YouTube _youtube;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient backgroundGoogleApiClient;
    private GoogleSignInAccount acct;
    private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private final JsonFactory JSON_FACTORY = new JacksonFactory();
    private final String TAG="YouTubeProvider";
    private IYouTubeProvider youTubePrividerInterface;
    private static final int RC_SIGN_IN = 9001;
    private FragmentActivity loginInActivity;

    private static YouTubeProvider ourInstance = new YouTubeProvider();
    public static YouTubeProvider getInstance() {
        return ourInstance;
    }
    private YouTubeProvider() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(YouTubeScopes.YOUTUBE), new Scope(YouTubeScopes.YOUTUBE_READONLY))
                        //.requestServerAuthCode(SERVER_CLIENT_ID, false)
                .build();
    }
    private GoogleAccountCredential getYouTubeApiCredentials(){
        // This OAuth 2.0 access scope allows for read-only access to the
        // authenticated user's account, but not other types of account access.
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.readonly");
        // Authorize the request.
        //Credential credential = Auth.authorize(scopes, "myuploads");
        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(AppContext.getContext(), scopes);
        credential.setSelectedAccountName(acct.getEmail());

        return  credential;
    }
    private YouTube getYouTubeApiClient() {
        if(_youtube==null) {
            GoogleAccountCredential credential = getYouTubeApiCredentials();
            if(credential!=null) {
                // This object is used to make YouTube Data API requests.
                _youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
                        "mediaagent").build();
            }
        }
        return _youtube;
    }
    public GoogleSignInOptions getGoogleSignInOptions(){
        return gso;
    }
    public void setGoogleApiClient(@NonNull Context context, @NonNull FragmentActivity loginInActivity,
                                              @NonNull IYouTubeProvider youTubePrividerInterface){
        if(backgroundGoogleApiClient!=null){
            backgroundGoogleApiClient.stopAutoManage(
                    (MainActivity)((AppContext) AppContext.getContext()).getMainActivity());
            backgroundGoogleApiClient = null;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(loginInActivity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        this.youTubePrividerInterface = youTubePrividerInterface;
        this.loginInActivity = loginInActivity;
    }
    public void stopAutoManageClient(){
        if(backgroundGoogleApiClient!=null){
            backgroundGoogleApiClient.stopAutoManage(
                    (MainActivity)((AppContext) AppContext.getContext()).getMainActivity());
            backgroundGoogleApiClient = null;
        }
        if(mGoogleApiClient!=null){
            mGoogleApiClient.stopAutoManage(loginInActivity);
            mGoogleApiClient = null;
        }
    }
    public GoogleApiClient getGoogleApiClient(){
        if(mGoogleApiClient==null){
            if(backgroundGoogleApiClient==null) {
                MainActivity activity = (MainActivity) ((AppContext) AppContext.getContext()).getMainActivity();
                backgroundGoogleApiClient = new GoogleApiClient.Builder(activity)
                        .enableAutoManage(activity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            }
            return backgroundGoogleApiClient;
        }
        return mGoogleApiClient;
    }
    public void makeSilentLogin(){
        GoogleApiClient cl = ourInstance.getGoogleApiClient();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(cl);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            if(youTubePrividerInterface!=null) {
                youTubePrividerInterface.inProcess(true);
            }
            final CountDownLatch waiter = new CountDownLatch(1);
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    if (youTubePrividerInterface != null) {
                        youTubePrividerInterface.inProcess(false);
                    }
                    handleSignInResult(googleSignInResult);
                    waiter.countDown();
                }
            });
            try {
                waiter.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            acct = result.getSignInAccount();
        }
        else{
            acct=null;
        }
        if(youTubePrividerInterface!=null) {
            youTubePrividerInterface.updateUI(result.isSuccess());
        }
    }
    public String getDisplayName(){
        return acct.getDisplayName();
    }
    public boolean isLoggedIn() {
        return acct != null;
    }
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(YouTubeProvider.getInstance().getGoogleApiClient());
        if(loginInActivity!=null) {
            loginInActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }
    public void signOut() {
        Auth.GoogleSignInApi.signOut(YouTubeProvider.getInstance().getGoogleApiClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if(youTubePrividerInterface!=null) {
                            youTubePrividerInterface.updateUI(false);
                            AppLogger.getInstance().writeLog(TAG, "YouTube logged out", AppLogger.LogLevel.TRACE);
                        }
                    }
                });
    }
    public void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(YouTubeProvider.getInstance().getGoogleApiClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if(youTubePrividerInterface!=null) {
                            youTubePrividerInterface.updateUI(false);
                        }
                    }
                });
    }
    public GenericResponse<List<haifa.university.youtube_provider.PlaylistItem>> getWatchHistoryPlayListItems(Date pullFromDate) {
        GenericResponse<List<haifa.university.youtube_provider.PlaylistItem>> response = new GenericResponse<>();
        // Define a list to store items in the list of uploaded videos.
        List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();
        try {
            YouTube youtube = getYouTubeApiClient();
            // Call the API's channels.list method to retrieve the
            // resource that represents the authenticated user's channel.
            // In the API response, only include channel information needed for
            // this use case. The channel's contentDetails part contains
            // playlist IDs relevant to the channel, including the ID for the
            // list that contains videos uploaded to the channel.
            YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");
            channelRequest.setMine(true);
            channelRequest.setFields("items/contentDetails,nextPageToken,pageInfo");
            ChannelListResponse channelResult = channelRequest.execute();

            List<Channel> channelsList = channelResult.getItems();

            if (channelsList != null) {
                // The user's default channel is the first item in the list.
                // Extract the playlist ID for the channel's videos from the
                // API response.
                String uploadPlaylistId =
                        channelsList.get(0).getContentDetails().getRelatedPlaylists().getWatchHistory();

                // Retrieve the playlist of the channel's uploaded videos.
                YouTube.PlaylistItems.List playlistItemRequest =
                        youtube.playlistItems().list("id,contentDetails,snippet");
                playlistItemRequest.setPlaylistId(uploadPlaylistId);

                // Only retrieve data used in this application, thereby making
                // the application more efficient. See:
                // https://developers.google.com/youtube/v3/getting-started#partial
                playlistItemRequest.setFields(
                        "items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");

                String nextToken = "";
                // Call the API one or more times to retrieve all items in the
                // list. As long as the API response returns a nextPageToken,
                // there are still more items to retrieve.
                while (nextToken != null){
                    playlistItemRequest.setPageToken(nextToken);
                    PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();

                    List<PlaylistItem> itemsReturned = playlistItemResult.getItems();
                    for (PlaylistItem item:itemsReturned) {
                        if(pullFromDate!=null && item.getSnippet()!=null && item.getSnippet().getPublishedAt()!=null
                                // Take in account times zones in google DateTime
                                && (item.getSnippet().getPublishedAt().getValue() + item.getSnippet().getPublishedAt().getTimeZoneShift() * (60*60*1000))
                                < pullFromDate.getTime()) {
                            nextToken = null;
                            break;
                        }
                        playlistItemList.add(item);
                    }
                    if((nextToken != null)) {
                        nextToken = playlistItemResult.getNextPageToken();
                    }
                }

                // Prints information about the results.
                //prettyPrint(playlistItemList.size(), playlistItemList.iterator());
            }

        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
            response.addError(e);

        } catch (Throwable t) {
            t.printStackTrace();
            response.addError(t.getMessage());
        }
        List<haifa.university.youtube_provider.PlaylistItem> playlistItemListRet = new ArrayList<>();
        for (PlaylistItem y_item:playlistItemList) {
            playlistItemListRet.add(haifa.university.youtube_provider.Converter.ConvertFromYouTubeObj(y_item));
        }
        response.data = playlistItemListRet;
        return response;
    }
    private static void prettyPrint(int size, Iterator<PlaylistItem> playlistEntries) {
        System.out.println("=============================================================");
        System.out.println("\t\tTotal History Videos: " + size);
        System.out.println("=============================================================\n");

        while (playlistEntries.hasNext()) {
            PlaylistItem playlistItem = playlistEntries.next();
            System.out.println(" video name  = " + playlistItem.getSnippet().getTitle());
            System.out.println(" video id    = " + playlistItem.getContentDetails().getVideoId());
            System.out.println(" upload date = " + playlistItem.getSnippet().getPublishedAt());
            System.out.println("\n-------------------------------------------------------------\n");
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            AppLogger.getInstance().writeLog(TAG, "YouTube logged in", AppLogger.LogLevel.TRACE);
            handleSignInResult(result);
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
