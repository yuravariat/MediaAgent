package haifa.university.mediaagent.common;

/**
 * Created by yura on 21/10/2015.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import haifa.university.mediaagent.service.OnAlarmReceiver;

public class AppSettingsManager {

    public static AppSettingsManager instance = null;
    private Context appContext = null;
    private AppSettings settings;

    private int sParameter = 10; // default value
    private File applicationDir = new File(Environment.getExternalStorageDirectory(), "MediaAgent");
    private final String SETTINGS_FILE_NAME = "settings.txt";
    private Date appStarted;

    public static AppSettingsManager getInstance() {
        if (instance == null) {
            instance = new AppSettingsManager();
            instance.appStarted = new Date();
        }
        return instance;
    }
    private AppSettingsManager() {
        super();
        load();
    }
    public void releaseInstance() {
        instance.clean();
        instance = null;
    }
    private void clean() {
    }
    public File getApplicationDir() {
        if(applicationDir==null || !applicationDir.exists()){
            applicationDir.mkdirs();
        }
        return applicationDir;
    }
    public AppSettings getSettings() {
        if(settings==null){
                settings = new AppSettings("new settings");
        }
        return settings;
    }
    public Context getAppContext() {
        return appContext;
    }
    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }
    public Date getAppStarted(){
        return appStarted;
    }
    public void save() {
        File f = new File(applicationDir, SETTINGS_FILE_NAME);

        if (!f.exists())
        {
            try
            {
                boolean mkdir = f.getParentFile().mkdirs();
                if(mkdir) {
                    f.createNewFile();
                }
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        String jsonString = "";
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            jsonString = gson.toJson(getSettings());
        } catch (Throwable e) {
            e.printStackTrace();
        }

        BufferedWriter br = null;
        try {
            br = new BufferedWriter(new FileWriter(f,false));
            br.write(jsonString);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.flush();
                    br.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void load() {

        StringBuffer jsonStr = new StringBuffer();

        File f = new File(applicationDir, SETTINGS_FILE_NAME);

        BufferedReader br = null;

        if (!f.exists()) {
            settings = new AppSettings("new settings");
            return;
        }
        try {
            br = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = br.readLine()) != null) {
                jsonStr.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            settings = new AppSettings("new settings");
            return;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String settingsStr = jsonStr.toString();

        if (settingsStr == null || settingsStr.equals("")) {
            settings = new AppSettings("new settings");
            return;
        }

        // set parameters
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            settings = gson.fromJson(settingsStr, AppSettings.class);
        } catch (Exception e) {
            settings = new AppSettings("new settings");
            e.printStackTrace();
        }
    }

    public Date getServiceStarted() {
        return settings.serviceStartedAt;
    }
    public void setServiceStarted(Date serviceStarted) {
        settings.serviceStartedAt = serviceStarted;
    }
    public boolean isServiceRunning() {
        return OnAlarmReceiver.isAlarmSet(AppContext.getContext());
        //return settings.isServiceStarted;
    }
    public void setServiceRunning(boolean serviceRunning) {
        settings.isServiceStarted = serviceRunning;
    }
    public int getSendCount() {
        return settings.sendCount;
    }
    public void updateSendCount() {
        settings.sendCount++;
        if(settings.sendCount == Integer.MAX_VALUE){
            settings.sendCount = 0;
        }
    }
    public class AppSettings {
        public AppSettings(String name){
            this.name = name;
        }
        public String name;
        public Date serviceStartedAt;
        public boolean isServiceStarted;
        public int sendCount;
    }
}

