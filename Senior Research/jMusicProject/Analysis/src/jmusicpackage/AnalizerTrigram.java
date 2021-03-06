package jmusicpackage;

import java.io.*;
//import java.math.BigDecimal;
import java.util.*;

public class AnalizerTrigram{
	//IMPORTANT:
	//point this at your library data file
	static File libraryPercentStorage = new File("Songs/_Current Library Analysis/Trigram/All.txt");
	//hard coded path was only used for testing, now being set by calls to analyze method
	static File songPhraseTreeStorage; //= new File("Test 3 Results/100 length left hand/Random Song Trees.txt");
	static String ARTIST1 = "Bach";
	static String ARTIST2 = "Chopin";
	static String ARTIST3 = "Mozart";
	static String ARTIST4 = "Byrd";

	public static void main(String[] args) {
		//used to work out how many composers are in the library data file (min of 2, max of 4)
		double artistcount = 0;

		TreeMap<String, Integer> songPhrasePitchTree = new TreeMap<String, Integer>();
	    TreeMap<String, Integer> songPhraseRhythmTree = new TreeMap<String, Integer>();

		TreeMap<String, Double> a1PercentPitchTree = new TreeMap<String, Double>();
	    TreeMap<String, Double> a1PercentRhythmTree = new TreeMap<String, Double>();

	    TreeMap<String, Double> a2PercentPitchTree = new TreeMap<String, Double>();
	    TreeMap<String, Double> a2PercentRhythmTree = new TreeMap<String, Double>();

	    TreeMap<String, Double> a3PercentPitchTree = new TreeMap<String, Double>();
	    TreeMap<String, Double> a3PercentRhythmTree = new TreeMap<String, Double>();

	    TreeMap<String, Double> a4PercentPitchTree = new TreeMap<String, Double>();
	    TreeMap<String, Double> a4PercentRhythmTree = new TreeMap<String, Double>();

		try{
			//read in from the percents
			if(libraryPercentStorage.exists()){
				Scanner fileScanner = new Scanner(libraryPercentStorage);
				boolean artist1 = false;
				boolean artist2 = false;
				boolean artist3 = false;
				boolean artist4 = false;
				boolean pitch = false;
				boolean rhythm = false;
				while(fileScanner.hasNext()){
					String input=fileScanner.nextLine();
					Scanner stringParser = new Scanner(input);
					String key = new String();
					double value = 0.0;
					String currentString = stringParser.next();
					if(!stringParser.hasNext()){
						if(currentString.equals(ARTIST1)){
							artist1 = true;
							artist2 = false;
							artist3 = false;
							artist4 = false;
							artistcount++;
						}
						else if(currentString.equals(ARTIST2)){
							artist1 = false;
							artist2 = true;
							artist3 = false;
							artist4 = false;
							artistcount++;
						}
						else if(currentString.equals(ARTIST3)){
							artist1 = false;
							artist2 = false;
							artist3 = true;
							artist4 = false;
							artistcount++;
						}
						else if(currentString.equals(ARTIST4)){
							artist1 = false;
							artist2 = false;
							artist3 = false;
							artist4 = true;
							artistcount++;
						}
						else if(currentString.equals("Pitch")){
							pitch = true;
							rhythm = false;
						}
						else if(currentString.equals("Rhythm")){
							pitch = false;
							rhythm = true;
						}
					}
					else {
						key = currentString;
						currentString = stringParser.next();
						key = key+" "+currentString;
						currentString = stringParser.next();
						key = key+" "+currentString;
						currentString = stringParser.next();
						double temp = Double.parseDouble(currentString);
						value = temp;
						if(artist1 && pitch){
							a1PercentPitchTree.put(key,value);
						}
						else if(artist1 && rhythm){
							a1PercentRhythmTree.put(key,value);
						}
						else if(artist2 && pitch){
							a2PercentPitchTree.put(key,value);
						}
						else if(artist2 && rhythm){
							a2PercentRhythmTree.put(key,value);
						}
						else if(artist3 && pitch){
							a3PercentPitchTree.put(key,value);
						}
						else if(artist3 && rhythm){
							a3PercentRhythmTree.put(key,value);
						}
						else if(artist4 && pitch){
							a4PercentPitchTree.put(key,value);
						}
						else if(artist4 && rhythm){
							a4PercentRhythmTree.put(key,value);
						}
					}
				}
				fileScanner.close();
			}
    	}
    	catch(FileNotFoundException e){}

    	try{
    		//read in from the individual song
			if(songPhraseTreeStorage.exists()){
				Scanner fileScanner = new Scanner(songPhraseTreeStorage);
				int totalsPointer = 0;
				boolean pitch = false;
				boolean rhythm = false;
				while(fileScanner.hasNext()){
					String input=fileScanner.nextLine();
					Scanner stringParser = new Scanner(input);
					String key = new String();
					int value = 0;
					String currentString = stringParser.next();
					if(!stringParser.hasNext()){
						if(totalsPointer == 0){
							pitch = true;
							rhythm = false;
						}
						else if(totalsPointer == 1){
							pitch = false;
							rhythm = true;
						}
						totalsPointer++;
					}
					else {
						key = currentString;
						currentString = stringParser.next();
						key = key+" "+currentString;
						currentString = stringParser.next();
						key = key+" "+currentString;
						currentString = stringParser.next();
						int temp = Integer.parseInt(currentString);
						value = temp;
						if(pitch){
							songPhrasePitchTree.put(key,value);
						}
						else if(rhythm){
							songPhraseRhythmTree.put(key,value);
						}
					}
				}
				fileScanner.close();
			}
    	}
    	//attempting to use log space to help prevent overflow/underflow
    	//do probability calculations
    	catch(FileNotFoundException e){}
		Iterator<String> songPhrasePitchIterator = songPhrasePitchTree.keySet().iterator();
        Iterator<String> songPhraseRhythmIterator = songPhraseRhythmTree.keySet().iterator();
        double artist1PitchLikelihood = Math.log10(1.0/artistcount);
        double artist2PitchLikelihood = Math.log10(1.0/artistcount);
        double artist3PitchLikelihood = Math.log10(1.0/artistcount);
        if(artistcount < 3) artist3PitchLikelihood = -Double.MAX_VALUE;
        double artist4PitchLikelihood = Math.log10(1.0/artistcount);
        if(artistcount < 4) artist4PitchLikelihood = -Double.MAX_VALUE;
        double artist1RhythmLikelihood = Math.log10(1.0/artistcount);
        double artist2RhythmLikelihood = Math.log10(1.0/artistcount);
        double artist3RhythmLikelihood = Math.log10(1.0/artistcount);
        if(artistcount < 3) artist3RhythmLikelihood = -Double.MAX_VALUE;
        double artist4RhythmLikelihood = Math.log10(1.0/artistcount);
        if(artistcount < 4) artist4RhythmLikelihood = -Double.MAX_VALUE;
        //smoothing is do-able here since there are only 12 pitch values relative to scale
        //however, this needs to be done in library construction. So for time being, doing a
        //temporary solution (which isn't likely to be hit anyways, since i'm fairly sure that
        //the library actually covers all or nearly all 1728 orderings for everyone just from the number of songs)
        while(songPhrasePitchIterator.hasNext()){
        	String curKey = songPhrasePitchIterator.next();
        	if(a1PercentPitchTree.get(curKey) != null){
        		artist1PitchLikelihood = artist1PitchLikelihood+(Math.log10(a1PercentPitchTree.get(curKey))*songPhrasePitchTree.get(curKey));
        	}
        	else{
        		artist1PitchLikelihood = artist1PitchLikelihood+(-10.0*songPhrasePitchTree.get(curKey));
        	}
        	if(a2PercentPitchTree.get(curKey) != null){
        		artist2PitchLikelihood = artist2PitchLikelihood+(Math.log10(a2PercentPitchTree.get(curKey))*songPhrasePitchTree.get(curKey));
        	}
        	else{
        		artist2PitchLikelihood = artist2PitchLikelihood+(-10.0*songPhrasePitchTree.get(curKey));
        	}
        	if(artistcount >= 3){
	        	if(a3PercentPitchTree.get(curKey) != null){
	        		artist3PitchLikelihood = artist3PitchLikelihood+(Math.log10(a3PercentPitchTree.get(curKey))*songPhrasePitchTree.get(curKey));
	        	}
	        	else{
	        		artist3PitchLikelihood = artist3PitchLikelihood+(-10.0*songPhrasePitchTree.get(curKey));
	        	}
        	}
        	if(artistcount >= 4) {
	        	if(a4PercentPitchTree.get(curKey) != null){
	        		artist4PitchLikelihood = artist4PitchLikelihood+(Math.log10(a4PercentPitchTree.get(curKey))*songPhrasePitchTree.get(curKey));
	        	}
	        	else{
	        		artist4PitchLikelihood = artist4PitchLikelihood+(-10.0*songPhrasePitchTree.get(curKey));
	        	}
        	}
        }
        //since there are a massive amount of possible rhythms, smoothing probably won't give anything but a 0
        //so i'm currently using a small probability i'm manually choosing so that it will still be counted
        //otherwise a song which doesn't match any rhythms will give 1/artistcount as the likelihood
        //currently using 1.0*10^-10 as there aren't any powers greater than -6 in the library so this is several orders smaller
        //which log10 of is simply -10.0
        while(songPhraseRhythmIterator.hasNext()){
        	String curKey = songPhraseRhythmIterator.next();
        	if(a1PercentRhythmTree.get(curKey) != null){
        		artist1RhythmLikelihood = artist1RhythmLikelihood+(Math.log10(a1PercentRhythmTree.get(curKey))*songPhraseRhythmTree.get(curKey));
        	}
        	else{
        		artist1RhythmLikelihood = artist1RhythmLikelihood+(-10.0*songPhraseRhythmTree.get(curKey));
        	}
        	if(a2PercentRhythmTree.get(curKey) != null){
        		artist2RhythmLikelihood = artist2RhythmLikelihood+(Math.log10(a2PercentRhythmTree.get(curKey))*songPhraseRhythmTree.get(curKey));
        	}
        	else{
        		artist2RhythmLikelihood = artist2RhythmLikelihood+(-10.0*songPhraseRhythmTree.get(curKey));
        	}
        	if(artistcount >= 3) {
	        	if(a3PercentRhythmTree.get(curKey) != null){
	        		artist3RhythmLikelihood = artist3RhythmLikelihood+(Math.log10(a3PercentRhythmTree.get(curKey))*songPhraseRhythmTree.get(curKey));
	        	}
	        	else{
	        		artist3RhythmLikelihood = artist3RhythmLikelihood+(-10.0*songPhraseRhythmTree.get(curKey));
	        	}
        	}
        	if(artistcount >= 4) {
	        	if(a4PercentRhythmTree.get(curKey) != null){
	        		artist4RhythmLikelihood = artist4RhythmLikelihood+(Math.log10(a4PercentRhythmTree.get(curKey))*songPhraseRhythmTree.get(curKey));
	        	}
	        	else{
	        		artist4RhythmLikelihood = artist4RhythmLikelihood+(-10.0*songPhraseRhythmTree.get(curKey));
	        	}
        	}
        }

        //write results
        File results = new File("results.txt");
        try{
    		BufferedWriter writer = new BufferedWriter(new FileWriter(results,false));
    		writer.write(artist1PitchLikelihood+artist1RhythmLikelihood+"\n");
    		writer.write(artist1PitchLikelihood+"\n");
    		writer.write(artist1RhythmLikelihood+"\n");
    		writer.write(artist2PitchLikelihood+artist2RhythmLikelihood+"\n");
    		writer.write(artist2PitchLikelihood+"\n");
    		writer.write(artist2RhythmLikelihood+"\n");
    		writer.write(artist3PitchLikelihood+artist3RhythmLikelihood+"\n");
    		writer.write(artist3PitchLikelihood+"\n");
    		writer.write(artist3RhythmLikelihood+"\n");
    		writer.write(artist4PitchLikelihood+artist4RhythmLikelihood+"\n");
    		writer.write(artist4PitchLikelihood+"\n");
    		writer.write(artist4RhythmLikelihood+"\n");
			writer.close();
        }
        catch(FileNotFoundException e){}
        catch(IOException e){}
	}

	//point at indicated song's tree and analyze
	static void analyze(String treeFile){
		songPhraseTreeStorage = new File(treeFile);
		String[] a = new String[0];
		main(a);
	}
}
