package haifa.university.mediaagent.common;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import haifa.university.mediaagent.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yura on 19/10/2015.
 */
public class AppHelper {
    private static AppHelper ourInstance = new AppHelper();

    public static AppHelper getInstance() {
        return ourInstance;
    }

    public Typeface awsomeFont;

    private AppHelper() {
        awsomeFont = Typeface.createFromAsset(AppContext.getContext().getAssets(), "fonts/fontawesome-webfont.ttf");
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) AppContext.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public String sha1Hash(String toHash) {
        String hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            // This is ~55x faster than looping and String.formating()
            hash = bytesToHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hash;
    }

    // http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

    public String getDeviceIdentificationID() {
        TelephonyManager tm = (TelephonyManager) AppContext.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = "";
        if (ActivityCompat.checkSelfPermission(AppContext.getContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AppContext.getContext(), Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AppContext.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            deviceId = tm.getLine1Number();
        }
        if (deviceId != null && deviceId.length() > 2) {
            deviceId = deviceId.substring(2);
            return deviceId;
        }

        // or generate device id
        String tmDevice, androidId;
        tmDevice = "";
        if (ActivityCompat.checkSelfPermission(AppContext.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            tmDevice = "" + tm.getDeviceId();
        }
        //tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(AppContext.getContext().getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        //UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());

        //deviceId = deviceUuid.toString();
        deviceId = (tmDevice!=null && !tmDevice.equals("null") ? tmDevice : androidId);

        return deviceId;
    }
    public String getDeviceId(){

        if (ActivityCompat.checkSelfPermission(AppContext.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) AppContext.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        }
        return "no-permissions";
    }
    public void exportAppFilesToPubblicDir(){
        AppSettingsManager.getInstance().save();
        File appPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/MediaAgent");
        File sdcardPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/infomedia");
        try {
            copyDirectoryOneLocationToAnotherLocation(appPath,sdcardPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void copyDirectoryOneLocationToAnotherLocation(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
                //targetLocation.setReadable(true, false);
                //targetLocation.setExecutable(true, false);
                //targetLocation.setReadable(true,false);
                //try {
                //    chmod(targetLocation,0777);
                //} catch (Exception e) {
                //    e.printStackTrace();
                //}
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {

                copyDirectoryOneLocationToAnotherLocation(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            //targetLocation.setReadable(true,false);
            //targetLocation.setExecutable(true,false);
            //targetLocation.setReadable(true,false);
            //try {
            //    chmod(targetLocation,0777);
            //} catch (Exception e) {
            //    e.printStackTrace();
            //}
        }
    }
    public int chmod(File path, int mode) throws Exception {
        Class fileUtils = Class.forName("android.os.FileUtils");
        Method setPermissions = fileUtils.getMethod("setPermissions", String.class, int.class, int.class, int.class);
        return (Integer) setPermissions.invoke(null, path.getAbsolutePath(), mode, -1, -1);
    }
    public boolean isPackageExist(String targetPackage){
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = AppContext.getContext().getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage)) return true;
        }
        return false;
    }
}
