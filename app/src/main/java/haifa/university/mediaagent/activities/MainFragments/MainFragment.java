package haifa.university.mediaagent.activities.MainFragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import haifa.university.info_beads_mediaagent.InfoCollectorState;
import haifa.university.mediaagent.R;
import haifa.university.mediaagent.activities.MainActivity;
import haifa.university.mediaagent.activities.MainLayoutTabType;
import haifa.university.mediaagent.common.AppContext;
import haifa.university.mediaagent.common.AppLogger;
import haifa.university.mediaagent.common.AppSettingsManager;
import haifa.university.mediaagent.common.JsonFileSettings;
import haifa.university.mediaagent.service.AppService;
import haifa.university.mediaagent.service.OnAlarmReceiver;

/**
 * Created by yura on 20/10/2015.
 */
public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private static final SimpleDateFormat dateFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private Button startServiceButton;
    private TextView appStartedTextView;
    private TextView serviceStartedTextView;
    private TextView lastCollectionTextView;
    private TextView lastSendTextView;
    private TextView lastSendSuccessTextView;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(int sectionNumber) {
        Log.i(TAG,"newInstance");
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainLayoutTabType tabType = MainLayoutTabType.values()[getArguments().getInt(ARG_SECTION_NUMBER)];
        View rootView = null;
        switch (tabType) {
            case Dashboard:
                Log.d(TAG,"Dashboard On create");
                rootView = inflater.inflate(R.layout.fragment_main, container, false);

                startServiceButton = rootView.findViewById(R.id.start_service_button);
                appStartedTextView = rootView.findViewById(R.id.status_app_started);
                serviceStartedTextView = rootView.findViewById(R.id.status_service_started);
                lastCollectionTextView = rootView.findViewById(R.id.status_last_info_collection);
                lastSendTextView = rootView.findViewById(R.id.status_last_sent_attempt);
                lastSendSuccessTextView = rootView.findViewById(R.id.status_last_sent_success);

                rootView.findViewById(R.id.short_cut_button1).setOnClickListener(view->{
                    ((MainActivity)getActivity()).ChangePageNumber(1);
                });
                rootView.findViewById(R.id.short_cut_button2).setOnClickListener(view->{
                    ((MainActivity)getActivity()).ChangePageNumber(2);
                });
                rootView.findViewById(R.id.short_cut_button3).setOnClickListener(view->{
                    ((MainActivity)getActivity()).ChangePageNumber(3);
                });
                rootView.findViewById(R.id.short_cut_button4).setOnClickListener(view->{
                    ((MainActivity)getActivity()).ChangePageNumber(4);
                });
                rootView.findViewById(R.id.short_cut_button5).setOnClickListener(view->{
                    ((MainActivity)getActivity()).ChangePageNumber(5);
                });

                break;
            default:
                Log.d("MainFragment","default On create");
                rootView = inflater.inflate(R.layout.fragment_default, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER) + 1));
                textView.setText(tabType.toString());
        }
        return rootView;
    }
    @SuppressLint("SetTextI18n")
    public void updateDashboardDetails(){
        Log.i(TAG,"updateDashboardDetails");

        //startServiceButton.setVisibility(View.GONE);
        //if (!AppHelper.getInstance().isServiceRunning(AppService.class)) {
        //    Intent intent = new Intent(AppContext.getContext(), AppService.class);
        //    getActivity().startService(intent);
        //}

        startServiceButton.setOnClickListener(v -> {
            //Intent intent = new Intent(getActivity(), AppService.class);
            Button startServiceButton = (Button) v.findViewById(R.id.start_service_button);
            if(!AppSettingsManager.getInstance().isServiceRunning()){
                OnAlarmReceiver.scheduleIntervalAlarms(AppContext.getContext());
                startServiceButton.setText(getString(R.string.StopService));
                serviceStartedTextView.setText(
                        getString(R.string.status_service_started) + " started at " + dateFromat.format(new Date()));
            }
            else {
                OnAlarmReceiver.cancelIntervalAlarm(AppContext.getContext());
                startServiceButton.setText(getString(R.string.StartService));
                serviceStartedTextView.setText(
                        getString(R.string.status_service_started) + " not running");
            }
        });

        Date started = AppSettingsManager.getInstance().getAppStarted();
        Date service_started = AppSettingsManager.getInstance().getServiceStarted();
        String settings_file_path = Environment.getExternalStorageDirectory()
                + "/MediaAgent/InfoCollectorInfoBead.txt";
        InfoCollectorState state = JsonFileSettings.load(InfoCollectorState.class,settings_file_path);

        Date last_collection = state!=null ? state.lastCollection : null;
        Date last_send_attempt = state!=null ? state.lastSendAttempt : null;
        Date last_send_success = state!=null ? state.lastSendSuccess : null;

        appStartedTextView.setText(
                getString(R.string.status_app_started) + " " +
                        (started != null ? dateFromat.format(started) : " not running"));

        if(AppSettingsManager.getInstance().isServiceRunning()){
            startServiceButton.setText(getString(R.string.StopService));
            serviceStartedTextView.setText(
                    getString(R.string.status_service_started) + " started at " + dateFromat.format(service_started));
        }
        else{
            serviceStartedTextView.setText(
                    getString(R.string.status_service_started) + " not running");
        }

        //boolean isRunningService = OnAlarmReceiver.isAlarmSet(AppContext.getContext());
        //testTextTextView.setText("Test area: service is " + (isRunningService?"running":"not running"));

        if(last_collection!=null){
            lastCollectionTextView.setText(
                    getString(R.string.status_last_info_collection) + " " + dateFromat.format(last_collection));
        }
        if(last_send_attempt!=null){
            lastSendTextView.setText(
                    getString(R.string.status_last_sent_attempt) + " " + dateFromat.format(last_send_attempt));
        }
        if(last_send_success!=null){
            lastSendSuccessTextView.setText(
                    getString(R.string.status_last_sent_success) + " " + dateFromat.format(last_send_success));
        }

        //if(!YouTubeProvider.getInstance().isLoggedIn()){
        //    // try to make silent login
        //    YouTubeProvider.getInstance().makeSilentLogin();
        //}
        //((TextView)rootView.findViewById(R.id.status_app_started)).setText(getString(R.string.status_app_started) + " " +
        //        dateFromat.format(AppSettingsManager.getInstance().getAppStarted()));
        //((TextView)rootView.findViewById(R.id.status_facebook_login)).setText(getString(R.string.status_facebook_login) + " " +
        //        (FacebookProvider.getInstance().isLoggedIn()?"logged in":"not logged in"));
        //((TextView)rootView.findViewById(R.id.status_twitter_login)).setText(getString(R.string.status_twitter_login) + " " +
        //        (TwitterProvider.getInstance().isLoggedIn()?"logged in":"not logged in"));
        //((TextView)rootView.findViewById(R.id.status_google_login)).setText(getString(R.string.status_google_login) + " " +
        //        (YouTubeProvider.getInstance().isLoggedIn() ? "logged in" : "not logged in"));
    }
    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(OnAlarmReceiver.SERVICE_DONE_ACTION);
        getContext().registerReceiver(updateUIReciver,filter);
        updateDashboardDetails();
        super.onResume();
    }
    @Override
    public void onPause() {
        try {
            getContext().unregisterReceiver(updateUIReciver);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Receiver not registered")) {
                // Ignore this exception. This is exactly what is desired
                Log.w(TAG,"Tried to unregister the reciver when it's not registered");
            } else {
                // unexpected, re-throw
                throw e;
            }
        }
        super.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG,"onSaveInstanceState");
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList("key", bookmarks);
    }
    private BroadcastReceiver updateUIReciver = new MyReceiver(new Handler());
    private class MyReceiver extends BroadcastReceiver {
        private Handler handler; // Handler used to execute code on the UI thread

        public MyReceiver(Handler handler) {
            this.handler = handler;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"updateUIReciver onReceive");
            try {
                // Post the UI updating code to our Handler
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateDashboardDetails();
                    }
                });
            }
            catch(Exception e){
                e.printStackTrace();
                Log.i(TAG,"updateUIReciver onReceive error = " + e.getMessage());
                AppLogger.getInstance().writeLog(TAG, "error = " + e.getMessage(), e, AppLogger.LogLevel.ERROR);
            }
        }
    };
}
