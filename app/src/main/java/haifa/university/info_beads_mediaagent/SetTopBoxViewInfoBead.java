package haifa.university.info_beads_mediaagent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import haifa.university.browser_provider.Bookmark;
import haifa.university.browser_provider.BrowserProvider;
import haifa.university.info_beads_general.InfoBead;
import haifa.university.info_beads_general.InfoBeadControl;
import haifa.university.info_beads_general.InfoItem;
import haifa.university.info_beads_general.MetadataPart;
import haifa.university.info_beads_general.Triplet;
import haifa.university.mediaagent.common.AppContext;
import haifa.university.mediaagent.common.AppLogger;
import haifa.university.mediaagent.common.GenericResponse;
import haifa.university.set_top_box_provider.SetTopBoxView;


/**
 * BrowseHistory InfoBead
 * @author Yuri Variat
 * @vesion  1.0;
 *
 */
public class SetTopBoxViewInfoBead extends InfoBead<SetTopBoxView>{
    private static final String TAG="BrowseHistoryInfoBead";
	private static final long serialVersionUID = 1L;

    public SetTopBoxViewInfoBead(){
        super();
        initialize();
    }
	public void initialize()
	{
        // -------------- set id ------------------------
        this.setInfoBeadId("BrowseHistoryInfoBead");
        this.setInfoBeadVersionId(String.valueOf(serialVersionUID));

        // -------------- set control part ------------------------
        InfoBeadControl control = new InfoBeadControl();
        control.setComMode(InfoBeadControl.ConnectionType.PULL);
        setControlPart(control);

        // -------------- set metadataPart ------------------------
        // setting up of the main components of the info-bead
        MetadataPart metadata = new MetadataPart();

        metadata.setInfoBeadName("BrowsersHistoryInfobead");
        metadata.setDescription("Info-bead, that returns browse history on demand (pull method)");
        metadata.setVersion("v1.0");
        metadata.getBackwardCompatibility().add("v1.0");
        // Enter the default metadata into the info-bead
        this.setMetadata(metadata);
	}
	public void handleData(Triplet inTriplet)
	{
		// no need auth pull method.
	}
    public Triplet<List<SetTopBoxView>> pullData(Date pullFromDate) {
        GenericResponse<List<Bookmark>> bookmarks = BrowserProvider.getInstance().getHistoryFromAllBrowsers(AppContext.getContext(), pullFromDate);
        Triplet<List<SetTopBoxView>> triplet = new Triplet<>(this.getInfoBeadId());
        triplet.setAllIds(getInfobeadAllIds());
        ArrayList<SetTopBoxView> historyBookmarks = new ArrayList<SetTopBoxView>();
//        if(bookmarks.isOK() && bookmarks.hasData()) {
//            for (Bookmark b : bookmarks.data) {
//                SetTopBoxView hb = new SetTopBoxView();
//                hb.url = b.url;
//                hb.title = b.title;
//                hb.visits = b.visits;
//                hb.created = b.created;
//                hb.lastVisited = b.lastVisited;
//                historyBookmarks.add(hb);
//            }
//        }
//        else if(!bookmarks.isOK()){
//            AppLogger.getInstance().writeLog(TAG, bookmarks.getErrorsJSON(), AppLogger.LogLevel.ERROR);
//        }
        InfoItem<List<SetTopBoxView>> infItem = new InfoItem<List<SetTopBoxView>>();
        infItem.setInfoValue(historyBookmarks);
        triplet.setInfoItem(infItem);
        return triplet;
    }
    @Override
    public Triplet<List<SetTopBoxView>> pullData(){
        return pullData(null);
    }

	@Override
	public void destruct() {
	}
}


