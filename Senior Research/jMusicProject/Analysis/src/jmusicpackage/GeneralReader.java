package jmusicpackage;

import java.io.*;
import java.util.*;

import jm.JMC;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;

public class GeneralReader implements JMC {

	public static void read(String songPath, String name){

		int pitchTotal = 0;
		int rhythmTotal = 0;

		//TreeMap<Integer, Integer> pitchTree = new TreeMap<Integer, Integer>();
		TreeMap<String, Integer> pitchByKeyTree = new TreeMap<String, Integer>();
        TreeMap<String, Integer> rhythmTree = new TreeMap<String, Integer>();
        /*
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
    	*/

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
					String key = new String();
					int value = 0;
					int numberOfNumbers = 0;
					while(stringParser.hasNext()){
						String temp = stringParser.next();
						if(numberOfNumbers == 0){
							key = temp;
						}
						else if(numberOfNumbers == 1){
							key = key+" "+temp;
						}
						else if(numberOfNumbers == 2){
							value = Integer.parseInt(temp);
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

    	//fetch pitch by key tree (if exists)
        File pitchByKeyTreeStorage = new File(name+" Pitch By Key Tree.txt");
    	try{
    		if(pitchByKeyTreeStorage.exists()){
				Scanner fileScanner = new Scanner(pitchByKeyTreeStorage);
				String input=fileScanner.nextLine();
				Scanner stringParser = new Scanner(input);
				pitchTotal = stringParser.nextInt();
				while(fileScanner.hasNext()){
					input=fileScanner.nextLine();
					stringParser = new Scanner(input);
					String key = new String();
					int value = 0;
					int numberOfNumbers = 0;
					while(stringParser.hasNext()){
						String temp = stringParser.next();
						if(numberOfNumbers == 0){
							key = temp;
						}
						else if(numberOfNumbers == 1){
							key = key+" "+temp;
						}
						else if(numberOfNumbers == 2){
							value = Integer.parseInt(temp);
						}
						numberOfNumbers++;
					}
					pitchByKeyTree.put(key,value);
					stringParser.close();
				}
				fileScanner.close();
    		}
    	}
    	catch(FileNotFoundException e){}

    	//set up midi reading
    	Score song = new Score();
        Read.midi(song, songPath);
        Part[] songParts = song.getPartArray();
        Phrase[][] songPhrases = new Phrase[song.getSize()][];
        for(int i = 0; i < song.getSize(); i++){
        	songPhrases[i] = songParts[i].getPhraseArray();
        	for(int j=0; j<songParts[i].getSize(); j++){
        		/*
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
        		*/
        		//build on pitch by key tree from song phrase
        		int[] pitches = songPhrases[i][j].getPitchArray();
        		if(pitches.length>1){
        			for(int n=0; n<pitches.length-1; n++){
        				//if not a rest
        				if(pitches[n]<=Note.MAX_PITCH && pitches[n]>=Note.MIN_PITCH){
        					int nextPitch = 0;
        					boolean found = false;
        					int scan = n+1;
        					while(!found&&scan<pitches.length){
        						if(pitches[scan]<=Note.MAX_PITCH && pitches[scan]>=Note.MIN_PITCH){
        							found = true;
        							nextPitch = pitches[scan];
        						}
        						else scan++;
        					}
        					if(found){
        						//set up key signature information
        						int keysig = 0;
        		        		//int[] scale = MAJOR_SCALE;
        		        		int sharpsflats = song.getKeySignature();
        		        		if(sharpsflats<0){
        		        			if(sharpsflats==-1) keysig=5;
        		        			else if(sharpsflats==-2) keysig=10;
        		        			else if(sharpsflats==-3) keysig=3;
        		        			else if(sharpsflats==-4) keysig=8;
        		        			else if(sharpsflats==-5) keysig=1;
        		        			else if(sharpsflats==-6) keysig=6;
        		        			else if(sharpsflats==-7) keysig=11;
        		        		}
        		        		else if(sharpsflats>0){
        		        			if(sharpsflats==1) keysig=7;
        		        			else if(sharpsflats==2) keysig=2;
        		        			else if(sharpsflats==3) keysig=9;
        		        			else if(sharpsflats==4) keysig=4;
        		        			else if(sharpsflats==5) keysig=11;
        		        			else if(sharpsflats==6) keysig=6;
        		        			else if(sharpsflats==7) keysig=1;
        		        		}
        		        		//if(song.getKeyQuality()==1) scale=MINOR_SCALE;
        		        		//scale was unessacary. pitch to degree returns 0-11 relative to the tonic pitch 0
        		        		int degree1 = PhraseAnalysis.pitchToDegree(keysig, pitches[n]);
        		        		int degree2 = PhraseAnalysis.pitchToDegree(keysig, nextPitch);
        		        		String key = Integer.toString(degree1)+" "+Integer.toString(degree2);
		        				if(pitchByKeyTree.get(key) == null){
		        					pitchByKeyTree.put(key, 1);
		        					pitchTotal++;
		        				}
		        				else{
		        					int count = pitchByKeyTree.get(key);
		        					pitchByKeyTree.put(key, count+1);
		        					pitchTotal++;
		        				}
        		        		/*
        		        		//scale pitches down into an octave range from the key signature pitch
        		        		int pitch1 = pitches[n];
        		        		int pitch2 = nextPitch;
        		        		while(pitch1>(keysig+12)){
        		        			pitch1 = pitch1 - 12;
        		        		}
        		        		while(pitch2>(keysig+12)){
        		        			pitch2 = pitch2 - 12;
        		        		}

        		        		while
        		        		*/
        					}
        				}
        			}
        		}

        		//build on rhythm tree from song phrase
        		double[] rhythmArray = songPhrases[i][j].getRhythmArray();
        		Note[] notes = songPhrases[i][j].getNoteArray();
        		if(rhythmArray.length>1){
        			for(int n=0; n<rhythmArray.length-1; n++){
        				//not a rest
        				if(notes[n].getPitch()>=Note.MIN_PITCH){
        					Note nextNote = new Note();
        					boolean found = false;
        					int scan = n+1;
        					while(!found&&scan<rhythmArray.length){
        						if(notes[scan].getPitch()>=Note.MIN_PITCH){
        							found = true;
        							nextNote = notes[scan];
        						}
        						else scan++;
        					}
        					if(found){
        						double rhythm1 = Math.round(rhythmArray[n]*100.0)/100.0;
        						double rhythm2 = Math.round(nextNote.getRhythmValue()*100.0)/100.0;
        						String key = Double.toString(rhythm1)+" "+Double.toString(rhythm2);
		        				if(rhythmTree.get(key) == null){
		        					rhythmTree.put(key, 1);
		        					rhythmTotal++;
		        				}
		        				else{
		        					int rhythmCount = rhythmTree.get(key);
		        					rhythmTree.put(key, rhythmCount+1);
		        					rhythmTotal++;
		        				}
        					}
        				}
        			}
        		}
        		else{
        			//do nothing
        		}
        	}

        }

        /*
        //trunkate and round trees, correcting pitch total
        pitchTree = pitchRangeTrunkate(pitchTree);
        rhythmTree = rhythmRound(rhythmTree);
        */

        try{
	        //BufferedWriter pitchWriter = new BufferedWriter(new FileWriter(pitchTreeStorage,false));
        	BufferedWriter pitchByKeyWriter = new BufferedWriter(new FileWriter(pitchByKeyTreeStorage,false));
	        BufferedWriter rhythmWriter = new BufferedWriter(new FileWriter(rhythmTreeStorage,false));
	        File pitchPercentStorage = new File(name + " Pitch Percents.txt");
	        File rhythmPercentStorage = new File(name + " Rhythm Percents.txt");
	        //BufferedWriter pitchPercentWriter = new BufferedWriter(new FileWriter(pitchPercentStorage,false));
	        BufferedWriter pitchByKeyPercentWriter = new BufferedWriter(new FileWriter(pitchPercentStorage,false));
	        BufferedWriter rhythmPercentWriter = new BufferedWriter(new FileWriter(rhythmPercentStorage,false));

	        //Iterator<Integer> pitchIterator = pitchTree.keySet().iterator();
	        Iterator<String> pitchByKeyIterator = pitchByKeyTree.keySet().iterator();
	        Iterator<String> rhythmIterator = rhythmTree.keySet().iterator();
	        /*
	        pitchWriter.write(pitchTotal + "\n");
	        while(pitchIterator.hasNext()){
	        	int cur = pitchIterator.next();
	        	pitchWriter.write(cur + " " + pitchTree.get(cur) + "\n");
	        	pitchPercentWriter.write(cur + " " + (((double)pitchTree.get(cur)/(double)pitchTotal)*100) + "\n");
	        }
	        */

	        pitchByKeyWriter.write(pitchTotal + "\n");
	        while(pitchByKeyIterator.hasNext()){
	        	String cur = pitchByKeyIterator.next();
	        	pitchByKeyWriter.write(cur + " " + pitchByKeyTree.get(cur) + "\n");
	        	//removing the * 100 to correct percents to total to about 1 for the whole list and range from 0 to 1 individually
	        	pitchByKeyPercentWriter.write(cur + " " + ((double)pitchByKeyTree.get(cur)/(double)pitchTotal) + "\n");
	        }

	        rhythmWriter.write(rhythmTotal + "\n");
	        while(rhythmIterator.hasNext()){
	        	String cur = rhythmIterator.next();
	        	rhythmWriter.write(cur + " " + rhythmTree.get(cur) + "\n");
	        	rhythmPercentWriter.write(cur + " " + ((double)rhythmTree.get(cur)/(double)rhythmTotal) + "\n");
	        }
	        pitchByKeyWriter.close();
	        pitchByKeyPercentWriter.close();
	        rhythmWriter.close();
	        rhythmPercentWriter.close();
        }
        catch(IOException e){}
	}

	//no longer used since switch to pitch by key
	/*
	public static TreeMap<Integer,Integer> pitchRangeTrunkate(TreeMap<Integer,Integer> pitchTreeIn){
    	TreeMap<Integer,Integer> pitchTreeOut = new TreeMap<Integer,Integer>();
    	Iterator<Integer> pitchIterator = pitchTreeIn.keySet().iterator();
    	while(pitchIterator.hasNext()){
    		int key = pitchIterator.next();
    		if(-24<=key && key<=24){
    			pitchTreeOut.put(key, pitchTreeIn.get(key));
    		}
    		else{
    			//pitchTotal = pitchTotal-pitchTreeIn.get(key);
    		}
    	}
    	return pitchTreeOut;
    }
    */

	//no longer used to preserve as many variations of rhythm as possible
	/*
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
    */


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
