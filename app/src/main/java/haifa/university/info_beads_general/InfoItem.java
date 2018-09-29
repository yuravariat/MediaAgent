package haifa.university.info_beads_general;

import java.util.Date;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Eyal Dim
 * @version 1.0
 * 
 * The info-item holds the info-beads value as well as all the evidence data that is relevant to a value 
 * that the info-bead holds. The evidence data helps explaining the data
 *
 */
public class InfoItem<TypeOfInfoItemValue> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String infoValueType;  // the type of the value - assisting in understanding what value is it
	private TypeOfInfoItemValue infoValue;	// the value itself
	private String infoUnits;   // the units of the value
	
	private ArrayList<Triplet> evidence;  // holds all triplets of data that was used as evidence 
									      // for generating the info-beads value
    private Date evidenceStartTime; 	  // the UTC time of the first evidence
    private Date evidenceEndTime; 		  // the UTC time of the last evidence
	
	private String explainInfo; // a string that explains how the value was generated
	
	//accuracy values
	private String 	infoAccuracy; // the assessed accuracy of the value in units of infoValueType (+/- 1 sigma)
	private String 	infoResolution; // the resolution of the representation of the value in units of infoValueType 
	private double 	infoConfidence; // 0 - no confidence, 1- full confidence, in between - level of confidence
	
	private String  timeUnits;    // the time units of the reported value time
	private int		timeAccuracy; // the accuracy of time representation in  milliseconds
	private double	sampleRate;	  // in Hertz (time per second)
		
	//time values
	private Date	infoValidFrom;  // the UTC time when the value becomes valid
	private Date	infoExpiration; // the UTC time when the value is no longer valid
	private Date	receptionTime;  // the UTC time when the value was received
	private Date 	inferenceTime;  // the UTC time when the value was calculated
	
	private String  supplementalData;  // anything that the info-bead producer would like to add

	//Setters and getters
	
	/**
	 *  Used to compare type of input from another info-bead with expected type
	 *  @return the information item type within a string 
	 */
	public String getInfoType() {
		return infoValueType;
	}
	
	/**
	 * Sets the information item type within a string.
	 * Used to compare type of the sent information by the receiving info-bead that expects a specific type
	 * @param infoType
	 */
	public void setInfoType(String infoType) {
		this.infoValueType = infoType;
	}
	
	
	/**
	 * Get the value of the info-bead attribute as kept by the info-item
	 * @return the value having the attribute type
	 */
	public TypeOfInfoItemValue getInfoValue() {
		return infoValue;
	}
	
	/**
	 * Set the value of the info-bead attribute as kept by the info-item
	 * @param info the information to be stored
	 */
	public void setInfoValue(TypeOfInfoItemValue info) {
		this.infoValue = info;
	}
	
	
	/**
	 * Get the units if the information item (e.g. for length, meter, millimeter, mile, etc.)
	 * @return a String containing the units description
	 */
	public String getInfoUnits() {
		return infoUnits;
	}
	
	/**
	 * Set the units if the information item (e.g. for length, meter, millimeter, mile, etc.)
	 * @param infoUnits a String containing the units description
	 */
	public void setInfoUnits(String infoUnits) {
		this.infoUnits = infoUnits;
	}
	
	/**
	 * Get the list of evidence that contributed to the inferred value
	 * @return the list of evidence in a Triplet format
	 */
	public ArrayList<Triplet> getEvidence() {
		return evidence;
	}
	
	/**
	 * 	Set the list of evidence that contributed to the inferred value
	 * @param evidence the list of evidence in a Triplet format
	 */
	public void setEvidence(ArrayList<Triplet> evidence) {
		this.evidence = evidence;
	}
	
	/**
	 * Gets the explanation in regards to how the info-bead value was inferred from the evidence
	 * @return a string containing the explanation
	 */
	public String getExplainInfo() {
		return explainInfo;
	}
	
	/**
	 * Sets the explanation in regards to how the info-bead value was inferred from the evidence
	 * @param explainInfo a string containing the explanation
	 */
	public void setExplainInfo(String explainInfo) {
		this.explainInfo = explainInfo;
	}
	
	/**
	 * Gets the assumed accuracy of the inferred info-bead value
	 * @return a string describing the accuracy
	 */
	public String getInfoAccuracy() {
		return infoAccuracy;
	}
	
	/**
	 * Sets the assumed accuracy of the inferred info-bead value 
	 * @param infoAccuracy a string describing the accuracy
	 */
	public void setInfoAccuracy(String infoAccuracy) {
		this.infoAccuracy = infoAccuracy;
	}
	
	/**
	 * Gets the resolution of the inferred info-bead value
	 * @return a string describing the resolution
	 */
	public String getInfoResolution() {
		return infoResolution;
	}

	/**
	 * Sets the resolution of the inferred info-bead value
	 * @param infoResolution a string describing the resolution
	 */
	public void setInfoResolution(String infoResolution) {
		this.infoResolution = infoResolution;
	}
	
	/**
	 * Get a value that represents the level of confidence in the inferred info-bead attribute.
	 * The value is expected to be between 0 (no confidence) and one (full confidence) or -1 (no data)
	 * @return confidence level in a double format
	 */
	public double getInfoConfidence() {
		return infoConfidence;
	}
	
	/**
	 * Set a value that represents the level of confidence in the inferred info-bead attribute.
	 * The value is expected to be between 0 (no confidence) and one (full confidence) or -1 (no data)
	 * @param infoConfidence the confidence level in a double format
	 */
	public void setInfoConfidence(double infoConfidence) {
		this.infoConfidence = infoConfidence;
	}
	
	/**
	 * Gets the time accuracy of the inferred data 
	 * (e.g. 1 year, 100 millisecond, where the "time units" are available by using getTimeUnits() )
	 * @return an int representing the accuracy by the number of "units"  
	 */
	public int getTimeAccuracy() {
		return timeAccuracy;
	}
	
	/**
	 * Sets the time accuracy of the inferred data 
	 * (e.g. 1 year, 100 millisecond, where the "time units" are available by using getTimeUnits() )
	 * @param timeAccuracy an int representing the accuracy by the number of "units"
	 */
	public void setTimeAccuracy(int timeAccuracy) {
		this.timeAccuracy = timeAccuracy;
	}
	
	/**
	 * Gets the sample rate of the inferred data 
	 * @return the sample rate as a double value of time units as available by using getTimeUnits() -
	 * (e.g. 1.5 months )
	 */
	public double getSampleRate() {
		return sampleRate;
	}

	/**
	 * Sets the sample rate of the inferred data 
	 * @param sampleRate the sample rate as a double value of time units as available by using getTimeUnits() -
	 * (e.g. 1.5 months )
	 */
	public void setSampleRate(double sampleRate) {
		this.sampleRate = sampleRate;
	}
	
	/**
	 * Gets the first date when the info-bead value became or would become valid
	 * @return the date of validity
	 */
	public Date getInfoValidityStartTime() {
		return infoValidFrom;
	}
	
	/**
	 * Sets the first date and time when the info-bead value became or would become valid
	 * @param infoStartTime the date of validity
	 */
	public void setInfoValidityStartTime(Date infoStartTime) {
		this.infoValidFrom = infoStartTime;
	}
	
	/**
	 * Gets the first date and time when the info-beads value became or would become invalid (i.e. expire) 
	 * @return the date of expiration
	 */
	public Date getInfoValidityEndTime() {
		return infoExpiration;
	}
	
	/**
	 * Sets the first date and time when the info-beads value became or would become invalid (i.e. expire) 
	 * @param infoEndTime the date of expiration
	 */
	public void setInfoValidityEndTime(Date infoEndTime) {
		this.infoExpiration = infoEndTime;
	}

	/**
	 * Get the time when an evidence Triplet was received by the info-bead
	 * @return date and time of evidence reception
	 */
	public Date getReceptionTime() {
		return receptionTime;
	}

	/**
	 * Set the time when an evidence Triplet was received by the info-bead
	 * @param receptionTime date and time of evidence reception
	 */
	public void setReceptionTime(Date receptionTime) {
		this.receptionTime = receptionTime;
	}

	/**
	 * Get the time units related to the info-bead in a string format (e.g. "year", "second").
	 * @return a string describing the time units
	 */
	public String getTimeUnits() {
		return timeUnits;
	}
	
	
	/**
	 * Set the time units related to the info-bead in a string format (e.g. "year", "second").
	 * @param timeUnits  a string describing the time units
	 */
	public void setTimeUnits(String timeUnits) {
		this.timeUnits = timeUnits;
	}
	
	/**
	 * Get the date and time when the info-bead value was inferred
	 * @return date and time in a Date format
	 */
	public Date getInferenceTime() {
		return inferenceTime;
	}
	
	/**
	 * Set the date and time when the info-bead value was inferred
	 * @param inferenceTime date and time in a Date format
	 */
	public void setInferenceTime(Date inferenceTime) {
		this.inferenceTime = inferenceTime;
	}
	
	/**
	 * Get the time when the evidence participating in the inference was first collected 
	 * @return date and time in a Date format
	 */
	public Date getEvidenceStartTime() {
		return evidenceStartTime;
	}
	
	/**
	 * Set the time when the evidence participating in the inference  was first collected 
	 * @return date and time in a Date format
	 */	
	public void setEvidenceStartTime(Date evidenceStartTime) {
		this.evidenceStartTime = evidenceStartTime;
	}
	
	/**
	 * Get the time when the evidence participating in the inference was last to be collected
	 * If there is a single evidence then evidenceEndTime should be equal to evidenceStartTime
	 * @return date and time in a Date format
	 */
	public Date getEvidenceEndTime() {
		return evidenceEndTime;
	}

	/**
	 * Seret the time when the evidence participating in the inference was last to be collected
	 * If there is a single evidence then evidenceEndTime should be equal to evidenceStartTime
	 * @param evidenceEndTime date and time in a Date format
	 */	
	public void setEvidenceEndTime(Date evidenceEndTime) {
		this.evidenceEndTime = evidenceEndTime;
	}

	/**
	 * Get a string for anything that the info-bead producer would like to add in regards to the data
	 * @return a string
	 */
	public String getSupplementalData() {
		return supplementalData;
	}

	/**
	 * Set a string for anything that the info-bead producer would like to add in regards to the data
	 * @param supplementalData a string
	 */
	public void setSupplementalData(String supplementalData) {
		this.supplementalData = supplementalData;
	}
	
	public InfoItem getInfoItem(){
		return this;
	}
	

}
