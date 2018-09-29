package haifa.university.twitter_provider;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import haifa.university.mediaagent.R;
import haifa.university.mediaagent.common.AppContext;
import haifa.university.mediaagent.common.GenericResponse;
import io.fabric.sdk.android.Fabric;

/**
 * Created by yura on 09/11/2015.
 */
public class TwitterProvider {
    private static TwitterProvider ourInstance;

    public static TwitterProvider getInstance() {
        if(ourInstance==null){
            ourInstance = new TwitterProvider();
            ourInstance.AuthConfig();
        }
        return ourInstance;
    }

    private TwitterProvider() {
    }
    public void Init(){

    }
    public void AuthConfig(){
        //TwitterAuthConfig authConfig = new TwitterAuthConfig("consumerKey","consumerSecret");
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                AppContext.getContext().getString(R.string.twitter_consumerKey),
                AppContext.getContext().getString(R.string.twitter_consumerSecret));
        Fabric.with(AppContext.getContext(),
                new TwitterCore(authConfig),new Twitter(authConfig));

    }
    public void logOut(){
        Twitter.logOut();
    }
    public boolean isLoggedIn() {
        final TwitterSession session = Twitter.getSessionManager().getActiveSession();
        return session != null && session.getAuthToken() != null;
    }
    public String GetUseSession(){
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;
        return token + ":" + secret;
    }
    public void getProfile(final Callback<TweeterUser> callBack){
        Callback<User> tweeterCallBack = new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                //Do something with result, which provides a Tweet inside of result.data
                Result<TweeterUser> res =
                        new Result<TweeterUser>(
                                Converter.ConvertTweeterUserFromTwitterObj(result.data),
                                result.response
                        );
                callBack.success(res);
            }
            public void failure(TwitterException exception) {
                //Do something on failure
                callBack.failure(exception);
            }
        };
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        // Can also use Twitter directly: Twitter.getApiClient()
        AccountService service = twitterApiClient.getAccountService();
        service.verifyCredentials(false,false,tweeterCallBack);
    }
    public GenericResponse<ArrayList<TwitterTweet>> getTweets(int count,Long since_id){
        final GenericResponse<ArrayList<TwitterTweet>> response = new GenericResponse<>();

        final CountDownLatch waiter = new CountDownLatch(1);
        Callback<List<Tweet>>tweeterCallBack = new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                //Do something with result, which provides a Tweet inside of result.data
                response.data = new ArrayList<>();
                for (Tweet t_tweet:result.data) {
                    response.data.add(Converter.ConvertTwitterTweetFromTwitterObj(t_tweet));
                }
                waiter.countDown();
            }
            public void failure(TwitterException exception) {
                //Do something on failure
                response.addError(exception);
                waiter.countDown();
            }
        };
        //int count = 40;
        //Long since_id = null;
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        if(session!=null) {
            //@GET("/1.1/statuses/user_timeline.json")
            //void userTimeline(
            // @Query("user_id") Long var1,
            // @Query("screen_name") String var2,
            // @Query("count") Integer var3,
            // @Query("since_id") Long var4,
            // @Query("max_id") Long var5,
            // @Query("trim_user") Boolean var6,
            // @Query("exclude_replies") Boolean var7,
            // @Query("contributor_details") Boolean var8,
            // @Query("include_rts") Boolean var9,
            // Callback<List<Tweet>> var10);

            TwitterCore.getInstance().getApiClient().getStatusesService()
                    .userTimeline(session.getId(), null, count, since_id!=null && since_id<=0 ? null:since_id,
                            null, null, null, null, false, tweeterCallBack);
        }
        try {
            waiter.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            response.addError(e);
        }

        return response;
    }
}
