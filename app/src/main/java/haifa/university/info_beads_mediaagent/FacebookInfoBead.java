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


/**
 * BrowseHistory InfoBead
 * @author Yuri Variat
 * @vesion  1.0;
 *
 */
public class FacebookInfoBead extends InfoBead<FBLike>{
    private static final String TAG="FacebookInfoBead";
	private static final long serialVersionUID = 1L;
    public FacebookInfoBead(){
        super();
        initialize();
    }
	public void initialize()
	{
        // -------------- set id ------------------------
        this.setInfoBeadId("FacebookInfoBead");
        this.setInfoBeadVersionId(String.valueOf(serialVersionUID));

        // -------------- set control part ------------------------
        InfoBeadControl control = new InfoBeadControl();
        control.setComMode(InfoBeadControl.ConnectionType.PULL);
        setControlPart(control);

        // -------------- set metadataPart ------------------------
        // setting up of the main components of the info-bead
        MetadataPart metadata = new MetadataPart();

        metadata.setInfoBeadName("FacebookInfoBead");
        metadata.setDescription("Info-bead, that returns posts liked by user from Facebook " +
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
    public Triplet<List<FBLike>> pullData(Date pullFromDate) {
        GenericResponse<ArrayList<FBLike>> likedPostsRes = FacebookProvider.getInstance().getLikes(pullFromDate);
        ArrayList<FBLike> likedPosts;

        if(likedPostsRes.isOK() && likedPostsRes.hasData()){
            likedPosts = likedPostsRes.data;
        }
        else{
            likedPosts = new ArrayList<>();
            if(!likedPostsRes.isOK()){
                AppLogger.getInstance().writeLog(TAG, likedPostsRes.getErrorsJSON(), AppLogger.LogLevel.ERROR);
            }
        }

        Triplet<List<FBLike>> triplet = new Triplet<>(this.getInfoBeadId());
        triplet.setAllIds(getInfobeadAllIds());

        InfoItem<List<FBLike>> infItem = new InfoItem<>();
        infItem.setInfoValue(likedPosts);
        triplet.setInfoItem(infItem);
        return triplet;
    }
    @Override
    public Triplet<List<FBLike>> pullData(){
        return pullData(null);
    }

	@Override
	public void destruct() {
	}

	// *********************************** Common Classes Definition *********************************** //
	// haifa.university.facebook_provider.FBLike
}


