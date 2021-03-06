package jmusicpackage;

import java.io.*;
import java.util.*;

import jm.JMC;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;

public class GeneralReaderUnigram implements JMC {

	static int pitchTotal = 0;
	//static int pitchByKeyTotal = 0;
	static int rhythmTotal = 0;

	public static void read(String songPath, String name){

		TreeMap<Integer, Integer> pitchTree = new TreeMap<Integer, Integer>();
		//TreeMap<String, Integer> pitchByKeyTree = new TreeMap<String, Integer>();
        TreeMap<Double, Integer> rhythmTree = new TreeMap<Double, Integer>();
        //fetch pitch tree (if exists)
        File pitchTreeStorage = new File(name+" Pitch Tree.txt");
    	try{
			if(pitchTreeStorage.exists()){
				Scanner fileScanner = new Scanner(pitchTreeStorage);
				String input=fileScanner.nextLine();
				Scanner stringParser = new Scanner(input);
				pitchTotal = stringParser.nextInt();
				while(fileScanner.hasNext()){
					input=fileScanner.nextLine();
					stringParser = new Scanner(input);
					int key = 0;
					int value = 0;
					int numberOfNumbers = 0;
					while(stringParser.hasNext()){
						int temp = stringParser.nextInt();
						if(numberOfNumbers == 0){
							key = temp;
						}
						else if(numberOfNumbers == 1){
							value = temp;
						}
						numberOfNumbers++;
					}
					pitchTree.put((int)key,value);
					stringParser.close();
				}
				fileScanner.close();
			}
    	}
    	catch(FileNotFoundException e){}

    	//fetch rhythm tree (if exists)
        File rhythmTreeStorage = new File(name+" Rhythm Tree.txt");
    	try{
			if(rhythmTreeStorage.exists()){
				Scanner fileScanner = new Scanner(rhythmTreeStorage);
				String input=fileScanner.nextLine();
				Scanner stringParser = new Scanner(input);
				rhythmTotal = stringParser.nextInt();
				while(fileScanner.hasNext()){
					input=fileScanner.nextLine();
					stringParser = new Scanner(input);
					double key = 0.0;
					int value = 0;
					int numberOfNumbers = 0;
					while(stringParser.hasNext()){
						double temp = Double.parseDouble(stringParser.next());
						if(numberOfNumbers == 0){
							key = temp;
						}
						else if(numberOfNumbers == 1){
							value = (int)temp;
						}
						numberOfNumbers++;
					}
					rhythmTree.put(key,value);
					stringParser.close();
				}
				fileScanner.close();
			}
    	}
    	catch(FileNotFoundException e){}

    	/*
    	//fetch rhythm tree (if exists)
        File pitchByKeyTreeStorage = new File(name+" Pitch By Key Tree.txt");
    	try{
    		if(rhythmTreeStorage.exists()){
				Scanner fileScanner = new Scanner(rhythmTreeStorage);
				String input=fileScanner.nextLine();
				Scanner stringParser = new Scanner(input);
				pitchByKeyTotal = stringParser.nextInt();
    		}
    	}
    	catch(FileNotFoundException e){}
    	*/

    	//set up midi reading
    	Score song = new Score();
        Read.midi(song, songPath);
        Part[] songParts = song.getPartArray();
        Phrase[][] songPhrases = new Phrase[song.getSize()][];
        for(int i = 0; i < song.getSize(); i++){
        	songPhrases[i] = songParts[i].getPhraseArray();
        	for(int j=0; j<songParts[i].getSize(); j++){
        		//build on pitch interval tree from song phrase
        		int[] pitchintervals = PhraseAnalysis.pitchIntervals(songPhrases[i][j]);
        		if(pitchintervals.length!=0){
        			pitchTotal+=pitchintervals.length;
        			for(int n=0; n<pitchintervals.length; n++){
        				//if not a rest
        				if(pitchintervals[n]<(Note.MAX_PITCH - Note.MIN_PITCH)){
	        				if(pitchTree.get(pitchintervals[n]) == null){
	        					pitchTree.put(pitchintervals[n], 1);
	        				}
	        				else{
	        					int pitchCount = pitchTree.get(pitchintervals[n]);
	        					pitchTree.put(pitchintervals[n], pitchCount+1);
	        				}
        				}
        			}
        		}
        		else{
        			//do nothing
        		}
        		//build on rhythm tree from song phrase
        		double[] rhythmArray = songPhrases[i][j].getRhythmArray();
        		Note[] notes = songPhrases[i][j].getNoteArray();
        		if(rhythmArray.length!=0){
        			rhythmTotal+=rhythmArray.length;
        			for(int n=0; n<rhythmArray.length; n++){
        				//not a rest
        				if(notes[n].getPitch()>=Note.MIN_PITCH){
	        				if(rhythmTree.get(rhythmArray[n]) == null){
	        					rhythmTree.put(rhythmArray[n], 1);
	        				}
	        				else{
	        					int rhythmCount = rhythmTree.get(rhythmArray[n]);
	        					rhythmTree.put(rhythmArray[n], rhythmCount+1);
	        				}
        				}
        			}
        		}
        		else{
        			//do nothing
        		}
        	}

        }

        //trunkate and round trees, correcting pitch total
        pitchTree = pitchRangeTrunkate(pitchTree);
        rhythmTree = rhythmRound(rhythmTree);

        try{
	        BufferedWriter pitchWriter = new BufferedWriter(new FileWriter(pitchTreeStorage,false));
	        BufferedWriter rhythmWriter = new BufferedWriter(new FileWriter(rhythmTreeStorage,false));
	        File pitchPercentStorage = new File(name + " Pitch Percents.txt");
	        File rhythmPercentStorage = new File(name + " Rhythm Percents.txt");
	        BufferedWriter pitchPercentWriter = new BufferedWriter(new FileWriter(pitchPercentStorage,false));
	        BufferedWriter rhythmPercentWriter = new BufferedWriter(new FileWriter(rhythmPercentStorage,false));

	        Iterator<Integer> pitchIterator = pitchTree.keySet().iterator();
	        Iterator<Double> rhythmIterator = rhythmTree.keySet().iterator();
	        pitchWriter.write(pitchTotal + "\n");
	        while(pitchIterator.hasNext()){
	        	int cur = pitchIterator.next();
	        	pitchWriter.write(cur + " " + pitchTree.get(cur) + "\n");
	        	pitchPercentWriter.write(cur + " " + (((double)pitchTree.get(cur)/(double)pitchTotal)*100) + "\n");
	        }

	        rhythmWriter.write(rhythmTotal + "\n");
	        while(rhythmIterator.hasNext()){
	        	double cur = rhythmIterator.next();
	        	rhythmWriter.write(cur + " " + rhythmTree.get(cur) + "\n");
	        	rhythmPercentWriter.write(cur + " " + (((double)rhythmTree.get(cur)/(double)rhythmTotal)*100) + "\n");
	        }
	        pitchWriter.close();
	        pitchPercentWriter.close();
	        rhythmWriter.close();
	        rhythmPercentWriter.close();
        }
        catch(IOException e){}
	}


