package haifa.university.youtube_provider;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;

/**
 * Created by yura on 22/02/2016.
 */
public interface IYouTubeProvider {
    public void updateUI(boolean signedIn);
    public void inProcess(boolean started);
}
