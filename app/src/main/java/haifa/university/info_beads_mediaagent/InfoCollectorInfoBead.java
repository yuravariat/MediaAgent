package haifa.university.info_beads_mediaagent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import haifa.university.facebook_provider.FBLike;
import haifa.university.facebook_provider.FacebookProvider;
import haifa.university.info_beads_general.InfoBead;
import haifa.university.info_beads_general.InfoBeadControl;
import haifa.university.info_beads_general.InfoItem;
import haifa.university.info_beads_general.MetadataPart;
import haifa.university.info_beads_general.Triplet;
import haifa.university.mediaagent.common.AppContext;
import haifa.university.mediaagent.common.AppHelper;
import haifa.university.mediaagent.common.AppLogger;
import haifa.university.mediaagent.common.AppSettingsManager;
import haifa.university.mediaagent.common.GenericResponse;
import haifa.university.mediaagent.common.JsonFileSettings;
import haifa.university.mediaagent.service.OnAlarmReceiver;
import haifa.university.twitter_provider.TwitterProvider;
import haifa.university.twitter_provider.TwitterTweet;
import haifa.university.youtube_provider.PlaylistItem;
import haifa.university.youtube_provider.YouTubeProvider;

/**
 * InfoCollector InfoBead
 * @author Yuri Variat
 * @version  1.0;
 *
 */
public class InfoCollectorInfoBead extends InfoBead<ArrayList<Triplet>> implements Runnable {
	private static final String TAG="InfoCollectorInfoBead";
	private static final long serialVersionUID = 1L;
    private Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

    private InfoCollectorState infoCollectorState;
    private final String SETTINGS_FILE_PATH = Environment.getExternalStorageDirectory()
            + "/MediaAgent/InfoCollectorInfoBead.txt";

    private Thread infoCollectorThread;
    private final int httpReadTimeout = 30 * 1000;
    private final int httpConnectionTimeout = 7 * 1000;
    private volatile boolean running = false;
    private volatile boolean started = false;
    private final String apiUrl = "http://ec2-52-40-148-154.us-west-2.compute.amazonaws.com";

    private boolean collectHistory = true;
    private boolean collectFacebook = true;
    private boolean collectTwitter = true;
    private boolean collectYoutube = true;

