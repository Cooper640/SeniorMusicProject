package jmusicpackage;

import java.io.*;
import java.util.*;

import jm.JMC;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;

public class GeneralReaderTrigram implements JMC {

	//static int pitchTotal = 0;
	//static int rhythmTotal = 0;

	public static void read(String songPath, String name){

		int pitchTotal = 0;
		int rhythmTotal = 0;

		TreeMap<String, Integer> pitchByKeyTree = new TreeMap<String, Integer>();
        TreeMap<String, Integer> rhythmTree = new TreeMap<String, Integer>();

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
							key = key+" "+temp;
						}
						else if(numberOfNumbers == 3){
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
							key = key+" "+temp;
						}
						else if(numberOfNumbers == 3){
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
        		//build on pitch by key tree from song phrase
        		int[] pitches = songPhrases[i][j].getPitchArray();
        		if(pitches.length>2){
        			for(int n=0; n<pitches.length-2; n++){
        				//if not a rest
        				if(pitches[n]<=Note.MAX_PITCH && pitches[n]>=Note.MIN_PITCH){
        					int nextPitch = 0;
        					int thirdPitch = 0;
        					boolean found = false;
        					int scan = n+1;
        					while(!found&&scan<pitches.length){
        						if(pitches[scan]<=Note.MAX_PITCH && pitches[scan]>=Note.MIN_PITCH){
        							found = true;
        							nextPitch = pitches[scan];
        						}
        						else scan++;
        					}
        					scan++;
        					found = false;
        					while(!found&&scan<pitches.length){
        						if(pitches[scan]<=Note.MAX_PITCH && pitches[scan]>=Note.MIN_PITCH){
        							found = true;
        							thirdPitch = pitches[scan];
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
        		        		int degree3 = PhraseAnalysis.pitchToDegree(keysig, thirdPitch);
        		        		String key = Integer.toString(degree1)+" "+Integer.toString(degree2)+" "+Integer.toString(degree3);
		        				if(pitchByKeyTree.get(key) == null){
		        					pitchByKeyTree.put(key, 1);
		        					pitchTotal++;
		        				}
		        				else{
		        					int count = pitchByKeyTree.get(key);
		        					pitchByKeyTree.put(key, count+1);
		        					pitchTotal++;
		        				}
        					}
        				}
        			}
        		}

        		//build on rhythm tree from song phrase
        		double[] rhythmArray = songPhrases[i][j].getRhythmArray();
        		Note[] notes = songPhrases[i][j].getNoteArray();
        		if(rhythmArray.length>2){
        			//same issue as with pitch total, same fix
        			//rhythmTotal+=rhythmArray.length;
        			for(int n=0; n<rhythmArray.length-2; n++){
        				//not a rest
        				if(notes[n].getPitch()>=Note.MIN_PITCH){
        					Note nextNote = new Note();
        					Note thirdNote = new Note();
        					boolean found = false;
        					int scan = n+1;
        					while(!found&&scan<rhythmArray.length){
        						if(notes[scan].getPitch()>=Note.MIN_PITCH){
        							found = true;
        							nextNote = notes[scan];
        						}
        						else scan++;
        					}
        					scan++;
        					found = false;
        					while(!found&&scan<rhythmArray.length){
        						if(notes[scan].getPitch()>=Note.MIN_PITCH){
        							found = true;
        							thirdNote = notes[scan];
        						}
        						else scan++;
        					}
        					if(found){
        						double rhythm1 = Math.round(rhythmArray[n]*100.0)/100.0;
        						double rhythm2 = Math.round(nextNote.getRhythmValue()*100.0)/100.0;
        						double rhythm3 = Math.round(thirdNote.getRhythmValue()*100.0)/100.0;
        						String key = Double.toString(rhythm1)+" "+Double.toString(rhythm2)+" "+Double.toString(rhythm3);
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

        try{
        	BufferedWriter pitchByKeyWriter = new BufferedWriter(new FileWriter(pitchByKeyTreeStorage,false));
	        BufferedWriter rhythmWriter = new BufferedWriter(new FileWriter(rhythmTreeStorage,false));
	        File pitchPercentStorage = new File(name + " Pitch Percents.txt");
	        File rhythmPercentStorage = new File(name + " Rhythm Percents.txt");
	        BufferedWriter pitchByKeyPercentWriter = new BufferedWriter(new FileWriter(pitchPercentStorage,false));
	        BufferedWriter rhythmPercentWriter = new BufferedWriter(new FileWriter(rhythmPercentStorage,false));

	        Iterator<String> pitchByKeyIterator = pitchByKeyTree.keySet().iterator();
	        Iterator<String> rhythmIterator = rhythmTree.keySet().iterator();

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
}
