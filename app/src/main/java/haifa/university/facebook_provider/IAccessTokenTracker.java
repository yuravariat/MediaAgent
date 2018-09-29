package haifa.university.facebook_provider;

import com.facebook.AccessToken;

/**
 * Created by yura on 02/03/2016.
 */
public interface IAccessTokenTracker {
    public void onCurrentAccessTokenChanged(AccessToken oldAccessToken,AccessToken currentAccessToken);
}
