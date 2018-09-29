package haifa.university.info_beads_general;

import java.net.URL;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Eyal Dim
 * @version 1.0
 * Info-bead metadata class - explains the behavior of the info-bead and its interfaces 
 */
public class MetadataPart implements Serializable{
	
	// initializing the metadata with empty or not applicable (N/A) data
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String 				infoBeadName = "N/A";					// Info-bead name
	private String 				description = "N/A";					// Info-bead description // Added by Yuri Variat on 1/10/2017
	private ArrayList<String> 	keyWords = new ArrayList<String>();		// keywords to assist in search for the info-bead
	private ArrayList<String>	infoUnits = new ArrayList<String>();	// units that may be used by this info-bead
	private URL 				helpFileURL = null;					// URL to a resource containing the help data
	private ArrayList<String> 	inputInterfaces = new ArrayList<String>();// a list of all input interfaces names
	private ArrayList<String> 	outputInterface = new ArrayList<String>();// a list of all output interface
	private URL					metadataFileURL = null;				// URL to the metadata file
	private String 				partNumber = "N/A";						// Specifying the id of the info-bead as specified by the provider
	private String				version = "N/A";						// Version ID
	private ArrayList<String>  	backwardCompatibility = new ArrayList<String>();// Which previous versions are supported
	private String				contact = "N/A";						// identify the supplier
	private String				trustworthiness = "N/A";				// Trustworthiness code 	
	private String				supplementalData = "N/A";				// Anything that the info-bead manufacturer would like to add 	
	

	//Getters and setters for each field


	public String getInfoBeadName() {
		return infoBeadName;
	}

	public void setInfoBeadName(String infoBeadName) {
		this.infoBeadName = infoBeadName;
	}

	public ArrayList<String> getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(ArrayList<String> keyWords) {
		this.keyWords = keyWords;
	}

	public ArrayList<String> getInfoUnits() {
		return infoUnits;
	}

	public void setInfoUnits(ArrayList<String> infoUnits) {
		this.infoUnits = infoUnits;
	}

	public URL getHelpFileURL() {
		return helpFileURL;
	}

	public void setHelpFileURL(URL help) {
		this.helpFileURL = help;
	}

	public ArrayList<String> getInputInterfaces() {
		return inputInterfaces;
	}

	public void setInputInterfaces(ArrayList<String> inputInterfaces) {
		this.inputInterfaces = inputInterfaces;
	}

	public ArrayList<String> getOutputInterface() {
		return outputInterface;
	}

	public void setOutputInterface(ArrayList<String> outputInterface) {
		this.outputInterface = outputInterface;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ArrayList<String> getBackwardCompatibility() {
		return backwardCompatibility;
	}

	public void setBackwardCompatibility(ArrayList<String> backwardCompatibility) {
		this.backwardCompatibility = backwardCompatibility;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTrustworthiness() {
		return trustworthiness;
	}

	public void setTrustworthiness(String trustworthiness) {
		this.trustworthiness = trustworthiness;
	}

	public URL getMetadataFileURL() {
		return metadataFileURL;
	}

	public void setMetadataFileURL(URL metadataFileURL) {
		this.metadataFileURL = metadataFileURL;
	}

	public String getSupplementalData() {
		return supplementalData;
	}

	public void setSupplementalData(String supplementalData) {
		this.supplementalData = supplementalData;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
