package haifa.university.mediaagent.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Date;
import java.util.List;

import haifa.university.info_beads_mediaagent.InfoCollectorInfoBead;
import haifa.university.mediaagent.R;
import haifa.university.mediaagent.activities.MainActivity;
import haifa.university.mediaagent.common.AppContext;
import haifa.university.mediaagent.common.AppLogger;
import haifa.university.mediaagent.common.AppSettingsManager;

/**
 * Created by yura on 13/12/2015.
 */
public class OnAlarmReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "OnAlarmReceiver";
    private static final int DATA_COLLECTION_ALARM = 1234;
    private static final long minute = 60 * 1000;
    public static final long collectionInterval = 60 * minute;
    public static final String SERVICE_DONE_ACTION = "haifa.university.service_done";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "BroadcastReceiver has received alarm intent.");
        AppLogger.getInstance().writeLog(TAG,  "BroadcastReceiver has received alarm intent.",AppLogger.LogLevel.TRACE);

        // 1 //
        if (intent.getAction() == SERVICE_DONE_ACTION) {
            Intent service1 = new Intent(context, AppService.class);
            AppLogger.getInstance().writeLog(TAG, "startWakefulService",AppLogger.LogLevel.TRACE);
            startWakefulService(context, service1);

            // Vibrate
            //Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            //long[] pattern = {0, 500, 100, 200, 100, 200};
            //vibrator.vibrate(pattern,-1);
        }
        else {
            scheduleIntervalAlarms(context);
        }

        // 2 //
        //Intent service1 = new Intent(context, AlarmService.class);
        //context.startService(service1);
    }
    public static void scheduleIntervalAlarms(Context ctxt) {
        Log.i(TAG, "scheduleIntervalAlarms was set.");
        AppLogger.getInstance().writeLog(TAG, "scheduleIntervalAlarms for " + collectionInterval +
                " realtime="+SystemClock.elapsedRealtime(),
                AppLogger.LogLevel.TRACE);

        AlarmManager mgr = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(ctxt, OnAlarmReceiver.class);
        intent.setAction(OnAlarmReceiver.SERVICE_DONE_ACTION);//my custom string action name
        PendingIntent pi=PendingIntent.getBroadcast(ctxt,DATA_COLLECTION_ALARM, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        mgr.setRepeating(AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime(),(collectionInterval), pi);


        if(AppSettingsManager.getInstance().getServiceStarted()==null) {
            AppSettingsManager.getInstance().setServiceStarted(new Date());
        }
        AppSettingsManager.getInstance().setServiceRunning(true);
        AppSettingsManager.getInstance().save();
    }
    public static void cancelIntervalAlarm(Context ctxt){
        Log.i(TAG, "scheduleIntervalAlarms was canceled.");
        AppLogger.getInstance().writeLog(TAG, "Alarm was canceled",AppLogger.LogLevel.TRACE);

        AlarmManager mgr = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(ctxt, OnAlarmReceiver.class);
        intent.setAction(OnAlarmReceiver.SERVICE_DONE_ACTION);//my custom string action name
        PendingIntent pi = PendingIntent.getBroadcast(ctxt, DATA_COLLECTION_ALARM, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mgr.cancel(pi);
        pi.cancel();

        AppSettingsManager.getInstance().setServiceStarted(null);
        AppSettingsManager.getInstance().setServiceRunning(false);
        AppSettingsManager.getInstance().save();
    }
    public static boolean isAlarmSet(Context ctxt){
        Intent intent=new Intent(ctxt, OnAlarmReceiver.class);
        intent.setAction(OnAlarmReceiver.SERVICE_DONE_ACTION);//my custom string action name
        boolean isWorking = (PendingIntent.getBroadcast(ctxt , DATA_COLLECTION_ALARM, intent,
               PendingIntent.FLAG_NO_CREATE) != null);//just changed the flag
        return isWorking;
    }
    private static void showMessage(Context ctxt, List<String> results) {
        Log.i(TAG, "showMessage");
        PowerManager.WakeLock screenOn = ((PowerManager)ctxt.getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "example");
        screenOn.acquire();

        // Notification ID to allow for future updates
        final int MY_NOTIFICATION_ID = 1;


        // Notification Action Elements
        Intent mNotificationIntent;
        PendingIntent mContentIntent;

        mNotificationIntent = new Intent(ctxt, MainActivity.class);
        mContentIntent = PendingIntent.getActivity(ctxt, 0, mNotificationIntent,Intent.FLAG_ACTIVITY_NEW_TASK);

        // Define the Notification's expanded message and Intent:
        // Notification Sound and Vibration on Arrival
        Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] mVibratePattern = { 0, 200, 200, 300 };

        // Notification Text Elements
        String tickerText = "Info media agent";
        String contentTitle = "Notification";
        StringBuffer sb = new StringBuffer();
        for(String s:results){
            sb.append(s +"\n");
        }

        String contentText = sb.toString();

        Notification.Builder notificationBuilder = new Notification.Builder(ctxt);

        notificationBuilder.setTicker(tickerText);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        Resources res = ctxt.getResources();
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher));
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentTitle(contentTitle);
        notificationBuilder.setContentText(contentText);
        notificationBuilder.setContentIntent(mContentIntent).setSound(soundURI);
        notificationBuilder.setVibrate(mVibratePattern);

        // Pass the Notification to the NotificationManager:
        NotificationManager mNotificationManager = (NotificationManager)ctxt.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());
        }

        screenOn.release();
    }
}