	public static TreeMap<Integer,Integer> pitchRangeTrunkate(TreeMap<Integer,Integer> pitchTreeIn){
    	TreeMap<Integer,Integer> pitchTreeOut = new TreeMap<Integer,Integer>();
    	Iterator<Integer> pitchIterator = pitchTreeIn.keySet().iterator();
    	while(pitchIterator.hasNext()){
    		int key = pitchIterator.next();
    		if(-24<=key && key<=24){
    			pitchTreeOut.put(key, pitchTreeIn.get(key));
    		}
    		else{
    			pitchTotal = pitchTotal-pitchTreeIn.get(key);
    		}
    	}
    	return pitchTreeOut;
    }

    public static TreeMap<Double,Integer> rhythmRound(TreeMap<Double,Integer> rhythmTreeIn){
    	TreeMap<Double,Integer> rhythmTreeOut = new TreeMap<Double,Integer>();
    	Iterator<Double> rhythmIterator = rhythmTreeIn.keySet().iterator();
    	while(rhythmIterator.hasNext()){
    		double origKey = rhythmIterator.next();
    		double newKey = Math.round(origKey*(double)100)/(double)100;
    		int curValue = 0;
    		if(rhythmTreeOut.get(newKey)!= null) curValue = rhythmTreeOut.get(newKey);
    		rhythmTreeOut.put(newKey, (rhythmTreeIn.get(origKey)+curValue));
    	}
    	return rhythmTreeOut;
    }


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
