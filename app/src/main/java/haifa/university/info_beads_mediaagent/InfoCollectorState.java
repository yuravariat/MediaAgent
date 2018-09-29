package haifa.university.info_beads_mediaagent;

import java.util.Date;

/**
 * Created by yura on 17/06/2016.
 */
public class InfoCollectorState {

    public InfoCollectorState(){}

    public String version;
    public Date lastCollection;
    public Date lastSendAttempt;
    public Date lastSendSuccess;

    public Date lastTimeCollectedHistory;
    public Date lastTimeCollectedFacebook;
    public long lastCollectedTwitterId;
    public Date lastTimeCollectedYouTube;
    public Date newLastTimeCollectedHistory;
    public Date newLastTimeCollectedFacebook;
    public long newLastCollectedTwitterId;
    public Date newLastTimeCollectedYouTube;

    public void UpdateNewLastTime(){
        if(newLastTimeCollectedHistory!=null){
            lastTimeCollectedHistory = newLastTimeCollectedHistory;
        }
        if(newLastTimeCollectedFacebook!=null){
            lastTimeCollectedFacebook = newLastTimeCollectedFacebook;
        }
        if(newLastCollectedTwitterId>0){
            lastCollectedTwitterId = newLastCollectedTwitterId;
        }
        if(newLastTimeCollectedYouTube!=null){
            lastTimeCollectedYouTube = newLastTimeCollectedYouTube;
        }
        newLastTimeCollectedHistory = null;
        newLastTimeCollectedFacebook= null;
        newLastCollectedTwitterId= 0L;
        newLastTimeCollectedYouTube= null;
    }
}
