package haifa.university.mediaagent.service;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import haifa.university.info_beads_mediaagent.InfoCollectorInfoBead;
import haifa.university.mediaagent.common.AppLogger;
import haifa.university.mediaagent.common.AppSettingsManager;

/**
 * Created by yura on 08/12/2015.
 */
public class AppService extends Service { // implements IResultReceiver {
    private final String TAG="AppService";
    private InfoCollectorInfoBead collectorInfoBead;
    private AsyncTask<Void,Void,Void> asyncTask;
    public AppService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        System.out.println(TAG + " onCreate");
        AppLogger.getInstance().writeLog(TAG,"Started", AppLogger.LogLevel.TRACE);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Destroy");
        AppLogger.getInstance().writeLog(TAG, "Stoped", AppLogger.LogLevel.TRACE);
        //Toast.makeText(getApplicationContext(), TAG + " Destroy", Toast.LENGTH_SHORT).show();

        // Stop collector InfoBead
        if(collectorInfoBead!=null){
            collectorInfoBead.stop();
        }
        AppSettingsManager.getInstance().setServiceStarted(null);

        super.onDestroy();

    }
    @SuppressLint("StaticFieldLeak")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        AppLogger.getInstance().writeLog(TAG,"onStartCommand", AppLogger.LogLevel.TRACE);

        if(collectorInfoBead==null){
            collectorInfoBead = new InfoCollectorInfoBead();
        }
        asyncTask = new AsyncTask<Void,Void,Void>(){
            @Override
            public Void doInBackground(Void aVoid[]){
                collectorInfoBead.run();
                return null;
            }
        };
        asyncTask.execute();

        OnAlarmReceiver.completeWakefulIntent(intent);

        return Service.START_NOT_STICKY;
    }
}