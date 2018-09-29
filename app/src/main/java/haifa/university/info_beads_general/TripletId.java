package haifa.university.info_beads_general;
import java.io.Serializable;

public class TripletId implements Serializable {
	
	private String infobeadID = ""; // The id of the info-bead in the model that sends the triplet
	private String infobeadVersionID = ""; // The version id of the info-bead in the model that sends the triplet
	private String modelID = "";   // The id of the model holding the info-bead.
							  //If it is a groupModel, the Id is expected to start with GM,
						 	  //if it is a UM the ID is expected to start with UM
	private String userOrGroupID = ""; // The id of the user or group associated with the model holding the info-bead.
									//If it is a group ID, the Id is expected to start with GI,
									//if it is a user ID the ID is expected to start with UI
	private String ownerID = ""; // if associated with an owner it should start with OI
	

	/**
	 * True if the info-bead is a user info-bead in a UM 
	 **/
	public boolean isUserModelInfobead(){
		if (!infobeadID.isEmpty()  &&  !modelID.isEmpty()){
			if (modelID.substring(0, 1).equals("UM")){
				return true;
				
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	
	/**
	 * True if the info-bead is a group info-bead in a GM
	 **/
	public boolean isGrouprModelInfobead(){
		if (!infobeadID.isEmpty()  &&  !modelID.isEmpty()){
			if (modelID.substring(0, 1).equals("GM")){
				return true;
				
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	
	/**
	 * True if the info-bead UM was associated with a user ID
	 **/
	public boolean hasUserIdAssociated(){
		if (!infobeadID.isEmpty()  &&  !modelID.isEmpty()  &&  !userOrGroupID.isEmpty()){
			if (modelID.substring(0, 1).equals("UM")  &&  userOrGroupID.substring(0, 1).equals("UI")){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	/**
	 * True if the info-bead GM was associated with a group or subgroup ID
	 **/
	public boolean hasGroupIdAssociated(){
		if (!infobeadID.isEmpty()  &&  !modelID.isEmpty()  &&  !userOrGroupID.isEmpty()){
			if (modelID.substring(0, 1).equals("GM")  &&  userOrGroupID.substring(0, 1).equals("GI")){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	
	/**
	 * True if the info-bead  was associated with an owner ID
	 **/
	public boolean hasOwnerIdAssociated(){
		if (!infobeadID.isEmpty()  &&  !ownerID.isEmpty() ){
			if (ownerID.substring(0, 1).equals("OI") ){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	
	//*********** getters and setters *********************
	public String getInfobeadID() {
		return infobeadID;
	}
	public void setInfobeadID(String infobeadID) {
		this.infobeadID = infobeadID;
	}
	public String getInfobeadVersionID() {
		return infobeadVersionID;
	}
	public void setInfobeadVersionID(String infobeadVersionID) {
		this.infobeadVersionID = infobeadVersionID;
	}
	public String getModelID() {
		return modelID;
	}
	public void setModelID(String modelID) {
		this.modelID = modelID;
	}
	public String getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}
	public String getUserOrGroupID() {
		return userOrGroupID;
	}
	public void setUserOrGroupID(String userOrGroupID) {
		this.userOrGroupID = userOrGroupID;
	}

}
