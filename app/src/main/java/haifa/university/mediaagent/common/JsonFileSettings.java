package haifa.university.mediaagent.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;

import haifa.university.info_beads_mediaagent.InfoCollectorInfoBead;
import haifa.university.info_beads_mediaagent.InfoCollectorState;
import haifa.university.mediaagent.common.AppLogger;

/**
 * Created by yura on 15/06/2016.
 */
public class JsonFileSettings {
    private static final String TAG="JsonFileSettings";
    private static Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
    private JsonFileSettings(){}
    public static void save(Object objToSave, String filepath) {
        File f = new File(filepath);

        if (!f.exists()) {
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String jsonString = "";
        try {
            jsonString = gson.toJson(objToSave);
        } catch (Throwable e) {
            AppLogger.getInstance().writeLog(TAG, "saveInfoCollectorState tojson", e, AppLogger.LogLevel.ERROR);
            e.printStackTrace();
        }

        BufferedWriter br = null;
        try {
            br = new BufferedWriter(new FileWriter(f,false));
            br.write(jsonString);
        } catch (Throwable e) {
            e.printStackTrace();
            AppLogger.getInstance().writeLog(TAG, "saveInfoCollectorState write buffer", e, AppLogger.LogLevel.ERROR);
        } finally {
            if (br != null) {
                try {
                    br.flush();
                    br.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                    AppLogger.getInstance().writeLog(TAG, "saveInfoCollectorState save", e, AppLogger.LogLevel.ERROR);
                }
            }
        }
    }
    public static <T> T load(Class<T> cls,String filepath) {

        StringBuffer jsonStr = new StringBuffer();

        File f = new File(filepath);

        BufferedReader br = null;

        if (!f.exists()) {
            return null;
        }
        try {
            br = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = br.readLine()) != null) {
                jsonStr.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            AppLogger.getInstance().writeLog(TAG, "save read", e, AppLogger.LogLevel.ERROR);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    AppLogger.getInstance().writeLog(TAG, "save read2", e, AppLogger.LogLevel.ERROR);
                }
            }
        }
        T obj=null;
        try {
            String settingsStr = jsonStr.toString();
            obj = gson.fromJson(settingsStr, cls);
        }
        catch (Exception e){
            e.printStackTrace();
            AppLogger.getInstance().writeLog(TAG, "deserialize error", e, AppLogger.LogLevel.ERROR);
            return null;
        }
        catch (Throwable e){
            e.printStackTrace();
            AppLogger.getInstance().writeLog(TAG, "deserialize error", e, AppLogger.LogLevel.ERROR);
            return null;
        }
        return obj;
    }
}
