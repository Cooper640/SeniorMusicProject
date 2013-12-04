package jmusicpackage;

import java.io.*;
import java.util.*;

import jm.JMC;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;

public class SinglePhraseSelector implements JMC{

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

		TreeMap<Integer, Integer> pitchTree = new TreeMap<Integer, Integer>();
        TreeMap<Double, Integer> rhythmTree = new TreeMap<Double, Integer>();
        File treeStorage = new File("Random Song Trees.txt");

        Score song = new Score();

        Read.midi(song, songName);
        Part[] songParts = song.getPartArray();
        Phrase[] phrases = new Phrase[songParts[partNum].getSize()];

        phrases = songParts[partNum].getPhraseArray();
        int[] pitchintervals = PhraseAnalysis.pitchIntervals(phrases[phraseNum]);
        if(pitchintervals.length!=0){
        	if(noteNum>pitchintervals.length){
        		noteNum = pitchintervals.length;
        		offset = noteNum-CHUNKSIZE;
        		if(offset<0) offset=0;
        	}
        	SinglePhraseSelector.pitchCount = (noteNum-1)-offset;
        	SinglePhraseSelector.rhythmCount = noteNum-offset;
			for(int n=offset; n<(noteNum-1); n++){
				if(pitchTree.get(pitchintervals[n]) == null){
					pitchTree.put(pitchintervals[n], 1);
				}
				else{
					int pitchCount = pitchTree.get(pitchintervals[n]);
					pitchTree.put(pitchintervals[n], pitchCount+1);
				}
			}
		}

        double[] rhythmArray = phrases[phraseNum].getRhythmArray();
		if(rhythmArray.length!=0){
			for(int n=offset; n<noteNum; n++){
				if(rhythmTree.get(rhythmArray[n]) == null){
					rhythmTree.put(rhythmArray[n], 1);
				}
				else{
					int rhythmCount = rhythmTree.get(rhythmArray[n]);
					rhythmTree.put(rhythmArray[n], rhythmCount+1);
				}
			}
		}

		//write the section out to a midi file
		Note[] sectionNotes = new Note[noteNum-offset];
		for(int i = offset; i<noteNum; i++){
			sectionNotes[i-offset] = phrases[phraseNum].getNote(i);
		}

		Phrase sectionPhrase = new Phrase(sectionNotes);
		Part p = new Part();
		p.addPhrase(sectionPhrase);
		Score s = new Score("Section");
		s.addPart(p);
		Write.midi(s,"SongSection.mid");

		//fix trees
		pitchTree = pitchRangeTrunkate(pitchTree);
        rhythmTree = Reader.rhythmRound(rhythmTree);


        try{
	        BufferedWriter writer = new BufferedWriter(new FileWriter(treeStorage,false));
	        File percentStorage = new File("Random Song Percents.txt");
	        BufferedWriter percentWriter = new BufferedWriter(new FileWriter(percentStorage,false));

	        Iterator<Integer> pitchIterator = pitchTree.keySet().iterator();
	        Iterator<Double> rhythmIterator = rhythmTree.keySet().iterator();
	        writer.write(SinglePhraseSelector.pitchCount + "\n");
	        percentWriter.write("Pitches" + "\n");
	        while(pitchIterator.hasNext()){
	        	int cur = pitchIterator.next();
	        	writer.write(cur + " " + pitchTree.get(cur) + "\n");
	        	percentWriter.write(cur + " " + (((double)pitchTree.get(cur)/(double)SinglePhraseSelector.pitchCount)*100) + "\n");
	        }
	        writer.write(SinglePhraseSelector.rhythmCount + "\n");
	        percentWriter.write("Rhythm" + "\n");
	        while(rhythmIterator.hasNext()){
	        	double cur = rhythmIterator.next();
	        	writer.write(cur + " " + rhythmTree.get(cur) + "\n");
	        	percentWriter.write(cur + " " + (((double)rhythmTree.get(cur)/(double)SinglePhraseSelector.rhythmCount)*100) + "\n");
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
    			SinglePhraseSelector.pitchCount = SinglePhraseSelector.pitchCount-pitchTreeIn.get(key);
    		}
    	}
    	return pitchTreeOut;
    }

}
