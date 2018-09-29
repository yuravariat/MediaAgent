package haifa.university.info_beads_general;

import java.util.ArrayList;

public class InfoBeadControl {
	/* Controls the behavior of the info-bead by:
	 * 1. Allowing it to use push, pull or notify communication modes
	 * 2. Allowing it to generate, store, and send data or to be in idle mode waiting
	 * 3. Keep track of which IDs are authorized to get data from the info-beads
	 */

	public enum ConnectionType 	{PUSH, PULL, NOTIFY};	// PUSH - pushing the attribute data as it arrives;
														// PULL - pulling the attribute data on demand
														// NOTIFY - notifying that new data has arrived
	public enum ActivationType {ON, OFF};				// ON- the info bead is active and reports data
														// OFF- the info-bead is waiting to turn on and does not report data


	private ConnectionType	comMode = ConnectionType.PUSH;	// How does this info-bead communicate with others - push is default
	private ActivationType 	onOff = ActivationType.ON ;		// Whether the info-bead is on or off. ON is default.
	private ArrayList<String>	authorizationToSendToID = new ArrayList<String>(); // a list of all IDs that it is permitted to send data to them. If empty, no restriction
	
	//getters and setters
	
	public ConnectionType getComMode() {
		return comMode;
	}
	public void setComMode(ConnectionType comMode) {
		this.comMode = comMode;
	}
	
	public ActivationType getOnOff() {
		return onOff;
	}
	private void setOnOff(ActivationType onOff) {
		this.onOff = onOff;
	}
	
		
	//control functions
	public void activate(){
		// Setting the info-bead to generate store and send data
		setOnOff(ActivationType.ON);
	}

	public void deactivate(){
		// Setting the info-bead to generate store and send data
		setOnOff(ActivationType.OFF);
	}
	
	public String authorize(String id){
		/*Adding an ID to the authorized list
		*return:
		*"Already authorized" if ID has alreay been included
		*"Authorized" if ID added to authorized list
		*"Illegal ID" if ID is null or blank or illegal
		*/
		if ((id != null) && (id.trim().length() > 0)) {
			if (authorizationToSendToID.contains(id)){
				return("Alredy authorized");
			}
			else {
				authorizationToSendToID.add(id);
				return("Authorized");
				
			}
		}
		else {
			return ("Illegal ID");
		}   
	}
	
	public String deauthorize(String id){
		/*Adding an ID to the authorized list
		*return:
		*"Authorization canceled" if ID was found in the list and deleted
		*"ID not found" if ID was not in the list
		*"Illegal ID"if id is null or blank or illegal
		*/
		if ((id != null) && (id.trim().length() > 0)) {
			int i = authorizationToSendToID.indexOf(id);
			if (i != -1){
				authorizationToSendToID.remove(i);
				return("Alredy authorizedAuthorization canceled");
			}
			else {
				return("ID not found");
				
			}
		}
		else {
			return ("Illegal ID");
		}   
	}
	
	public void defineCommunicationMode(ConnectionType comMode){
		setComMode(comMode);		
	}
	
	public void defineSettings(ConnectionType comMode1, ActivationType onOff1){
		// Sets both comMode and onOffMode
		setComMode(comMode1);
		setOnOff(onOff1);
	}
	

	// TODO public void addInformation (genericInfoBead.Triplet triplet); Left for the info-bead implementation.
	
	// TODO public void deleteInformation (genericInfoBead.Triplet triplet); Left for the info-bead implementation
}
