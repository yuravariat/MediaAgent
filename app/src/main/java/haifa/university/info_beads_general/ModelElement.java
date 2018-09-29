package haifa.university.info_beads_general;




public interface ModelElement {
	
	/**
	 * @author Eyal Dim
	 * @version 1.0
	 * create a link to an element
	 * @param element
	 */
	void connect(ModelElement element);
	
	/**
	 * Start running the Component
	 * @param id	- the id of the Component in the user model 
	 */
	void activate(String id);
	
}
