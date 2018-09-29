package haifa.university.facebook_provider;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yura on 24/10/2015.
 */
public class HashKeyGenerator {
    public static String Generate(Context context, String packageName){
        String key = "";
        if(packageName==null){
            packageName = "haifa.university.mediaagent";
        }
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                //Log.d("KeyHash:", key);
                //System.out.print("KeyHash:"+ key);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return key;
    }
}
