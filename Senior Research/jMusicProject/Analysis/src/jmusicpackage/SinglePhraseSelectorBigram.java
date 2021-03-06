package jmusicpackage;

import java.io.*;
import java.util.*;

import jm.JMC;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;

public class SinglePhraseSelectorBigram implements JMC{

	static int pitchCount = 0;
	static int rhythmCount = 0;

	public static void read(String songName, int CHUNKSIZE, int partNum, int phraseNum, int offset){
		//parameters
		//String songName = "chopin_p15(raindrop).mid";
		//int CHUNKSIZE = 100;
		//int partNum = 1;
		//int phraseNum = 0;
		//int offset = 0;
		int noteNum = offset+CHUNKSIZE;

		TreeMap<String, Integer> pitchByKeyTree = new TreeMap<String, Integer>();
        TreeMap<String, Integer> rhythmTree = new TreeMap<String, Integer>();
        File treeStorage = new File("Random Song Trees.txt");

        Score song = new Score();

        Read.midi(song, songName);
        Part[] songParts = song.getPartArray();
        Phrase[] phrases = new Phrase[songParts[partNum].getSize()];

        phrases = songParts[partNum].getPhraseArray();
        int[] pitches = phrases[phraseNum].getPitchArray();
		if(pitches.length>1){
			for(int n=offset; n<pitches.length-1 && n<noteNum-1; n++){
				//if not a rest
				if(pitches[n]<=Note.MAX_PITCH && pitches[n]>=Note.MIN_PITCH){
					int nextPitch = 0;
					boolean found = false;
					int scan = n+1;
					while(!found&&scan<pitches.length&&scan<noteNum){
						if(pitches[scan]<=Note.MAX_PITCH && pitches[scan]>=Note.MIN_PITCH){
							found = true;
							nextPitch = pitches[scan];
						}
						else scan++;
					}
					if(found){
						//set up key signature information
						int keysig = 0;
		        		int[] scale = MAJOR_SCALE;
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
		        		if(song.getKeyQuality()==1) scale=MINOR_SCALE;
		        		int degree1 = PhraseAnalysis.pitchToDegree(keysig, pitches[n]);
		        		int degree2 = PhraseAnalysis.pitchToDegree(keysig, nextPitch);
		        		String key = Integer.toString(degree1)+" "+Integer.toString(degree2);
        				if(pitchByKeyTree.get(key) == null){
        					pitchByKeyTree.put(key, 1);
        					pitchCount++;
        				}
        				else{
        					int count = pitchByKeyTree.get(key);
        					pitchByKeyTree.put(key, count+1);
        					pitchCount++;
        				}
					}
				}
			}
		}

		double[] rhythmArray = phrases[phraseNum].getRhythmArray();
		Note[] notes = phrases[phraseNum].getNoteArray();
		if(rhythmArray.length>1){
			for(int n=0; n<rhythmArray.length-1 && n<noteNum-1; n++){
				//not a rest
				if(notes[n].getPitch()>=Note.MIN_PITCH){
					Note nextNote = new Note();
					boolean found = false;
					int scan = n+1;
					while(!found&&scan<rhythmArray.length&&scan<noteNum){
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
        					rhythmCount++;
        				}
        				else{
        					int rhythmCount = rhythmTree.get(key);
        					rhythmTree.put(key, rhythmCount+1);
        					rhythmCount++;
        				}
					}
				}
			}
		}
		else{
			//do nothing
		}

		//write the section out to a midi file
		Note[] sectionNotes = new Note[1];
		if(noteNum<pitches.length)
		{
			sectionNotes = new Note[CHUNKSIZE];
			for(int i = offset; i<noteNum; i++){
				sectionNotes[i-offset] = phrases[phraseNum].getNote(i);
			}

		}
		else
		{
			sectionNotes = new Note[pitches.length-offset];
			for(int i = offset; i<pitches.length; i++){
				sectionNotes[i-offset] = phrases[phraseNum].getNote(i);
			}
		}

		Phrase sectionPhrase = new Phrase(sectionNotes);
		Part p = new Part();
		p.addPhrase(sectionPhrase);
		Score s = new Score("Section");
		s.addPart(p);
		Write.midi(s,"SongSection.mid");


        try{
	        BufferedWriter writer = new BufferedWriter(new FileWriter(treeStorage,false));
	        File percentStorage = new File("Random Song Percents.txt");
	        BufferedWriter percentWriter = new BufferedWriter(new FileWriter(percentStorage,false));

	        Iterator<String> pitchIterator = pitchByKeyTree.keySet().iterator();
	        Iterator<String> rhythmIterator = rhythmTree.keySet().iterator();
	        writer.write(pitchCount + "\n");
	        percentWriter.write("Pitches" + "\n");
	        while(pitchIterator.hasNext()){
	        	String cur = pitchIterator.next();
	        	writer.write(cur + " " + pitchByKeyTree.get(cur) + "\n");
	        	percentWriter.write(cur + " " + (((double)pitchByKeyTree.get(cur)/(double)pitchCount)*100) + "\n");
	        }
	        writer.write(rhythmCount + "\n");
	        percentWriter.write("Rhythm" + "\n");
	        while(rhythmIterator.hasNext()){
	        	String cur = rhythmIterator.next();
	        	writer.write(cur + " " + rhythmTree.get(cur) + "\n");
	        	percentWriter.write(cur + " " + (((double)rhythmTree.get(cur)/(double)rhythmCount)*100) + "\n");
	        }

	        writer.close();
	        percentWriter.close();
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
    			pitchCount = pitchCount-pitchTreeIn.get(key);
    		}
    	}
    	return pitchTreeOut;
    }

}
