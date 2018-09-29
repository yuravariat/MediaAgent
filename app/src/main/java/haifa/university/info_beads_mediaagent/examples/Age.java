package haifa.university.info_beads_mediaagent.examples;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.ArrayList;

import haifa.university.info_beads_general.InfoItem;
import haifa.university.info_beads_general.Triplet;
import haifa.university.info_beads_general.InfoBead;


/**
 * Age InfoBead
 * @author Yevgeni Mumblat
 * @vesion  1.0;
 *
 */
public class Age extends InfoBead implements Runnable {
	
		
	private static final long serialVersionUID = 1L;
	private volatile boolean running = true;
	
	private int outServPort;
	private DatagramSocket outgoingSock;

	
	public void initialize() 
	{
		outServPort = 15101;
		
		//Allocate new outgoing Socket
		try 
    	{
    		outgoingSock = new DatagramSocket();		
    	} 
    	catch (SocketException e2) 
    	{
    		// TODO Auto-generated catch block
    		e2.printStackTrace();
    	}
		
		Thread ageThread = new Thread(this,"Age");
		ageThread.start();				
	}

	public void handleData(Triplet inTriplet) 
	{
	
		Triplet tripletInstance = new Triplet("Age");
		
		InfoItem inputData = inTriplet.getInfoItem();
		Person myself = new Person();
		myself.IBUMDeserialize((String)inputData.getInfoValue());
		
		int age = myself.getAge(); 		
		
		Time t = new Time(System.currentTimeMillis());
		InfoItem data = new InfoItem();
		data.setInferenceTime(t);
		data.setExplainInfo("Age >> my age");
		data.setInfoType("Age");
		data.setInfoUnits("Years");
		data.setInfoValue(age);  
		tripletInstance.setTime(t);
		tripletInstance.setInfoItem(data);
		pushData(tripletInstance);
	}
	public void pushData(Triplet tripletToDeliver) {
		
		//String text;
        byte[] message = serializeTriplet(tripletToDeliver);
        
       	InetAddress local = null;
    	
    	try 
    	{
    		local = InetAddress.getByName("10.0.2.2");
    	} 
    	catch (UnknownHostException e2) 
    	{
    		// TODO Auto-generated catch block
    		e2.printStackTrace();
    	}
    	
		//    	/int msg_length=text.length();
        
    	DatagramPacket outDataPacket = new DatagramPacket(message, message.length,local,outServPort);
         
    	try 
    	{
    		outgoingSock.send(outDataPacket);
    	} 
    	catch (IOException e) 
    	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
	}
	
	private byte[] serializeTriplet(Triplet triplet)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(triplet);
		  byte[] tripletBytes = bos.toByteArray();
		  return tripletBytes;		  
		} catch (Exception ex) {
		    // ignore close exception
			 ex.printStackTrace();
		}
		//} catch (IOException ex) {
		//    // ignore close exception
		//}
		  
		try {
		    bos.close();
		} catch (IOException ex) {
		    // ignore close exception
		}
		
		if (out != null)
		try {
		     out.close();
		} catch (IOException ex) {
			    // ignore close exception
		}
		return null;	 		
	}
	
	
	@Override
	public void run() 
	{
		while (running)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	@Override
	public void destruct() {
		
		running = false;		
	}
	
	
	// ************************************** Common Class Person Definition ************************************** //  
	public enum GenderType {MALE, FEMALE};
	
	public class Person
	{
		private String fName;
		private String lName;
		private int idNum;
		private GenderType gender;
		private ArrayList<Person> kids = new ArrayList<Person>();
	    private int age;
	    private Person spouse;
		
		public Person (int idN,String lname, String fname, GenderType gndr, Person sps, ArrayList<Person> kds, int inAge)
		{
			this.fName = fname;
			this.lName = lname;
			this.idNum = idN;
			this.age = inAge;
			this.spouse = sps;
			this.gender = gndr;
			this.kids = kds;
		}
		
		private Person ()
		{
			this.spouse = null;
			this.kids = new ArrayList<Person>();
		}
		
		public int getIdNum() { return idNum;} 

		public GenderType getGender() { return gender; }

		public ArrayList<Person> getKids() { return kids; }

		public int getAge() { return age; }

		public Person getSpouse() {
			return spouse;
		}

		public void setSpouse(Person spouse) {
			this.spouse = spouse;
		}

	
		public String IBUMSerialize()
		{
			String tempSps = new String();
			
			if (this.spouse == null) {tempSps = "NULL;";}
			else 					 {tempSps = this.spouse.IBUMSerialize();}
			
			// fname_lname_id_age_gender ; spouse ; kid1 ; kid2 ; kid3 ... 
			String genderStr = (this.gender == GenderType.MALE)?"M":"F"; 
			String personStr = this.fName.concat("_").concat(this.lName).
					                      concat("_").concat(String.valueOf(this.idNum)).
					                      concat("_").concat(String.valueOf(this.age)).
					                      concat("_").concat(genderStr).
					                      concat(";").
					                      concat(tempSps);			
			
			String retVal = personStr;
						
			if (this.kids != null)
			{
				for (Person kid : this.kids)
				{
					retVal = retVal.concat(kid.IBUMSerialize());
				}
			}
			
			return retVal;
		}
		
		
		public void IBUMDeserialize(String inStr)
		{
			int i = 0;
			int firstKidLocation = 2;
			// fname_lname_id_age_gender ; spouse ; kid1 ; kid2 ; kid3 ... 
			
		    String[] splitPerson = inStr.split(";");

		    pDeserialize (this, splitPerson[0]);
		    
	    	if (splitPerson[1].equals("NULL")) 
	    	{
	    		this.spouse = null;
	    	}
	    	else
	    	{
	    		Person newSpouse = new Person();
	    		pDeserialize (newSpouse, splitPerson[1]);
	    		this.spouse = newSpouse;
	    		firstKidLocation = 3;
	    	}
		    
		    pDeserialize (this, splitPerson[0]);
		    
		    for (i = firstKidLocation ; (i < splitPerson.length) ; i+=2)
		    {
		    	Person newKid = new Person();
		    	pDeserialize (newKid, splitPerson[i]);
		    	this.kids.add(newKid);
		    }		
		}
		
		
		public void pDeserialize(Person p, String inStr)
		{
			// fname_lname_id_age_gender 
			
		    String[] splitPerson = inStr.split("_");

		    p.fName = splitPerson[0];
		    
		    p.lName = splitPerson[1];
		    
		    p.idNum = Integer.parseInt(splitPerson[2]);
		    
		    p.age = Integer.parseInt(splitPerson[3]);

		    GenderType genderStr = (splitPerson[4].equals("M"))?GenderType.MALE:GenderType.FEMALE; 
		    
		    this.gender = genderStr;
		}	
	}

	
}

