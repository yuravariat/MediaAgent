package haifa.university.info_beads_general;
import java.sql.Time;

/**
 * Info-bead input interface gets triplets of data from other info-beads
 * There may be 0 or more interfaces in an  info-bead
 * @author Eyal Dim
 * @version 1.0
 */
public interface InfoBeadInputInterface {
		
	/**
	 * Gets input triplets from another info-bead
	 * @param	senderID: The ID of the sending info-bead
	 * @paramsentTime: The time when the data was sent by the sending info-bead
	 * @paraminputData: a single Triplet as sent by the sending info-bead
	 */
	public Triplet getEvidence(String senderID, Time sentTime, Triplet inputData);

}
