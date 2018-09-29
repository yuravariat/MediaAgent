package haifa.university.info_beads_mediaagent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import haifa.university.facebook_provider.FBLike;
import haifa.university.facebook_provider.FacebookProvider;
import haifa.university.info_beads_general.InfoBead;
import haifa.university.info_beads_general.InfoBeadControl;
import haifa.university.info_beads_general.InfoItem;
import haifa.university.info_beads_general.MetadataPart;
import haifa.university.info_beads_general.Triplet;
import haifa.university.mediaagent.common.AppLogger;
import haifa.university.mediaagent.common.GenericResponse;
import haifa.university.twitter_provider.TwitterProvider;
import haifa.university.twitter_provider.TwitterTweet;


/**
 * BrowseHistory InfoBead
 * @author Yuri Variat
 * @vesion  1.0;
 *
 */
public class TwitterInfoBead extends InfoBead<TwitterTweet>{
    private static final String TAG="TwitterInfoBead";
	private static final long serialVersionUID = 1L;
    public TwitterInfoBead(){
        super();
        initialize();
    }
	public void initialize()
	{
        // -------------- set id ------------------------
        this.setInfoBeadId("TwitterInfoBead");
        this.setInfoBeadVersionId(String.valueOf(serialVersionUID));

        // -------------- set control part ------------------------
        InfoBeadControl control = new InfoBeadControl();
        control.setComMode(InfoBeadControl.ConnectionType.PULL);
        setControlPart(control);

        // -------------- set metadataPart ------------------------
        // setting up of the main components of the info-bead
        MetadataPart metadata = new MetadataPart();

        metadata.setInfoBeadName("TwitterInfoBead");
        metadata.setDescription("Info-bead, that returns user's Tweets from Twitter " +
                "API on demand (pull method)");
        metadata.setVersion("v1.0");
        metadata.getBackwardCompatibility().add("v1.0");
        // Enter the default metadata into the info-bead
        this.setMetadata(metadata);
	}
	public void handleData(Triplet inTriplet)
	{
		// no need auth pull method.
	}
    public Triplet<List<TwitterTweet>> pullData(Long pullFrom) {
        GenericResponse<ArrayList<TwitterTweet>> tweetsRes = TwitterProvider.getInstance().getTweets(50,pullFrom);
        ArrayList<TwitterTweet> tweets;
        if(tweetsRes.isOK() && tweetsRes.hasData()){
            tweets = tweetsRes.data;
        }
        else{
            tweets = new ArrayList<>();
            if(!tweetsRes.isOK()){
                AppLogger.getInstance().writeLog(TAG, tweetsRes.getErrorsJSON(), AppLogger.LogLevel.ERROR);
            }
        }

        Triplet<List<TwitterTweet>> triplet = new Triplet<>(this.getInfoBeadId());
        triplet.setAllIds(getInfobeadAllIds());
        InfoItem<List<TwitterTweet>> infItem = new InfoItem<>();
        infItem.setInfoValue(tweets);
        triplet.setInfoItem(infItem);
        return triplet;
    }
    @Override
    public Triplet<List<TwitterTweet>> pullData(){
        return pullData(null);
    }

	@Override
	public void destruct() {
	}

	// *********************************** Common Classes Definition *********************************** //
	// haifa.university.facebook_provider.FBLike
}