    public InfoCollectorInfoBead(){
        super();
        initialize();
    }
	public void initialize() {
		// -------------- set id ------------------------
		this.setInfoBeadId("InfoCollectorInfoBead");
        this.setInfoBeadVersionId(String.valueOf(serialVersionUID));

		// -------------- set control part ------------------------
		InfoBeadControl control = new InfoBeadControl();
		control.setComMode(InfoBeadControl.ConnectionType.PUSH);
		setControlPart(control);

		// -------------- set metadataPart ------------------------
		// setting up of the main components of the info-bead
		MetadataPart metadata = new MetadataPart();

		metadata.setInfoBeadName("InfoCollectorInfobead");
        metadata.setDescription("Info-bead that returns aggregation of other info-beads (works in push method)");
		metadata.setVersion("v1.0");
		metadata.getBackwardCompatibility().add("v1.0");
		// Enter the default metadata into the info-bead
		this.setMetadata(metadata);

        infoCollectorState = JsonFileSettings.load(InfoCollectorState.class,SETTINGS_FILE_PATH);
        if(infoCollectorState==null){
            infoCollectorState = new InfoCollectorState();
        }
		
		//infoCollectorThread = new Thread(this,"InfoCollector");
	}
	public void handleData(Triplet<ArrayList<Triplet>> inTriplet) { /* no need*/	}
    private GenericResponse<ArrayList<Triplet>> collectData(){
		AppLogger.getInstance().writeLog(TAG, "Data collection start", AppLogger.LogLevel.TRACE);

		// Collect info from info-beads
        GenericResponse<ArrayList<Triplet>> response = new GenericResponse<ArrayList<Triplet>>();
        response.data = new ArrayList<Triplet>();
        String infName = "";

		// History
        if(collectHistory) {
            infName = "History info-bead";
            try {
                //AppLogger.getInstance().writeLog(TAG, infName + " pull start", AppLogger.LogLevel.TRACE);
                Triplet<List<BrowseHistoryInfoBead.HistoryBookmark>> history =
                        AppContext.getBrowseHistoryInfoBead().pullData(infoCollectorState.lastTimeCollectedHistory);

                if (history != null && history.getInfoItem() != null && history.getInfoItem().getInfoValue().size() > 0) {
                    AppLogger.getInstance().writeLog(TAG, infName + " pull "
                            + history.getInfoItem().getInfoValue().size() + " items", AppLogger.LogLevel.TRACE);
                    infoCollectorState.newLastTimeCollectedHistory = new Date();
                    response.data.add(history);
                } else {
                    AppLogger.getInstance().writeLog(TAG, infName + " pull no items", AppLogger.LogLevel.TRACE);
                }

                //AppLogger.getInstance().writeLog(TAG, infName + " pull end", AppLogger.LogLevel.TRACE);
            } catch (Exception ex) {
                AppLogger.getInstance().writeLog(TAG, infName + " pull", ex, AppLogger.LogLevel.ERROR);
                AppLogger.getInstance().writeLog(TAG, infName + " no items because of error", AppLogger.LogLevel.TRACE);
            }
        }

		// Facebook
        if(collectFacebook) {
            infName = "Facebook info-bead";
            if(!FacebookProvider.getInstance().isLoggedIn()){
                AppLogger.getInstance().writeLog(TAG, infName + " not logged in", AppLogger.LogLevel.TRACE);
            }
            else {
                try {
                    //AppLogger.getInstance().writeLog(TAG, infName + " pull start", AppLogger.LogLevel.TRACE);
                    Triplet<List<FBLike>> facebook =
                            AppContext.getFacebookInfoBead().pullData(infoCollectorState.lastTimeCollectedFacebook);
                    if (facebook != null && facebook.getInfoItem() != null && facebook.getInfoItem().getInfoValue().size() > 0) {
                        AppLogger.getInstance().writeLog(TAG, infName + " pull "
                                + facebook.getInfoItem().getInfoValue().size() + " items", AppLogger.LogLevel.TRACE);
                        infoCollectorState.newLastTimeCollectedFacebook = new Date();
                        response.data.add(facebook);
                    } else {
                        AppLogger.getInstance().writeLog(TAG, infName + " pull no items", AppLogger.LogLevel.TRACE);
                    }
                    //AppLogger.getInstance().writeLog(TAG, infName + " pull end", AppLogger.LogLevel.TRACE);
                } catch (Exception ex) {
                    AppLogger.getInstance().writeLog(TAG, infName + " pull", ex, AppLogger.LogLevel.ERROR);
                    AppLogger.getInstance().writeLog(TAG, infName + " no items because of error", AppLogger.LogLevel.TRACE);
                }
            }
        }

		// Twitter
        if(collectTwitter) {
            infName = "Twitter info-bead";
            if(!TwitterProvider.getInstance().isLoggedIn()){
                AppLogger.getInstance().writeLog(TAG, infName + " not logged in", AppLogger.LogLevel.TRACE);
            }
            else {
                try {
                    //AppLogger.getInstance().writeLog(TAG, "Twitter info-bead pull start", AppLogger.LogLevel.TRACE);
                    Triplet<List<TwitterTweet>> twitter =
                            AppContext.getTwitterInfoBead().pullData(infoCollectorState.lastCollectedTwitterId);
                    if (twitter != null && twitter.getInfoItem() != null && twitter.getInfoItem().getInfoValue().size() > 0) {
                        AppLogger.getInstance().writeLog(TAG, infName + " pull "
                                + twitter.getInfoItem().getInfoValue().size() + " items", AppLogger.LogLevel.TRACE);
                        infoCollectorState.newLastCollectedTwitterId =
                                twitter.getInfoItem().getInfoValue().get(twitter.getInfoItem().getInfoValue().size() - 1).id;
                        response.data.add(twitter);
                    } else {
                        AppLogger.getInstance().writeLog(TAG, infName + " pull no items", AppLogger.LogLevel.TRACE);
                    }
                    //AppLogger.getInstance().writeLog(TAG, infName + " pull end", AppLogger.LogLevel.TRACE);
                } catch (Exception ex) {
                    AppLogger.getInstance().writeLog(TAG, infName + " pull", ex, AppLogger.LogLevel.ERROR);
                    AppLogger.getInstance().writeLog(TAG, infName + " no items because of error", AppLogger.LogLevel.TRACE);
                }
            }
        }

		// YouTube
        if(collectYoutube) {
            infName = "YouTube info-bead";
            if(!YouTubeProvider.getInstance().isLoggedIn()){
                AppLogger.getInstance().writeLog(TAG, infName + " not logged in", AppLogger.LogLevel.TRACE);
            }
            else {
                try {
                    //AppLogger.getInstance().writeLog(TAG, infName + " pull start", AppLogger.LogLevel.TRACE);
                    Triplet<List<PlaylistItem>> youtube =
                            AppContext.getYouTubeInfoBead().pullData(infoCollectorState.lastTimeCollectedYouTube);
                    if (youtube != null && youtube.getInfoItem() != null && youtube.getInfoItem().getInfoValue().size() > 0) {
                        AppLogger.getInstance().writeLog(TAG, infName + " pull "
                                + youtube.getInfoItem().getInfoValue().size() + " items", AppLogger.LogLevel.TRACE);
                        infoCollectorState.newLastTimeCollectedYouTube = new Date();
                        response.data.add(youtube);
                    } else {
                        AppLogger.getInstance().writeLog(TAG, infName + " pull no items", AppLogger.LogLevel.TRACE);
                    }
                    //AppLogger.getInstance().writeLog(TAG, infName + " pull end", AppLogger.LogLevel.TRACE);
                } catch (Exception ex) {
                    AppLogger.getInstance().writeLog(TAG, infName + " pull", ex, AppLogger.LogLevel.ERROR);
                    AppLogger.getInstance().writeLog(TAG, infName + " no items because of error", AppLogger.LogLevel.TRACE);
                }
            }
        }
        infoCollectorState.lastCollection = new Date();
        JsonFileSettings.save(infoCollectorState,SETTINGS_FILE_PATH);
        return response;
    }
    public void pushData(Triplet<ArrayList<Triplet>> tripletToDeliver) {

        //Send to server
        String jsonString = "";

        infoCollectorState.lastSendAttempt = new Date();
        JsonFileSettings.save(infoCollectorState,SETTINGS_FILE_PATH);

        try {
            jsonString = gson.toJson(tripletToDeliver);

            try {
                HttpURLConnection httpcon;
                String url = apiUrl + "/send-data?d="+AppHelper.getInstance().getDeviceIdentificationID();
                String result = null;

                //Connect
                httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
                httpcon.setDoOutput(true);
                httpcon.setRequestProperty("Content-Type", "application/json");
                httpcon.setRequestProperty("Accept", "application/json");
                httpcon.setRequestMethod("POST");
                httpcon.setReadTimeout(httpReadTimeout);
                httpcon.setConnectTimeout(httpConnectionTimeout);
                httpcon.connect();
                AppLogger.getInstance().writeLog(TAG, "Sending data to " + url, AppLogger.LogLevel.TRACE);

                //Write
                OutputStream os = httpcon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonString);
                writer.close();
                os.close();

                int responseCode = httpcon.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    infoCollectorState.lastSendSuccess = new Date();
                    infoCollectorState.UpdateNewLastTime();
                    JsonFileSettings.save(infoCollectorState,SETTINGS_FILE_PATH);

                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        result+=line;
                    }
                }
                else {
                    result="";
                }

            } catch (UnsupportedEncodingException e) {
                AppLogger.getInstance().writeLog(TAG, "error = " + e.getMessage(), e, AppLogger.LogLevel.ERROR);
            } catch (IOException e) {
                AppLogger.getInstance().writeLog(TAG, "error = " + e.getMessage(), e, AppLogger.LogLevel.ERROR);
            }
        } catch (Throwable e) {
            AppLogger.getInstance().writeLog(TAG, "pushData tojson", e, AppLogger.LogLevel.ERROR);
            e.printStackTrace();
        }
        //AppLogger.getInstance().writeLog(TAG, "Data json => " + jsonString, AppLogger.LogLevel.TRACE);
    }
    private void SendLogFile(String level){
        try {
            File file = new File(AppSettingsManager.getInstance().getApplicationDir().getPath() +  "/log-"+level+".txt");

            StringBuffer logStr = new StringBuffer();
            ArrayList<String> lastLines = new ArrayList<String>();
            BufferedReader brg = null;
            if (!file.exists()) {
                return;
            }
            try {
                brg = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = brg.readLine()) != null) {
                    lastLines.add(line);
                    if (lastLines.size() == 300)
                        lastLines.remove(0);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            for(int i=0;i<lastLines.size();i++){
                logStr.append(lastLines.get(i));
            }

            String jsonString = "{\"id\":\""+AppHelper.getInstance().getDeviceIdentificationID()+"\","+
                    "\"level\":\""+level+"\",\"log\":\""+logStr.toString().replaceAll("\"","\\\"")+"\"}";

            try {
                HttpURLConnection httpcon;
                String url = apiUrl + "/send-log?d="+AppHelper.getInstance().getDeviceIdentificationID();
                String result = null;

                //Connect
                httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
                httpcon.setDoOutput(true);
                httpcon.setRequestProperty("Content-Type", "application/json");
                httpcon.setRequestProperty("Accept", "application/json");
                httpcon.setRequestMethod("POST");
                httpcon.setReadTimeout(httpReadTimeout);
                httpcon.setConnectTimeout(httpConnectionTimeout);
                httpcon.connect();

                //Write
                OutputStream os = httpcon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonString);
                writer.close();
                os.close();

                int responseCode = httpcon.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        result+=line;
                    }
                }
                else {
                    result="";
                }

            } catch (UnsupportedEncodingException e) {
                AppLogger.getInstance().writeLog(TAG, "error = " + e.getMessage(), e, AppLogger.LogLevel.ERROR);
            } catch (IOException e) {
                AppLogger.getInstance().writeLog(TAG, "error = " + e.getMessage(), e, AppLogger.LogLevel.ERROR);
            }
        } catch (Throwable e) {
            AppLogger.getInstance().writeLog(TAG, "pushData tojson", e, AppLogger.LogLevel.ERROR);
            e.printStackTrace();
        }
    }
	@Override
	public void run() {
        //while (running) {
            try {
                GenericResponse<ArrayList<Triplet>> resp = collectData();

                // Actually send data
                //if(resp.data.size()>0) {
                    AppLogger.getInstance().writeLog(TAG, "Sending data " + resp.data.size() + " triplets id="+AppHelper.getInstance().getDeviceIdentificationID(),
                            AppLogger.LogLevel.TRACE);

                    Triplet<ArrayList<Triplet>> tripletInstance = new Triplet<ArrayList<Triplet>>("InfoCollectorInfoBead");
                    InfoItem<ArrayList<Triplet>> data = new InfoItem<ArrayList<Triplet>>();
                    data.setInferenceTime(new Date());
                    data.setInfoValue(resp.data);
                    data.setSupplementalData(AppHelper.getInstance().getDeviceIdentificationID());
                    tripletInstance.setAllIds(this.getInfobeadAllIds());
                    tripletInstance.setTime(new Date());
                    tripletInstance.setInfoItem(data);

                    pushData(tripletInstance);
                //}
                //else{
                //    AppLogger.getInstance().writeLog(TAG, "Not sending data, nothing to send",AppLogger.LogLevel.TRACE);
                //}

                AppSettingsManager.getInstance().updateSendCount();
                AppSettingsManager.getInstance().save();
                if(AppSettingsManager.getInstance().getSendCount()%4==0){
                    SendLogFile("trace");
                    SendLogFile("error");
                }

                //Intent intent = new Intent();
                //intent.setAction(OnAlarmReceiver.SERVICE_DONE_ACTION);
                //AppContext.getContext().sendBroadcast(intent);

                //AppLogger.getInstance().writeLog(TAG, "run() sleep " +  Thread.currentThread().getId() +
                //        " for " + collectionInterval + " milliseconds",
                //        AppLogger.LogLevel.TRACE);
                //Thread.sleep(collectionInterval);
                //AppLogger.getInstance().writeLog(TAG, "run() waked up " +  Thread.currentThread().getId(),
                //        AppLogger.LogLevel.TRACE);

            } catch (Throwable e) {
                AppLogger.getInstance().writeLog(TAG, "run()",e, AppLogger.LogLevel.ERROR);
                e.printStackTrace();
            }
        //}
        stop();
	}
    public synchronized void start() {
        if(!running && infoCollectorThread.isAlive()) {
            started = true;
            running = true;
            AppLogger.getInstance().writeLog(TAG, "Started", AppLogger.LogLevel.TRACE);
            infoCollectorThread.start();
        }
    }
    public synchronized void stop() {
        started = false;
        running = false;
        if(infoCollectorThread!=null) {
            infoCollectorThread.interrupt();
        }
        Log.i(TAG, "stop()");
        AppLogger.getInstance().writeLog(TAG, "Stoped", AppLogger.LogLevel.TRACE);

        Intent i = new Intent();
        i.setAction(OnAlarmReceiver.SERVICE_DONE_ACTION);
        AppContext.getContext().sendBroadcast(i);
    }
    public boolean isStarted() {
        return started;
    }
    public boolean isRunning() {
        return running;
    }
	@Override
	public void destruct() {
        JsonFileSettings.save(infoCollectorState,SETTINGS_FILE_PATH);
		stop();
	}
}