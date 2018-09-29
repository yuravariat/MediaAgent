package haifa.university.mediaagent.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import haifa.university.mediaagent.common.AppContext;
import haifa.university.mediaagent.common.AppLogger;

/**
 * Created by yura on 10/12/2015.
 */
public class ServiceAutoStarter extends BroadcastReceiver {
    private static final String TAG = "ServiceAutoStarter";
    @Override
    public void onReceive(Context context, Intent intent) {
        AppLogger.getInstance().writeLog(TAG, "onReceive",AppLogger.LogLevel.TRACE);
        OnAlarmReceiver.scheduleIntervalAlarms(AppContext.getContext());
        //Intent service = new Intent(context, AppService.class);
        //context.startService(service);
    }
}
