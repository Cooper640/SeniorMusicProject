package jmusicpackage;

import java.io.*;
//import java.math.BigDecimal;
import java.util.*;

public class AnalizerBigram{

	static File libraryPercentStorage = new File("Songs/_Current Library Analysis/Bigram/All.txt");
	//was only used in testing, now being set by calls to analyze method
	static File songPhraseTreeStorage = new File("Test 3 Results/100 length left hand/Random Song Trees.txt");
	static String ARTIST1 = "Bach";
	static String ARTIST2 = "Chopin";
	static String ARTIST3 = "Mozart";
	static String ARTIST4 = "Byrd";


    //static BigDecimal large = new BigDecimal(0);


    //static TreeMap<String, Integer> songPhrasePitchTree = new TreeMap<String, Integer>();
    //static TreeMap<String, Integer> songPhraseRhythmTree = new TreeMap<String, Integer>();

	public static void main(String[] args) {
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
						}
						else if(currentString.equals(ARTIST2)){
							artist1 = false;
							artist2 = true;
							artist3 = false;
							artist4 = false;
						}
						else if(currentString.equals(ARTIST3)){
							artist1 = false;
							artist2 = false;
							artist3 = true;
							artist4 = false;
						}
						else if(currentString.equals(ARTIST4)){
							artist1 = false;
							artist2 = false;
							artist3 = false;
							artist4 = true;
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
    	catch(FileNotFoundException e){}
		Iterator<String> songPhrasePitchIterator = songPhrasePitchTree.keySet().iterator();
        Iterator<String> songPhraseRhythmIterator = songPhraseRhythmTree.keySet().iterator();
        double artist1PitchLikelihood = Math.log10(0.25);
        double artist2PitchLikelihood = Math.log10(0.25);
        double artist3PitchLikelihood = Math.log10(0.25);
        double artist4PitchLikelihood = Math.log10(0.25);
        double artist1RhythmLikelihood = Math.log10(0.25);
        double artist2RhythmLikelihood = Math.log10(0.25);
        double artist3RhythmLikelihood = Math.log10(0.25);
        double artist4RhythmLikelihood = Math.log10(0.25);
        while(songPhrasePitchIterator.hasNext()){
        	String curKey = songPhrasePitchIterator.next();
        	if(a1PercentPitchTree.get(curKey) != null){
        		artist1PitchLikelihood = artist1PitchLikelihood+(Math.log10(a1PercentPitchTree.get(curKey))*songPhrasePitchTree.get(curKey));
        	}
        	if(a2PercentPitchTree.get(curKey) != null){
        		artist2PitchLikelihood = artist2PitchLikelihood+(Math.log10(a2PercentPitchTree.get(curKey))*songPhrasePitchTree.get(curKey));
        	}
        	if(a3PercentPitchTree.get(curKey) != null){
        		artist3PitchLikelihood = artist3PitchLikelihood+(Math.log10(a3PercentPitchTree.get(curKey))*songPhrasePitchTree.get(curKey));
        	}
        	if(a4PercentPitchTree.get(curKey) != null){
        		artist4PitchLikelihood = artist4PitchLikelihood+(Math.log10(a4PercentPitchTree.get(curKey))*songPhrasePitchTree.get(curKey));
        	}
        }
        while(songPhraseRhythmIterator.hasNext()){
        	String curKey = songPhraseRhythmIterator.next();
        	if(a1PercentRhythmTree.get(curKey) != null){
        		artist1RhythmLikelihood = artist1RhythmLikelihood+(Math.log10(a1PercentRhythmTree.get(curKey))*songPhraseRhythmTree.get(curKey));
        	}
        	if(a2PercentRhythmTree.get(curKey) != null){
        		artist2RhythmLikelihood = artist2RhythmLikelihood+(Math.log10(a2PercentRhythmTree.get(curKey))*songPhraseRhythmTree.get(curKey));
        	}
        	if(a3PercentRhythmTree.get(curKey) != null){
        		artist3RhythmLikelihood = artist3RhythmLikelihood+(Math.log10(a3PercentRhythmTree.get(curKey))*songPhraseRhythmTree.get(curKey));
        	}
        	if(a4PercentRhythmTree.get(curKey) != null){
        		artist4RhythmLikelihood = artist4RhythmLikelihood+(Math.log10(a4PercentRhythmTree.get(curKey))*songPhraseRhythmTree.get(curKey));
        	}
        }
        /*
        System.out.println("Pitch Likelyhood:");
        System.out.println(ARTIST1+": " + artist1PitchLikelihood);
        System.out.println(ARTIST2+": " + artist2PitchLikelihood);
        System.out.println(artist1PitchLikelihood-artist2PitchLikelihood);
        System.out.println("Rhythm Likelyhood:");
        System.out.println(ARTIST1+": " + artist1RhythmLikelihood);
        System.out.println(ARTIST2+": " + artist2RhythmLikelihood);
        System.out.println(artist1RhythmLikelihood-artist2RhythmLikelihood);
        */

        File results = new File("results.txt");
        try{
        	//this shouldn't be hit unless analyzing in chunks (or if forget to clear previous results)
        	/*
        	if(results.exists()){
        		Scanner fileScanner = new Scanner(results);
        		double likelihood = Float.parseFloat(fileScanner.nextLine());
        		double pitch = Float.parseFloat(fileScanner.nextLine());
        		double rhythm = Float.parseFloat(fileScanner.nextLine());
        		fileScanner.close();
        		BufferedWriter writer = new BufferedWriter(new FileWriter(results,false));
        		writer.write((likelihood*((artist1PitchLikelihood/artist2PitchLikelihood)*(artist1RhythmLikelihood/artist2RhythmLikelihood)))+"\n");
        		//large = large.multiply(new BigDecimal((artist1PitchLikelihood/artist2PitchLikelihood)*(artist1RhythmLikelihood/artist2RhythmLikelihood)));
        		//System.out.println(large.doubleValue());
        		System.out.println(likelihood);
        		writer.write((pitch*(artist1PitchLikelihood/artist2PitchLikelihood))+"\n");
        		writer.write((rhythm*(artist1RhythmLikelihood/artist2RhythmLikelihood))+"\n");
        		writer.close();
        	}
        	*/
        	//So currently this one will be used
    		//else{
    		BufferedWriter writer = new BufferedWriter(new FileWriter(results,false));
    		writer.write(artist1PitchLikelihood+artist1RhythmLikelihood+"\n");
    		writer.write(artist2PitchLikelihood+artist2RhythmLikelihood+"\n");
    		writer.write(artist3PitchLikelihood+artist3RhythmLikelihood+"\n");
    		writer.write(artist4PitchLikelihood+artist4RhythmLikelihood+"\n");
    		/*
    		double likelihood = Math.pow(10,(artist1PitchLikelihood-artist2PitchLikelihood)+(artist1RhythmLikelihood-artist2RhythmLikelihood));
    		double pitch = Math.pow(10,artist1PitchLikelihood-artist2PitchLikelihood);
    		double rhythm = Math.pow(10,artist1RhythmLikelihood-artist2RhythmLikelihood);
    		//overall
    		if(likelihood>1)writer.write("Overall: "+likelihood+" ("+ARTIST1+")\n");
    		else writer.write("Overall: "+likelihood+" ("+ARTIST2+")\n");
			//large = new BigDecimal(((artist1PitchLikelihood/artist2PitchLikelihood)*(artist1RhythmLikelihood/artist2RhythmLikelihood)));
    		System.out.println(likelihood);
			//pitch
    		if(pitch>1)writer.write("Pitch: "+pitch+" ("+ARTIST1+")\n");
    		else writer.write("Pitch: "+pitch+" ("+ARTIST2+")\n");
			//rhythm
    		if(rhythm>1)writer.write("Rhythm: "+rhythm+" ("+ARTIST1+")\n");
    		else writer.write("Rhythm: "+rhythm+" ("+ARTIST2+")\n");
    		*/
			writer.close();
    		//}
        }
        catch(FileNotFoundException e){}
        catch(IOException e){}
	}

	static void analyze(String treeFile){
		songPhraseTreeStorage = new File(treeFile);
		String[] a = new String[0];
		main(a);
	}
}
