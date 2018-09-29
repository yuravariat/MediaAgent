package haifa.university.facebook_provider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import haifa.university.mediaagent.common.AppContext;
import haifa.university.mediaagent.common.GenericResponse;

/**
 * Created by yura on 25/10/2015.
 */
public class FacebookProvider {
    private static FacebookProvider ourInstance;
    private AccessTokenTracker facebookAccessTokenTracker;
    private List<IAccessTokenTracker> accessTokenListeneres = new ArrayList<IAccessTokenTracker>();

    public static FacebookProvider getInstance() {
        if(ourInstance==null){
            ourInstance = new FacebookProvider();
            FacebookSdk.sdkInitialize(AppContext.getContext());
            ourInstance.facebookAccessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(
                        AccessToken oldAccessToken,
                        AccessToken currentAccessToken) {
                    // Set the access token using
                    // currentAccessToken when it's loaded or set.
                    for (IAccessTokenTracker listener: ourInstance.accessTokenListeneres) {
                        if(listener!=null) {
                            listener.onCurrentAccessTokenChanged(oldAccessToken, currentAccessToken);
                        }
                    }
                }
            };
        }
        return ourInstance;
    }
    private FacebookProvider() {
    }
    public boolean initSDK(){
        return true;
    }
    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken()!=null;
    }
    public Profile getCurrentProfile() {
        return Profile.getCurrentProfile();
    }
    public CallbackManager createCallbackManager(){
        return CallbackManager.Factory.create();
    }
    public void regiterAccessTokenTracker(IAccessTokenTracker listener){
        accessTokenListeneres.add(listener);
    }
    public boolean unregiterAccessTokenTracker(IAccessTokenTracker listener){
        if (accessTokenListeneres.contains(listener)) {
            return accessTokenListeneres.remove(listener);
        }
        return false;
    }
    public GenericResponse<ArrayList<FBLike>> getLikes(){
        return getLikes(null);
    }
    public GenericResponse<ArrayList<FBLike>> getLikes(Date fromDate){
        GenericResponse<ArrayList<FBLike>> response = new GenericResponse<>();

        final ArrayList<FBLike> allLikes = new ArrayList<>();
        final Date createdTimeLimit = fromDate;
        if(isLoggedIn()) {
            GraphRequest.Callback callback = new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    /* handle the result */
                    ArrayList<FBLike> likes = FacebookProvider.getInstance().deserializeLikesArray(response);
                    boolean nextPageNeed = true;
                    if (likes != null) {
                        for (FBLike like:likes) {
                            if(createdTimeLimit!=null && like.created_time!=null &&
                                    like.created_time.before(createdTimeLimit)) {
                                nextPageNeed = false;
                                break;
                            }
                            allLikes.add(like);
                        }
                    }
                    if(nextPageNeed) {
                        //get next batch of results of exists
                        GraphRequest nextRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                        if (nextRequest != null) {
                            nextRequest.setCallback(this);
                            nextRequest.executeAndWait();
                        }
                    }
                }
            };
            /* make the API call */
            makeGraphRequest(
                    callback,
                    HttpMethod.GET,
                    "/" + Profile.getCurrentProfile().getId() + "/likes",
                    null,
                    false);

            response.data = allLikes;
        }
        else{
            response.addError(1, "Not logged in");
        }
        return response;
    }
    private void makeGraphRequest(GraphRequest.Callback callback,HttpMethod method,String action,Bundle parameters, boolean Async){
        if(Async) {
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    action,
                    parameters,
                    method,
                    callback
            ).executeAsync();
        }
        else{
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    action,
                    parameters,
                    method,
                    callback
            ).executeAndWait();
        }
    }
    public ArrayList<FBLike> deserializeLikesArray(GraphResponse response){
        JSONArray obj = null;
        try {
            obj = response.getJSONObject().getJSONArray("data");
        } catch (JSONException e) {
            Log.e("FacebookProvider", "deserializeLikesArray error",e);
            e.printStackTrace();
        }
        if(obj!=null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            ArrayList<FBLike> arr = gson.fromJson(obj.toString(), new TypeToken<ArrayList<FBLike>>(){}.getType());
            return arr;
        }
        return new ArrayList<FBLike>();
    }
    public Bitmap GetProfileImage(){
        Bitmap profPict = null;
        try {
            URL image_value = new URL("https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture");
            profPict = BitmapFactory.decodeStream(image_value.openConnection().getInputStream());
        }
        catch (Exception ex){
            Log.e("FacebookProvider","GetProfileImage error",ex);
        }
        return profPict;
    }
}
