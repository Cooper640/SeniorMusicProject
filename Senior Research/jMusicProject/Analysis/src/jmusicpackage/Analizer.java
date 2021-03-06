package jmusicpackage;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class Analizer{

	static File libraryPercentStorage = new File("Test Bigram 1 Results/percents.txt");
	static File songPhraseTreeStorage = new File("Test 3 Results/100 length left hand/Random Song Trees.txt");


    static BigDecimal large = new BigDecimal(0);


    static TreeMap<Integer, Integer> songPhrasePitchTree = new TreeMap<Integer, Integer>();
    static TreeMap<Double, Integer> songPhraseRhythmTree = new TreeMap<Double, Integer>();

	public static void main(String[] args) {
		TreeMap<Integer, Double> bPercentPitchTree = new TreeMap<Integer, Double>();
	    TreeMap<Double, Double> bPercentRhythmTree = new TreeMap<Double, Double>();

	    TreeMap<Integer, Double> cPercentPitchTree = new TreeMap<Integer, Double>();
	    TreeMap<Double, Double> cPercentRhythmTree = new TreeMap<Double, Double>();

		try{
			if(libraryPercentStorage.exists()){
				Scanner fileScanner = new Scanner(libraryPercentStorage);
				boolean bach = false;
				boolean chopin = false;
				boolean pitch = false;
				boolean rhythm = false;
				while(fileScanner.hasNext()){
					String input=fileScanner.nextLine();
					Scanner stringParser = new Scanner(input);
					double key = 0.0;
					double value = 0.0;
					String currentString = stringParser.next();
					if(!stringParser.hasNext()){
						if(currentString.equals("Bach")){
							bach = true;
							chopin = false;
						}
						else if(currentString.equals("Chopin")){
							bach = false;
							chopin = true;
						}
						else if(currentString.equals("Pitches")){
							pitch = true;
							rhythm = false;
						}
						else if(currentString.equals("Rhythm")){
							pitch = false;
							rhythm = true;
						}
					}
					else {
						double temp = Double.parseDouble(currentString);
						key = temp;
						currentString = stringParser.next();
						temp = Double.parseDouble(currentString);
						value = temp;
						if(bach && pitch){
							bPercentPitchTree.put((int)key,value);
						}
						else if(bach && rhythm){
							bPercentRhythmTree.put(key,value);
						}
						else if(chopin && pitch){
							cPercentPitchTree.put((int)key,value);
						}
						else if(chopin && rhythm){
							cPercentRhythmTree.put(key,value);
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
					double key = 0.0;
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
						double temp = Double.parseDouble(currentString);
						key = temp;
						currentString = stringParser.next();
						int temp2 = Integer.parseInt(currentString);
						value = temp2;
						if(pitch){
							songPhrasePitchTree.put((int)key,value);
						}
						else if(rhythm){
							songPhraseRhythmTree.put(key,value);
						}
					}
				}
				fileScanner.close();
			}
    	}
    	catch(FileNotFoundException e){}
		Iterator<Integer> songPhrasePitchIterator = songPhrasePitchTree.keySet().iterator();
        Iterator<Double> songPhraseRhythmIterator = songPhraseRhythmTree.keySet().iterator();
        double bachPitchLikelihood = 0.5;
        double chopinPitchLikelihood = 0.5;
        double bachRhythmLikelihood = 0.5;
        double chopinRhythmLikelihood = 0.5;
        while(songPhrasePitchIterator.hasNext()){
        	int curKey = songPhrasePitchIterator.next();
        	if(bPercentPitchTree.get(curKey) != null){
        		bachPitchLikelihood = bachPitchLikelihood*Math.pow(bPercentPitchTree.get(curKey),songPhrasePitchTree.get(curKey));
        	}
        	if(cPercentPitchTree.get(curKey) != null){
        		chopinPitchLikelihood = chopinPitchLikelihood*Math.pow(cPercentPitchTree.get(curKey),songPhrasePitchTree.get(curKey));
        	}
        }
        while(songPhraseRhythmIterator.hasNext()){
        	double curKey = songPhraseRhythmIterator.next();
        	if(bPercentRhythmTree.get(curKey) != null){
        		bachRhythmLikelihood = bachRhythmLikelihood*Math.pow(bPercentRhythmTree.get(curKey),songPhraseRhythmTree.get(curKey));
        	}
        	if(cPercentRhythmTree.get(curKey) != null){
        		chopinRhythmLikelihood = chopinRhythmLikelihood*Math.pow(cPercentRhythmTree.get(curKey),songPhraseRhythmTree.get(curKey));
        	}
        }
        System.out.println("Pitch Likelyhood:");
        System.out.println("Bach: " + bachPitchLikelihood);
        System.out.println("Chopin: " + chopinPitchLikelihood);
        System.out.println(bachPitchLikelihood/chopinPitchLikelihood);
        System.out.println("Rhythm Likelyhood:");
        System.out.println("Bach: " + bachRhythmLikelihood);
        System.out.println("Chopin: " + chopinRhythmLikelihood);
        System.out.println(bachRhythmLikelihood/chopinRhythmLikelihood);

        File results = new File("results.txt");
        try{
        	if(results.exists()){
        		Scanner fileScanner = new Scanner(results);
        		double likelihood = Float.parseFloat(fileScanner.nextLine());
        		double pitch = Float.parseFloat(fileScanner.nextLine());
        		double rhythm = Float.parseFloat(fileScanner.nextLine());
        		fileScanner.close();
        		BufferedWriter writer = new BufferedWriter(new FileWriter(results,false));
        		writer.write((likelihood*((bachPitchLikelihood/chopinPitchLikelihood)*(bachRhythmLikelihood/chopinRhythmLikelihood)))+"\n");
        		large = large.multiply(new BigDecimal((bachPitchLikelihood/chopinPitchLikelihood)*(bachRhythmLikelihood/chopinRhythmLikelihood)));
        		System.out.println(large.doubleValue());
        		System.out.println(likelihood);
        		writer.write((pitch*(bachPitchLikelihood/chopinPitchLikelihood))+"\n");
        		writer.write((rhythm*(bachRhythmLikelihood/chopinRhythmLikelihood))+"\n");
        		writer.close();
        	}
    		else{
        		BufferedWriter writer = new BufferedWriter(new FileWriter(results,false));
        		writer.write(((bachPitchLikelihood/chopinPitchLikelihood)*(bachRhythmLikelihood/chopinRhythmLikelihood))+"\n");
    			large = new BigDecimal(((bachPitchLikelihood/chopinPitchLikelihood)*(bachRhythmLikelihood/chopinRhythmLikelihood)));
        		System.out.println(((bachPitchLikelihood/chopinPitchLikelihood)*(bachRhythmLikelihood/chopinRhythmLikelihood)));
    			writer.write((bachPitchLikelihood/chopinPitchLikelihood)+"\n");
    			writer.write((bachRhythmLikelihood/chopinRhythmLikelihood)+"\n");
    			writer.close();
    		}
        }
        catch(FileNotFoundException e){}
        catch(IOException e){};
	}

	static void analyze(String treeFile){
		songPhraseTreeStorage = new File(treeFile);
		String[] a = new String[0];
		main(a);
	}
}
