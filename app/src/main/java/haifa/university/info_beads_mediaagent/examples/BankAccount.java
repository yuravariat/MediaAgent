package haifa.university.info_beads_mediaagent.examples;

import java.sql.Time;
import java.util.ArrayList;

import haifa.university.info_beads_general.InfoItem;
import haifa.university.info_beads_general.Triplet;
import haifa.university.info_beads_general.InfoBead;


/**
 * BankAccount InfoBead (Sensor)
 * @author Yevgeni Mumblat
 * @vesion  1.0;
 *
 */
public class BankAccount extends InfoBead implements Runnable {
	
	
	
	
	private static final long serialVersionUID = 1L;


	public void initialize() {
		Thread bankAccountThread = new Thread(this,"BankAccount");
		bankAccountThread.start();
				
	}

	public void handleData(Triplet tripletTest) {
		
	// no need for a sensor	
	
	}
	
	
	@Override
	public void run() {
		Triplet tripletInstance = new Triplet("BankAccount");
		while (true)
		{
			int randomSleep = 500 + (int)(Math.random() * ((2000 - 500) + 1));
			//System.out.println("<Bank Account> value is : " + n + " Sleep: " + randomSleep + "\n");
			Time t = new Time(System.currentTimeMillis());
			InfoItem data = new InfoItem();
			ProfitAndLoss pL = new ProfitAndLoss();
			pL.addPandLEntry("MortageReturn", "01.02.2015",-5216.00);
			pL.addPandLEntry("SalaryIncome", "01.02.2015",9821.00);
			pL.addPandLEntry("ElectricityCompany", "10.02.2015",-190.10);
			pL.addPandLEntry("LoanReturn", "10.02.2015",-2000.00);
			pL.addPandLEntry("CreditCard", "10.02.2015",-964.54);
			pL.addPandLEntry("CashDraw", "19.02.2015",-500.00);
			pL.addPandLEntry("MortageReturn", "01.03.2015",-5216.00);
			pL.addPandLEntry("SalaryIncome", "01.03.2015",10000.00);
			pL.addPandLEntry("ElectricityCompany", "10.03.2015",-200.10);
			pL.addPandLEntry("CreditCard", "10.03.2015",-300.00);
			pL.addPandLEntry("CashDraw", "16.03.2015",-500.00);
			data.setInferenceTime(t);
			data.setExplainInfo("Bank Account >> Checking Account");
			data.setInfoType("Money");
			data.setInfoUnits("Shekels");
			data.setInfoValue(pL.IBUMSerialize());  
			tripletInstance.setTime(t);
			tripletInstance.setInfoItem(data);
			pushData(tripletInstance);

			try {
				Thread.sleep(randomSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destruct() {
		// TODO Auto-generated method stub
		
	}
	

	
	// *********************************** Common Class ProfitAndLoss Definition *********************************** //  
	
	public class ProfitAndLoss {
		
		ArrayList<PlEntry> entries = new ArrayList<PlEntry>();
		
		public void addPandLEntry (String newSource, String newdate,double newAmount)
		{
			PlEntry newEntry = new PlEntry(newSource, newdate, newAmount);
			entries.add(newEntry);
		}
		
		public ArrayList<PlEntry> getEntries()
		{
			return entries;
		}
		
		class PlEntry
		{
			String source;
			String date;
			double amount;
			
			public PlEntry(String newSource, String newdate,double newAmount)
			{
				source = newSource;
				date = newdate;
				amount = newAmount;
			}
		}
		
		public ArrayList<String> IBUMSerialize()
		{
			ArrayList<String> retArray =  new ArrayList<String>();
			
			for (PlEntry currEntry : entries)
			{
				String strEntry = String.valueOf(currEntry.amount).concat("_").concat(currEntry.date).concat("_").concat(currEntry.source);
				retArray.add(strEntry);
			}
			
			return retArray;
		}
		
		public void IBUMDeserialize(ArrayList<String> newList)
		{
			for (String currEntry : newList)
			{
				String[] newEntryFields = currEntry.split("_");
				PlEntry newEntry = new PlEntry(newEntryFields[2], newEntryFields[1], Double.parseDouble(newEntryFields[0]));
				entries.add(newEntry);
			}
		}
		

	}

	
}
