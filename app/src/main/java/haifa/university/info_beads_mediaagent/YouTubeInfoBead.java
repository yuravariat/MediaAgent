package haifa.university.info_beads_mediaagent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import haifa.university.info_beads_general.InfoBead;
import haifa.university.info_beads_general.InfoBeadControl;
import haifa.university.info_beads_general.InfoItem;
import haifa.university.info_beads_general.MetadataPart;
import haifa.university.info_beads_general.Triplet;
import haifa.university.mediaagent.common.AppLogger;
import haifa.university.mediaagent.common.GenericResponse;
import haifa.university.twitter_provider.TwitterProvider;
import haifa.university.twitter_provider.TwitterTweet;
import haifa.university.youtube_provider.PlaylistItem;
import haifa.university.youtube_provider.YouTubeProvider;


/**
 * BrowseHistory InfoBead
 * @author Yuri Variat
 * @vesion  1.0;
 *
 */
public class YouTubeInfoBead extends InfoBead<PlaylistItem>{
    private static final String TAG="YouTubeInfoBead";
	private static final long serialVersionUID = 1L;
    public YouTubeInfoBead(){
        super();
        initialize();
    }
	public void initialize()
	{
        // -------------- set id ------------------------
        this.setInfoBeadId("YouTubeInfoBead");
        this.setInfoBeadVersionId(String.valueOf(serialVersionUID));

        // -------------- set control part ------------------------
        InfoBeadControl control = new InfoBeadControl();
        control.setComMode(InfoBeadControl.ConnectionType.PULL);
        setControlPart(control);

        // -------------- set metadataPart ------------------------
        // setting up of the main components of the info-bead
        MetadataPart metadata = new MetadataPart();

        metadata.setInfoBeadName("YouTubeInfoBead");
        metadata.setDescription("Info-bead, that returns user's watched mvideos from YouTube " +
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
    public Triplet<List<PlaylistItem>> pullData(Date pullFromDate) {

        if(!YouTubeProvider.getInstance().isLoggedIn()){
            // try to make silent login
            YouTubeProvider.getInstance().makeSilentLogin();
        }

        GenericResponse<List<PlaylistItem>> videosRes = YouTubeProvider.getInstance().getWatchHistoryPlayListItems(pullFromDate);
        List<PlaylistItem> videos;
        if(videosRes.isOK() && videosRes.hasData()){
            videos = videosRes.data;
        }
        else{
            videos = new ArrayList<>();
            if(!videosRes.isOK()){
                AppLogger.getInstance().writeLog(TAG, videosRes.getErrorsJSON(), AppLogger.LogLevel.ERROR);
            }
        }

        Triplet<List<PlaylistItem>> triplet = new Triplet<>(this.getInfoBeadId());
        triplet.setAllIds(getInfobeadAllIds());
        InfoItem<List<PlaylistItem>> infItem = new InfoItem<>();
        infItem.setInfoValue(videos);
        triplet.setInfoItem(infItem);
        return triplet;
    }
    @Override
    public Triplet<List<PlaylistItem>> pullData(){
        return pullData(null);
    }

	@Override
	public void destruct() {
	}

	// *********************************** Common Classes Definition *********************************** //
	// haifa.university.facebook_provider.FBLike
}


