package helper;

import java.util.ArrayList;
import java.util.Formatter;

import ptk.PhishedRecipient;

public class Printing {
	
	public static void printUsage() {
		System.out.println("USAGE: java -jar ptk.jar {command}");
		System.out.println("Commands:");
		System.out.println("   'simsend'      - simulates the send command by ouputting the recipients and email bodies");
		System.out.println("   'send'         - sends the emails");
		System.out.println("   'evaluate'     - evaluates which recipients clicked the link");
		System.out.println("   'evaluatefull' - evaluates which recipients clicked the link and prints information");
		System.out.println("   'clone {url}'  - clones the specified website to the current directory");
	}
	
	public static void printPhisedFull(ArrayList<PhishedRecipient> phishedRecipients) {
		System.out.println();
		Formatter fmt = new Formatter(); 
		System.out.println(fmt.format("%30s %25s %20s %10s %10s", "Target", "Datetime", "IP", "Pixel", "Clicks"));
		fmt.close(); fmt = new Formatter();
		System.out.println("===================================================================================================");
		for (PhishedRecipient phisedRecipient : phishedRecipients) {
		    System.out.println(
		    		fmt.format("%30s %25s %20s %10s %10s ", 
		    		phisedRecipient.getRecipient(),phisedRecipient.getDateTime(),phisedRecipient.getIP(),phisedRecipient.getReads(), phisedRecipient.getHits())
		    );
		    fmt.close(); fmt = new Formatter();
		    System.out.println();
		}
		fmt.close();
	}
	
	public static void printSimple(ArrayList<PhishedRecipient> phishedRecipients) {
		for (PhishedRecipient phisedRecipient : phishedRecipients) {
			System.out.println(phisedRecipient.getRecipient());
		}
	}
	
}
